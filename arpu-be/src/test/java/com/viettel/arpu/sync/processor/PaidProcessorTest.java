package com.viettel.arpu.sync.processor;

/**
 * @author tuongvx
 * @created 27/06/2020 - 5:06 PM
 */

import com.viettel.arpu.config.FtpStorageProperties;
import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.model.entity.Loan;
import com.viettel.arpu.model.entity.SyncAuditLog;
import com.viettel.arpu.model.entity.Version;
import com.viettel.arpu.repository.LoanRepository;
import com.viettel.arpu.repository.SyncAuditRepository;
import com.viettel.arpu.repository.VersionRepository;
import com.viettel.arpu.service.sftp.SFTPService;
import com.viettel.arpu.service.sftp.manager.DataMapSFTP;
import com.viettel.arpu.service.sftp.manager.PaidProcessor;
import com.viettel.arpu.service.sftp.validator.ValidatorService;
import com.viettel.arpu.service.version.VersionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaidProcessorTest {

    @Autowired
    @Qualifier("syncPLValidatorServiceImpl")
    private ValidatorService syncValidatorService;
    @Autowired
    private SFTPService sftpService;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private VersionService versionService;
    @Autowired
    private VersionRepository versionRepository;
    @Autowired
    private FtpStorageProperties ftpStorageProperties;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SyncAuditRepository syncAuditRepository;
    @Autowired
    private PaidProcessor paidProcessor;


    @BeforeEach
    public void setup() throws Exception {
        paidProcessor.process();
    }

    @Test
    public void testItShouldBeSuccessCreateNewDataMapSFTPForPaidLoanFailed() throws Exception {
        DataMapSFTP dataMapSFTP = new DataMapSFTP(ftpStorageProperties, messageSource,
                sftpService, versionService).invoke(BatchConstant.WHITE_LIST);
        then(dataMapSFTP).isNotNull();
        then(dataMapSFTP.getVersion()).isNotNull();
        then(dataMapSFTP.getSftpInfoEx()).isNotNull();
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        then(dataMapSFTP.getVersion().getId()).isEqualTo(version);
        then(dataMapSFTP.getSftpInfoEx().getPath2File()).isEqualTo("/home/arpu/csv/whitelist_test.txt");
        then(dataMapSFTP.isConnectedToSFTPServer()).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void testItShouldBeSuccessCreateNewDataMapSFTPForPaidLoanSuccess() throws Exception {
        DataMapSFTP dataMapSFTP = new DataMapSFTP(ftpStorageProperties, messageSource,
                sftpService, versionService).invoke(BatchConstant.MALE);
        then(dataMapSFTP).isNotNull();
        then(dataMapSFTP.getVersion()).isNotNull();
        then(dataMapSFTP.getSftpInfoEx()).isNotNull();
        Long version = versionRepository.getLatestIdForBatchId(2l);
        then(version).isNotNull();
        then(dataMapSFTP.getVersion().getId()).isEqualTo(version);
        then(dataMapSFTP.getSftpInfoEx().getPath2File()).isEqualTo("/home/arpu/csv/payloan_test.txt");
        then(dataMapSFTP.isConnectedToSFTPServer()).isEqualTo(Boolean.TRUE);
    }


    @Test
    public void testItShouldHasLoanUpdateLoanStatusSuccess() {
        Long version = versionRepository.getLatestIdForBatchId(2l);
        then(version).isNotNull();
        then(version).isGreaterThan(4l);
        List<SyncAuditLog> sync = syncAuditRepository.findAllByVersionId(version);
        then(sync).isNotEmpty();
        then(sync.size()).isGreaterThanOrEqualTo(4);
        Optional<Loan> loan = loanRepository.findFirstByLoanAccount("12345678");
        then(loan).isNotEmpty();
        then(loan.get().getLoanStatus().getId()).isEqualTo("KVS_04");
    }

    @Test
    public void testItShouldHasAuditLogForRecordFirst() {
        Long version = versionRepository.getLatestIdForBatchId(2l);
        then(version).isNotNull();
        List<SyncAuditLog> logs = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 1l);
        then(logs).isNotEmpty();
        then(logs.size()).isEqualTo(1l);
    }

    @Test
    public void testItShouldHasAuditLogForRecordSecond() {
        Long version = versionRepository.getLatestIdForBatchId(2l);
        then(version).isNotNull();
        List<SyncAuditLog> logs = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 2l);
        then(logs).isNotEmpty();
        then(logs.size()).isEqualTo(1l);
    }

    @Test
    public void testItShouldHasAuditLogForRecordThree() {
        Long version = versionRepository.getLatestIdForBatchId(2l);
        then(version).isNotNull();
        List<SyncAuditLog> logs = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 3l);
        then(logs).isNotEmpty();
        then(logs.size()).isEqualTo(1l);
    }

    @Test
    public void testItShouldHasAuditLogForRecordFour() {
        Long version = versionRepository.getLatestIdForBatchId(2l);
        List<Version> opt = versionRepository.findAll();
        then(version).isNotNull();
        List<SyncAuditLog> sync = syncAuditRepository.findAll();
        List<SyncAuditLog> logs = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 4l);
        then(logs).isNotEmpty();
        then(logs.size()).isEqualTo(1l);
    }
}


