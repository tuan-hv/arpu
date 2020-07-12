/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.controller.ftpserver;

import com.viettel.arpu.config.FtpStorageProperties;
import com.viettel.arpu.config.SftpInfo;
import com.viettel.arpu.utils.FtpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class FTPController {

    @Autowired
    private FtpStorageProperties ftpStorageProperties;

    @GetMapping(value = "/download", params = {"path!="})
    public ResponseEntity<InputStreamResource> downloadPrototypeContract(
            @RequestParam(required = false) String path) throws IOException {

        String path2File;
        if (!StringUtils.isEmpty(path)) {
            path2File = ftpStorageProperties.getUploadDir() + path;
        } else {
            path2File = ftpStorageProperties.getTemplateFile();
        }

        SftpInfo sftpInfoEx = SftpInfo.builder()
                .password(ftpStorageProperties.getPwd())
                .path2File(path2File)
                .port(Integer.parseInt(ftpStorageProperties.getPort()))
                .username(ftpStorageProperties.getUser())
                .serverAddress(ftpStorageProperties.getHost())
                .build();

        Resource pdfFile = new ByteArrayResource(FtpUtils.fileInBytesFromFtp(sftpInfoEx));
        return fileFromFTP(pdfFile);
    }

    private ResponseEntity<InputStreamResource> fileFromFTP(Resource pdfFile) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Disposition", "inline;filename=" + "construct.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfFile.contentLength())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(new InputStreamResource(pdfFile.getInputStream()));
    }


}
