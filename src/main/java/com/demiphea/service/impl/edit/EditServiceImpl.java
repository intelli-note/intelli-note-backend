package com.demiphea.service.impl.edit;

import com.demiphea.common.Constant;
import com.demiphea.service.inf.edit.EditService;
import com.demiphea.utils.oss.qiniu.OssUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * EditServiceImpl
 *
 * @author demiphea
 * @since 17.0.9
 */
@Service
@RequiredArgsConstructor
public class EditServiceImpl implements EditService {
    @Override
    public String upload(MultipartFile file) throws IOException {
        return OssUtils.upload(Constant.TMP_DIR, null, file);
    }
}
