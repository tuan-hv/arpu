package com.viettel.arpu.sync.job;

import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.job.JobSyncWhitelist;
import com.viettel.arpu.model.entity.JobConfig;
import com.viettel.arpu.model.entity.SyncAuditLog;
import com.viettel.arpu.repository.JobConfigRepository;
import com.viettel.arpu.repository.SyncAuditRepository;
import com.viettel.arpu.repository.VersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author tuongvx
 * @created 29/06/2020 - 10:08 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WhiteListJobTest {
    @Autowired
    private JobSyncWhitelist jobSyncWhitelist;
    @Autowired
    VersionRepository versionRepository;
    @Autowired
    private SyncAuditRepository syncAuditRepository;
    @MockBean
    JobConfigRepository jobConfigRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSyncPayLoan() throws Exception {
        jobSyncWhitelist.syncWhiteList();
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> sync = syncAuditRepository.findAllByVersionId(version);
        then(sync).isNotEmpty();
    }

    @Test
    public void testGetCronPayLoanValueSuccess() throws Exception {
        JobConfig jobConfig = new JobConfig();
        jobConfig.setBatchId(1l);
        jobConfig.setCron("*/10 * * * * *");
        jobConfig.setDescription(null);
        Mockito.when(jobConfigRepository.findById(BatchConstant.PAY_LOAN_BATCH_ID)).thenReturn(Optional.of(jobConfig));
        String str = jobSyncWhitelist.getCronWhitelistValue();
        then(str).isNotNull();
        then(str).isEqualTo("*/10 * * * * *");
    }

    @Test
    public void testGetCronPayLoanValueFailedWithNullJobConfig() throws Exception {
        Mockito.when(jobConfigRepository.findById(2l)).thenReturn(null);
        String str = jobSyncWhitelist.getCronWhitelistValue();
        then(str).isNotNull();
        then(str).isEqualTo("0 0 0 * * *");
    }

    @Test
    public void testGetCronPayLoanValueFailedWithNullCron() throws Exception {
        JobConfig jobConfig = new JobConfig();
        Mockito.when(jobConfigRepository.findById(2l)).thenReturn(Optional.of(jobConfig));
        String str = jobSyncWhitelist.getCronWhitelistValue();
        then(str).isNotNull();
        then(str).isEqualTo("0 0 0 * * *");
    }
}
