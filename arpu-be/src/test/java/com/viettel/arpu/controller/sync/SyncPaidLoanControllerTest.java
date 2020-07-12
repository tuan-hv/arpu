package com.viettel.arpu.controller.sync;

import com.viettel.arpu.config.FtpStorageProperties;
import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.controller.mobile.MobileLoanController;
import com.viettel.arpu.controller.sftp.SyncController;
import com.viettel.arpu.model.dto.WithdrawMoneyDTO;
import com.viettel.arpu.model.entity.Version;
import com.viettel.arpu.model.request.WithdrawMoneyForm;
import com.viettel.arpu.model.response.BaseResponse;
import com.viettel.arpu.model.response.mb.VtStatusResponse;
import com.viettel.arpu.service.loan.MobileService;
import com.viettel.arpu.service.sftp.SFTPService;
import com.viettel.arpu.service.sftp.manager.DataMapSFTP;
import com.viettel.arpu.service.sftp.manager.SFTPManager;
import com.viettel.arpu.service.sftp.manager.WhiteListProcessor;
import com.viettel.arpu.service.version.VersionService;
import org.hibernate.engine.spi.VersionValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author tuongvx
 * @created 26/06/2020 - 4:28 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SyncPaidLoanControllerTest {

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
        this.mockMvc.perform(get("/api/sync/whitelist"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Success", is("Success")));
    }

    @Test
    public void testItShouldBeFailedWithWrongAPIAddress() throws Exception {
        this.mockMvc.perform(get("/api/sync/whiteli"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testItShouldBeFailedWithConnectServer() throws Exception {
        ftpStorageProperties.setBatchUser("fake");
        this.mockMvc.perform(get("/api/sync/whitelist"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Failed", is("Kết nối server SFTP thất bại.")));
    }
}
