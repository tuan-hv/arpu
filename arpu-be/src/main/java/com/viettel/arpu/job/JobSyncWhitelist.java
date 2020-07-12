/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.job;

import com.viettel.arpu.constant.BatchConstant;
import com.viettel.arpu.model.entity.JobConfig;
import com.viettel.arpu.repository.JobConfigRepository;
import com.viettel.arpu.service.sftp.manager.SFTPManager;
import com.viettel.arpu.service.sftp.manager.WhiteListProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Job thực hiện chạy đồng bộ danh sách whitelist
 *
 * @author tuongvx
 */
@Component
@Slf4j
public class JobSyncWhitelist {
    private static final String CRON_DEFAULT = "0 0 0 * * *";
    private JobConfigRepository jobConfigRepository;
    private WhiteListProcessor whiteListProcess;

    @Autowired
    public JobSyncWhitelist(JobConfigRepository jobConfigRepository, WhiteListProcessor whiteListProcess) {
        this.jobConfigRepository = jobConfigRepository;
        this.whiteListProcess = whiteListProcess;
    }

    /**
     * Method thực hiện chạy job đồng bộ whitelist
     *
     * @throws Exception
     */

    @Scheduled(cron = "#{@getCronWhitelistValue}")
    public void syncWhiteList() throws Exception {
        SFTPManager sftpManager = new SFTPManager().withWhiteListProcess(whiteListProcess).build();
        sftpManager.readBy(SFTPManager.Tasks.WHITE_LIST).process();
    }

    /**
     * Bean lấy ra cronconfig để chạy job
     * Trả về cron theo batch, nếu không sẽ trả về cron mặc định
     *
     * @return
     */
    @Bean
    public String getCronWhitelistValue() {
        try {
            Optional<JobConfig> jobConfig = jobConfigRepository.findById(BatchConstant.PAY_LOAN_BATCH_ID);
            String cronWhiteList = "";
            if (!jobConfig.isPresent()) {
                cronWhiteList = CRON_DEFAULT;
            }
            else {
                if (Strings.isEmpty(jobConfig.get().getCron())) {
                    cronWhiteList = CRON_DEFAULT;
                }
                else {
                    cronWhiteList = jobConfig.get().getCron();
                }
            }
            return cronWhiteList;
        } catch (Exception ex) {
            log.error("Error when get cronValue: " + ex);
            return CRON_DEFAULT;
        }
    }

}
