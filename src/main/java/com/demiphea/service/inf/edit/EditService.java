package com.demiphea.service.inf.edit;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * EditService
 *
 * @author demiphea
 * @since 17.0.9
 */
public interface EditService {
    /**
     * 获取文件链接
     *
     * @param file 文件
     * @return {@link String} 链接
     * @author demiphea
     */
    String upload(MultipartFile file) throws IOException;
}
