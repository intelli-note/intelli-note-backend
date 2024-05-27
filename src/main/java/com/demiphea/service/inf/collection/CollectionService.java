package com.demiphea.service.inf.collection;

import com.demiphea.model.vo.collection.CollectionVo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * CollectionService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface CollectionService {
    CollectionVo insertCollection(@NotNull Long id, @NotNull String name, @Nullable MultipartFile cover, @Nullable String description, @NotNull Boolean publicOption) throws IOException;
}
