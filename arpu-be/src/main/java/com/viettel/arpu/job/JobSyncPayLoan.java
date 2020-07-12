/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.arpu.job;

import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.model.entity.JobConfig;
import com.viettel.arpu.repository.JobConfigRepository;
import com.viettel.arpu.service.sftp.manager.PaidProcessor;
import com.viettel.arpu.service.sftp.manager.SFTPManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Job thực hiện chạy đồng bộ danh sách tất toán khoản vay
 *
 * @author tuongvx
 */
@Component
@Slf4j
public class JobSyncPayLoan {
    private static final String CRON_DEFAULT = "0 0 0 * * *";
    private JobConfigRepository jobConfigRepository;
    private PaidProcessor paidProcess;

    @Autowired
    public JobSyncPayLoan(JobConfigRepository jobConfigRepository, PaidProcessor paidProcess) {
        this.jobConfigRepository = jobConfigRepository;
        this.paidProcess = paidProcess;
    }

    /**
     * Method thực hiện chạy job đồng bộ tất toán khoản vay
     *
     * @throws Exception
     */
    @Scheduled(cron = "#{@getCronPayLoanValue}")
    public void syncPaidLoan() throws Exception {
        SFTPManager sftpManager = new SFTPManager().withPaidProcess(paidProcess).build();
        sftpManager.readBy(SFTPManager.Tasks.PAID).process();
    }

    /**
     * Bean lấy ra cronconfig để chạy job
     * Trả về cron theo batch, nếu không sẽ trả về cron mặc định
     *
     * @return
     */
    @Bean
    public String getCronPayLoanValue() {
        try {
            Optional<JobConfig> jobConfig = jobConfigRepository.findById(BatchConstant.PAY_LOAN_BATCH_ID);
            String st = "";
            if (!jobConfig.isPresent()) {
                st = CRON_DEFAULT;
            }
            else {
                if (Strings.isEmpty(jobConfig.get().getCron())) {
                    st = CRON_DEFAULT;
                }
                else {
                    st = jobConfig.get().getCron();
                }
            }
            return st;
        } catch (Exception ex) {
            log.error("Error when get cronValue" + ex);
            return CRON_DEFAULT;
        }
    }
}
