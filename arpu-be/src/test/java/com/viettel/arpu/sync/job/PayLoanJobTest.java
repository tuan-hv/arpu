package com.viettel.arpu.sync.job;

import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.job.JobSyncPayLoan;
import com.viettel.arpu.model.entity.JobConfig;
import com.viettel.arpu.model.entity.Loan;
import com.viettel.arpu.model.entity.SyncAuditLog;
import com.viettel.arpu.repository.JobConfigRepository;
import com.viettel.arpu.repository.LoanRepository;
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
public class PayLoanJobTest {
    @Autowired
    private JobSyncPayLoan jobSyncPayLoan;
    @Autowired
    VersionRepository versionRepository;
    @Autowired
    private SyncAuditRepository syncAuditRepository;
    @Autowired
    private LoanRepository loanRepository;
    @MockBean
    JobConfigRepository jobConfigRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSyncPayLoan() throws Exception {
        jobSyncPayLoan.syncPaidLoan();
        Long version = versionRepository.getLatestIdForBatchId(2l);
        then(version).isNotNull();
        List<SyncAuditLog> sync = syncAuditRepository.findAllByVersionId(version);
        then(sync).isNotEmpty();
        Optional<Loan> loan = loanRepository.findFirstByLoanAccount("12345678");
        then(loan).isNotEmpty();
    }

    @Test
    public void testGetCronPayLoanValueSuccess() throws Exception {
        JobConfig jobConfig = new JobConfig();
        jobConfig.setBatchId(2l);
        jobConfig.setCron("*/10 * * * * *");
        jobConfig.setDescription(null);
        then(jobConfig.getCron()).isNotNull();
        then(jobConfig.getDescription()).isNull();
        then(jobConfig.getBatchId()).isEqualTo(2l);
        Mockito.when(jobConfigRepository.findById(BatchConstant.PAY_LOAN_BATCH_ID)).thenReturn(Optional.of(jobConfig));
        String str = jobSyncPayLoan.getCronPayLoanValue();
        then(str).isNotNull();
        then(str).isEqualTo("*/10 * * * * *");
    }

    @Test
    public void testGetCronPayLoanValueFailedWithNullJobConfig() throws Exception {
        Mockito.when(jobConfigRepository.findById(2l)).thenReturn(null);
        String str = jobSyncPayLoan.getCronPayLoanValue();
        then(str).isNotNull();
        then(str).isEqualTo("0 0 0 * * *");
    }

    @Test
    public void testGetCronPayLoanValueFailedWithNullCron() throws Exception {
        JobConfig jobConfig = new JobConfig();
        then(jobConfig.getCron()).isNotNull();
        then(jobConfig.getDescription()).isNotNull();
        then(jobConfig.getBatchId()).isNull();
        Mockito.when(jobConfigRepository.findById(2l)).thenReturn(Optional.of(jobConfig));
        String str = jobSyncPayLoan.getCronPayLoanValue();
        then(str).isNotNull();
        then(str).isEqualTo("0 0 0 * * *");
    }
}
