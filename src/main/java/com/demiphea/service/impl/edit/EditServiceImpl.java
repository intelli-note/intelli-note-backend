package com.demiphea.service.impl.edit;

import com.alibaba.fastjson2.JSONObject;
import com.demiphea.common.Constant;
import com.demiphea.exception.common.CommonServiceException;
import com.demiphea.service.inf.edit.EditService;
import com.demiphea.utils.aliyun.nls.SpeechFlashRecognizerUtil;
import com.demiphea.utils.aliyun.ocr.RecognizeEduFormulaUtil;
import com.demiphea.utils.oss.qiniu.OssUtils;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

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

    @Override
    public JSONObject speechFlashRecognizer(MultipartFile audio, String type) throws IOException, URISyntaxException, ParseException {
        SpeechFlashRecognizerUtil.Format format;
        try {
            format = SpeechFlashRecognizerUtil.Format.valueOf(type.toUpperCase());
        } catch (Exception e) {
            throw new CommonServiceException("不能识别的音频类型");
        }
        return SpeechFlashRecognizerUtil.call(audio, format);
    }

    @Override
    public JSONObject recognizeFormula(MultipartFile formula) throws Exception {
        return RecognizeEduFormulaUtil.call(formula);
    }
}
