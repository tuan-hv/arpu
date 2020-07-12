package com.viettel.arpu.sync.auditlog.impl;

import com.viettel.arpu.model.entity.SyncAuditLog;
import com.viettel.arpu.repository.SyncAuditRepository;
import com.viettel.arpu.service.sftp.auditlog.SyncAuditLogService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author tuongvx
 * @created 27/06/2020 - 4:42 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SyncAuditLogServiceImplTest {
    @Autowired
    SyncAuditLogService syncAuditLogService;

    @Autowired
    SyncAuditRepository syncAuditRepository;

    @Test
    public void testCreateWhiteListBatch() {
        syncAuditLogService.saveAuditLog(3l, "Loi roi", 1l, "Tuong Vu");
        Optional<SyncAuditLog> syncAuditLog = syncAuditRepository.findById(1l);
        then(syncAuditLog).isNotEmpty();
        then(syncAuditLog.get().getRecordNumber()).isEqualTo(1l);
        then(syncAuditLog.get().getRecordContent()).isEqualTo("Tuong Vu");
        then(syncAuditLog.get().getVersionId()).isEqualTo(3l);
        then(syncAuditLog.get().getReason()).isEqualTo("Loi roi");
    }
}
