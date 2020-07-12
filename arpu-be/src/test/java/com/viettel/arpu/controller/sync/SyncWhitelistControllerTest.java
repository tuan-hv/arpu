package com.viettel.arpu.controller.sync;

import com.viettel.arpu.config.FtpStorageProperties;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.SyncAuditLog;
import com.viettel.arpu.repository.CustomerRepository;
import com.viettel.arpu.repository.SyncAuditRepository;
import com.viettel.arpu.repository.VersionRepository;
import com.viettel.arpu.service.sftp.manager.DataMapSFTP;
import com.viettel.arpu.service.sftp.manager.SFTPManager;
import com.viettel.arpu.service.sftp.manager.WhiteListProcessor;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SyncWhitelistControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FtpStorageProperties ftpStorageProperties;

    @MockBean
    DataMapSFTP dataMapSFTP;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSyncWhiteList() throws Exception {
        ftpStorageProperties.setBatchUser("arpu");
        this.mockMvc.perform(get("/api/sync/payloan"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Success", is("Success")));
    }

    @Test
    public void testItShouldBeFailedWithWrongAPIAddress() throws Exception {
        this.mockMvc.perform(get("/api/sync/payloa"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testItShouldBeFailedWithConnectServer() throws Exception {
        ftpStorageProperties.setBatchUser("fake");
        Mockito.when(dataMapSFTP.isConnectedToSFTPServer()).thenReturn(false);
        this.mockMvc.perform(get("/api/sync/payloan"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Failed", is("Kết nối server SFTP thất bại.")));
    }

}
