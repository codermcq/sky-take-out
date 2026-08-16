package com.starchef.controller.user;

import com.starchef.constant.MessageConstant;
import com.starchef.result.Result;
import com.starchef.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user/common")
@Api(tags = "C端通用接口")
public class UserCommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("C端文件上传: {}", file != null ? file.getOriginalFilename() : "null");

        try {
            assert file != null;
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID() + ext;

            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败: ", e);
        }

        return Result.success(MessageConstant.UPLOAD_FAILED);
    }
}
