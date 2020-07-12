package com.viettel.arpu.sync.processor;

import com.viettel.arpu.config.FtpStorageProperties;
import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.constant.enums.Gender;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.model.entity.SyncAuditLog;
import com.viettel.arpu.repository.CustomerRepository;
import com.viettel.arpu.repository.SyncAuditRepository;
import com.viettel.arpu.repository.VersionRepository;
import com.viettel.arpu.service.sftp.SFTPService;
import com.viettel.arpu.service.sftp.manager.DataMapSFTP;
import com.viettel.arpu.service.sftp.manager.WhiteListProcessor;
import com.viettel.arpu.service.sftp.validator.ValidatorService;
import com.viettel.arpu.service.version.VersionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author tuongvx
 * @created 28/06/2020 - 9:58 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WhiteListProcessorTest {

    @Autowired
    private SFTPService sftpService;
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
    private CustomerRepository customerRepository;
    @Autowired
    private WhiteListProcessor whiteListProcessor;

    @BeforeEach
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void setup() throws Exception {
        whiteListProcessor.process();
    }

    @Test
    public void testItShouldBeSuccessCreateNewDataMapSFTPForWhiteListSuccess() throws Exception {
        DataMapSFTP dataMapSFTP = new DataMapSFTP(ftpStorageProperties, messageSource,
                sftpService, versionService).invoke(BatchConstant.WHITE_LIST);
        then(dataMapSFTP).isNotNull();
        then(dataMapSFTP.getVersion()).isNotNull();
        then(dataMapSFTP.getSftpInfoEx()).isNotNull();
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        then(dataMapSFTP.getVersion().getId()).isGreaterThanOrEqualTo(version);
        then(dataMapSFTP.getSftpInfoEx().getPath2File()).isEqualTo("/home/arpu/csv/whitelist_test.txt");
        then(dataMapSFTP.isConnectedToSFTPServer()).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void testItShouldBeSuccessCreateNewDataMapSFTPForWhiteListFailed() throws Exception {
        DataMapSFTP dataMapSFTP = new DataMapSFTP(ftpStorageProperties, messageSource,
                sftpService, versionService).invoke(BatchConstant.PAID_LOAN);
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
    public void testItShouldHasNewVersionAndNewAudit() {
        Long version = versionRepository.getLatestVersionForBatchId(1l);
        then(version).isNotNull();
        Long versionId = versionRepository.getLatestIdForBatchId(1l);
        then(versionId).isNotNull();
        List<SyncAuditLog> syncAuditLog = syncAuditRepository.findAllByVersionId(versionId);
        then(syncAuditLog).isNotNull();
        then(syncAuditLog.size()).isGreaterThan(15);
    }

    @Test
    @DisplayName("One record insert DB")
    public void testItShouldNewOneCustomerWithSexMale() {
        Optional<Customer> customer = customerRepository.findByMsisdn("84988790767");
        then(customer).isNotEmpty();
        then(customer.get().getScoreMax()).isEqualTo(715);
        then(customer.get().getViettelpayWallet()).isEqualTo("970522496594");
    }

    @Test
    @DisplayName("One record insert DB")
    public void testItShouldNewOneCustomerWithSexFeMale() {
        Optional<Customer> customer = customerRepository.findByMsisdn("84988790667");
        then(customer).isNotEmpty();
        then(customer.get().getScoreMax()).isEqualTo(715);
        then(customer.get().getViettelpayWallet()).isEqualTo("970522496594");
        then(customer.get().getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    @DisplayName("it should be fail with cause : phone")
    public void testPhoneFailedValid() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 1l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("it should be fail with cause : Full Name")
    public void testFullNameFailedValid() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 2l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("it should be fail with cause : dateOfBirth")
    public void testDateOfBirthFailedValid() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 3l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }

    @Test

    @DisplayName("it should be fail with cause : gender")
    public void testGenderFailedValid() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 4l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("it should be fail with cause : identity")
    public void testIdentityFailedValid() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 5l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }

    @Test

    @DisplayName("it should be fail with cause : identityNumber")
    public void testIdentityNumberFailedValid() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 6l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("it should be fail with cause : dateOfIssue")
    public void testDateOfIssueFailedValid() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 7l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("it should be fail with cause : place")
    public void testPlaceIssueFailedValid() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 8l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("it should be fail with cause : viettelID")
    public void testViettelIDFailedValid() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 9l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }

    @Test

    @DisplayName("it should be fail with cause : arpuThreeMonth")
    public void testArpuThreeMonthFailedValid() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 10l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("it should be fail with cause : scoreMin")
    public void testScoreMinFailedValid() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 11l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("it should be fail with cause : scoreMax")
    public void testScoreMaxFailedValid() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 12l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("it should be fail with cause : scoreMax lessthan ScoreMin")
    public void testScoreMinGreaterThanScoreMaxFailedValid() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 13l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("it should be fail with cause : record size lessThan min size")
    public void testRecordLessThanSizeMin() {
        Long version = versionRepository.getLatestIdForBatchId(1l);
        then(version).isNotNull();
        List<SyncAuditLog> cus = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(version, 15l);
        then(cus).isNotEmpty();
        then(cus.size()).isEqualTo(1);
    }
}
