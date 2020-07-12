/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.viettel.arpu.controller.ftp;

import com.viettel.arpu.config.FtpStorageProperties;
import com.viettel.arpu.controller.ftpserver.FTPController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FTPControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    FtpStorageProperties ftpStorageProperties;

    @Autowired
    private Environment env;


    @Test
    void testReturnInternalSeverWhenConnectServerFalse() throws Exception {

        ftpStorageProperties.setHost("host");
        ftpStorageProperties.setPort("port");
        ftpStorageProperties.setUser("user");
        ftpStorageProperties.setPwd("pass");
        ftpStorageProperties.setUploadDir("uploadDir");
        ftpStorageProperties.setTemplateFile("templateFile");
        this.mockMvc.perform(get("/api/download")
                .param("path", "1591783125728.pdf"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("lỗi máy chủ")))
                .andExpect(jsonPath("$.code", is("ARPU500")));
    }

    @Test
    void testReturnSuccessWhenConnectServerSuccess() throws Exception {
        String host = env.getProperty("arpu.sftp.host");
        String port = env.getProperty("arpu.sftp.port");
        String user = env.getProperty("arpu.sftp.user");
        String pass = env.getProperty("arpu.sftp.pwd");
        String uploadDir = env.getProperty("arpu.sftp.uploadDir");
        String templateFile = env.getProperty("arpu.sftp.templateFile");

        ftpStorageProperties.setHost(host);
        ftpStorageProperties.setPort(port);
        ftpStorageProperties.setUser(user);
        ftpStorageProperties.setPwd(pass);
        ftpStorageProperties.setUploadDir(uploadDir);
        ftpStorageProperties.setTemplateFile(templateFile);
        this.mockMvc.perform(get("/api/download")
                .param("path", "1591783125728.pdf"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testReturnSuccessAndGetFileTemplate() throws Exception {
        String host = env.getProperty("arpu.sftp.host");
        String port = env.getProperty("arpu.sftp.port");
        String user = env.getProperty("arpu.sftp.user");
        String pass = env.getProperty("arpu.sftp.pwd");
        String uploadDir = env.getProperty("arpu.sftp.uploadDir");
        String templateFile = env.getProperty("arpu.sftp.templateFile");

        ftpStorageProperties.setHost(host);
        ftpStorageProperties.setPort(port);
        ftpStorageProperties.setUser(user);
        ftpStorageProperties.setPwd(pass);
        ftpStorageProperties.setUploadDir(uploadDir);
        ftpStorageProperties.setTemplateFile(templateFile);

        this.mockMvc.perform(get("/api/download"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
