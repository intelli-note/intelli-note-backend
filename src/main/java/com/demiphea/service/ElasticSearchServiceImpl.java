package com.demiphea.service;

import com.demiphea.entity.es.NoteDoc;
import com.demiphea.entity.es.UserDoc;
import com.demiphea.service.inf.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ElasticSearchServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class ElasticSearchServiceImpl implements ElasticSearchService {
    private final ElasticsearchTemplate template;

    @Override
    public List<UserDoc> searchUser(@NotNull String key, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(ma -> ma.field("summary").query(key)))
                .withPageable(PageRequest.of(pageNum - 1, pageSize))
                .build();
        SearchHits<UserDoc> hits = template.search(query, UserDoc.class);
        if (hits.hasSearchHits()) {
            return new ArrayList<>(hits.getSearchHits().stream().map(SearchHit::getContent).toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<NoteDoc> searchNote(@NotNull String key, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        return null;
    }
}
