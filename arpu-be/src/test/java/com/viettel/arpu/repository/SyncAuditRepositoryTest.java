package com.viettel.arpu.repository;

import com.viettel.arpu.model.entity.SyncAuditLog;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author tuongvx
 * @created 24/06/2020 - 5:47 PM
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
public class SyncAuditRepositoryTest {

    @Autowired
    SyncAuditRepository syncAuditRepository;
    @Autowired
    CodeCodeRepository codeCodeRepository;


    @Test
    public void testFindAllByVersionId() {
        List<SyncAuditLog> lst = syncAuditRepository.findAllByVersionId(3l);
        then(lst).isEmpty();
    }

    @Test
    public void testFindAllByVersionIdAndAndRecordNumber() {
        List<SyncAuditLog> lst = syncAuditRepository.findAllByVersionIdAndAndRecordNumber(3l, 1l);
        then(lst).isEmpty();
    }

}
