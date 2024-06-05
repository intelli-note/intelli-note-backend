package com.demiphea.service.impl;

import com.demiphea.core.mq.ElasticSearchListener;
import com.demiphea.entity.Note;
import com.demiphea.entity.User;
import com.demiphea.service.inf.MessageQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * MessageQueueServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class MessageQueueServiceImpl implements MessageQueueService {
    private final RabbitTemplate template;

    @Override
    public void syncES() {
        template.convertAndSend(ElasticSearchListener.EXCHANGE_NAME, "sync", "");
    }

    @Override
    public void saveUserToES(User user) {
        template.convertAndSend(ElasticSearchListener.EXCHANGE_NAME, "user.save", user);
    }

    @Override
    public void saveNoteToES(Note note) {
        template.convertAndSend(ElasticSearchListener.EXCHANGE_NAME, "note.save", note);
    }

    @Override
    public void deleteNoteInES(Long noteId) {
        template.convertAndSend(ElasticSearchListener.EXCHANGE_NAME, "note.delete", noteId);
    }
}
