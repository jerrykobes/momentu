package com.momentu.momentuapi.controllers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.momentu.momentuapi.security.auth.jwt.extractor.ClaimExtractor;
import com.momentu.momentuapi.storage.config.S3Settings;
import com.momentu.momentuapi.storage.key.S3KeyGenerator;
import com.momentu.momentuapi.storage.s3.S3Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Collections;
import java.util.Map;

@RepositoryRestController
@RequestMapping("/")
public class MediaController {
    private final ClaimExtractor claimExtractor;
    private final S3Manager s3Manager;
    private final S3Settings s3Settings;
    private final S3KeyGenerator s3KeyGenerator;

    @Autowired
    MediaController(ClaimExtractor claimExtractor, S3Manager s3Manager, S3Settings s3Settings, S3KeyGenerator s3KeyGenerator) {
        this.claimExtractor = claimExtractor;
        this.s3Manager = s3Manager;
        this.s3Settings = s3Settings;
        this.s3KeyGenerator = s3KeyGenerator;
    }

    @RequestMapping(value = "/media_upload", method = RequestMethod.POST)
    public @ResponseBody Map uploadMedia(@RequestHeader HttpHeaders headers, @RequestParam("file") MultipartFile file) {
        InputStream inputStream = null;
        if (!file.isEmpty()) {
            try {
                byte[] fileBytes = file.getBytes();
                Long fileLength = new Long(fileBytes.length);
                inputStream = new ByteArrayInputStream(fileBytes);
                Long contentLength = fileLength;//headers.getContentLength();
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(contentLength);
                String keyName = s3KeyGenerator.getUniqueKey();

                AWSCredentials awsCredentials = new BasicAWSCredentials(s3Settings.getAccessKeyId(), s3Settings.getSecretAccessKey());
                s3Manager.upload(awsCredentials, s3Settings.getMediaBucketName(), keyName, inputStream, objectMetadata);

                return Collections.singletonMap("status", "success");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Collections.singletonMap("status", "fail");
    }
}