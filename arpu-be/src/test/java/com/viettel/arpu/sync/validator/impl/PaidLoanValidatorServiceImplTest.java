package com.viettel.arpu.sync.validator.impl;

/**
 * @author tuongvx
 * @created 28/06/2020 - 12:58 PM
 */

import com.viettel.arpu.config.FtpStorageProperties;
import com.viettel.arpu.config.SftpInfo;
import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.model.entity.Customer;
import com.viettel.arpu.repository.LoanRepository;
import com.viettel.arpu.service.sftp.SFTPService;
import com.viettel.arpu.service.sftp.auditlog.SyncAuditLogService;
import com.viettel.arpu.service.sftp.manager.DataMapSFTP;
import com.viettel.arpu.service.sftp.manager.SFTPManager;
import com.viettel.arpu.service.sftp.validator.PaidLoanValidatorServiceImpl;
import com.viettel.arpu.service.sftp.validator.ValidatorService;
import com.viettel.arpu.service.version.VersionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaidLoanValidatorServiceImplTest {

    private SyncAuditLogService syncAuditLogService;
    private LoanRepository loanRepository;
    @Qualifier("validatorMessageSource")
    private MessageSource messageSource;
    @Autowired
    private SFTPService sftpService;
    @Autowired
    private FtpStorageProperties ftpStorageProperties;
    @Autowired
    private VersionService versionService;
    @Qualifier("syncPLValidatorServiceImpl")
    @Autowired
    ValidatorService service = new PaidLoanValidatorServiceImpl(syncAuditLogService, messageSource, loanRepository);
    private DataMapSFTP dataMapSFTP;
    private SftpInfo sftpInfoEx;


    @BeforeEach
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void setup() throws Exception {
        dataMapSFTP = new DataMapSFTP(ftpStorageProperties, messageSource,
                sftpService, versionService).invoke(BatchConstant.PAID_LOAN);
        sftpInfoEx = dataMapSFTP.getSftpInfoEx();
    }

    @Test
    public void testItShouldBeFailedReadProcessor() throws Exception {
        try {
            new SFTPManager().readBy(SFTPManager.Tasks.FAILED);
        } catch (UnsupportedOperationException ex) {
            UnsupportedOperationException exception = new UnsupportedOperationException("Not Support");
            then(ex.getMessage()).isEqualTo(exception.getMessage());
        }
    }

    @Test
    public void testItShouldBeReturnListSuccessWithSizeOne() throws Exception {
        List<String> data = sftpService.readFileFromSFTP(sftpInfoEx);
        List<String> lst = service.validatePayLoanList(data, dataMapSFTP.getVersion());
        then(lst).isNotEmpty();
    }

    @Test
    public void testItShouldBeReturnListSuccessWithEmptyByNullInputList() throws Exception {
        List<String> lst = service.validatePayLoanList(new ArrayList<>(), dataMapSFTP.getVersion());
        then(lst).isEmpty();
    }

    @Test
    public void testItShouldBeReturnListSuccessWithEmptyByNullVersion() throws Exception {
        List<String> lst = service.validatePayLoanList(new ArrayList<>(), dataMapSFTP.getVersion());
        then(lst).isEmpty();
    }

    @Test
    public void testItShouldBeReturnListWhiteListRecordIsNull() throws Exception {
        List<String> lst = service.validatePayLoanList(new ArrayList<>(), dataMapSFTP.getVersion());
        then(lst).isEmpty();
    }

    @Test
    public void testValidateWhiteListData() throws Exception {
        List<Customer> lst = service.validateWhiteListData(new ArrayList<>(), dataMapSFTP.getVersion());
        then(lst).isEmpty();
    }
}
