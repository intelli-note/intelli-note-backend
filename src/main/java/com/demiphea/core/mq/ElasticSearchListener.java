package com.demiphea.core.mq;

import com.demiphea.dao.NoteDao;
import com.demiphea.dao.UserDao;
import com.demiphea.entity.Note;
import com.demiphea.entity.User;
import com.demiphea.entity.es.NoteDoc;
import com.demiphea.entity.es.UserDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

/**
 * ElasticSearchListener
 *
 * @author demiphea
 * @since 17.0.9
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchListener {
    public static final String EXCHANGE_NAME = "es_module";

    private final ElasticsearchTemplate template;
    private final UserDao userDao;
    private final NoteDao noteDao;

    private UserDoc convert(@NotNull User user) {
        return new UserDoc(
                user.getId(),
                user.getId(),
                user.getUsername(),
                user.getBiography(),
                user.getGender() == null ? "未知" :
                        user.getGender() ? "男" :
                                "女"
        );
    }

    private NoteDoc convert(@NotNull Note note) {
        User author = userDao.selectById(note.getUserId());
        return new NoteDoc(
                note.getId(),
                note.getId(),
                note.getTitle(),
                note.getContent(),
                author.getId(),
                author.getUsername(),
                note.getCreateTime(),
                note.getUpdateTime(),
                note.getPrice()
        );
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("sync"),
            exchange = @Exchange(value = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = "sync"
    ))
    public void sync() {
        log.info("[sync]: Prepare data synchronization between Elasticsearch and MySQL database...");

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.matchAll(ma -> ma))
                .build();

        // 同步user
        userDao.selectList(null).forEach(user -> {
            UserDoc source = convert(user);
            UserDoc dest = template.get(source.getId().toString(), UserDoc.class);
            if (!source.equals(dest)) {
                dest = template.save(source);
                log.info("[sync]: [db]" + source + " =========> [es]" + dest);
            }
        });
        IndexCoordinates userIndex = IndexCoordinates.of("intelli_note_user");
        SearchScrollHits<UserDoc> userScroll = template.searchScrollStart(10000L, query, UserDoc.class, userIndex);
        String scrollId = userScroll.getScrollId();
        while (userScroll.hasSearchHits()) {
            for (SearchHit<UserDoc> hit : userScroll.getSearchHits()) {
                UserDoc source = hit.getContent();
                if (userDao.selectById(source.getId()) == null) {
                    template.delete(source);
                    log.info("[sync]: [es] ===delete===> " + source);
                }
            }
            scrollId = userScroll.getScrollId();
            userScroll = template.searchScrollContinue(scrollId, 10000L, UserDoc.class, userIndex);
        }
        template.searchScrollClear(scrollId);

        // 同步note
        noteDao.selectList(null).forEach(note -> {
            NoteDoc source = convert(note);
            NoteDoc dest = template.get(source.getId().toString(), NoteDoc.class);
            if (!source.equals(dest)) {
                dest = template.save(source);
                log.info("[sync]: [db]" + source + " =========> [es]" + dest);
            }
        });
        IndexCoordinates noteIndex = IndexCoordinates.of("intelli_note_note");
        SearchScrollHits<NoteDoc> noteScroll = template.searchScrollStart(10000L, query, NoteDoc.class, noteIndex);
        scrollId = noteScroll.getScrollId();
        while (noteScroll.hasSearchHits()) {
            for (SearchHit<NoteDoc> hit : noteScroll) {
                NoteDoc source = hit.getContent();
                if (noteDao.selectById(source.getId()) == null) {
                    template.delete(source);
                    log.info("[sync]: [es] ===delete===> " + source);
                }
            }
            scrollId = noteScroll.getScrollId();
            noteScroll = template.searchScrollContinue(scrollId, 10000L, NoteDoc.class, noteIndex);
        }
        template.searchScrollClear(scrollId);
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("user::save"),
            exchange = @Exchange(value = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = "user.save"
    ))
    public void saveUser(@NotNull User user) {
        log.info("[user.save]: " + user);
        template.save(convert(user));
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("note::save"),
            exchange = @Exchange(value = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = "note.save"
    ))
    public void saveNote(@NotNull Note note) {
        log.info("[note.save]: " + note);
        template.save(convert(note));
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("note::delete"),
            exchange = @Exchange(value = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = "note.delete"
    ))
    public void deleteNote(@NotNull Long noteId) {
        log.info("[note.delete]: " + noteId);
        template.delete(noteId.toString(), NoteDoc.class);
    }
}
