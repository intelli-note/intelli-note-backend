package com.demiphea.controller.utils;

import com.alibaba.fastjson2.JSONObject;
import com.demiphea.utils.aliyun.nls.FileTransUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FileTransCallback
 * 录音文件识别回调
 *
 * @author demiphea
 * @since 17.0.9
 */
@RestController
@RequestMapping("/FileTrans")
public class FileTransCallback {
    @PostMapping("/callback")
    public void callback(@RequestBody String res) {
        JSONObject result = FileTransUtil.parseResult(res);
        System.out.println(result);
    }
}
