/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;
import com.viettel.arpu.config.FtpStorageProperties;
import com.viettel.arpu.config.SftpInfo;
import com.viettel.arpu.exception.FileInvalidException;
import com.viettel.arpu.exception.FtpException;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public interface FtpUtils {
    /**
     * Returns file in bytes from FTP server.
     */
    static byte[] fileInBytesFromFtp(final SftpInfo sftpInfo) {
        Session session = null;
        Channel channel = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sftpInfo.getUsername(), sftpInfo.getServerAddress(), sftpInfo.getPort());
            session.setUserInfo(new UserInfo() {
                public String getPassphrase() {
                    return null;
                }

                public String getPassword() {
                    return null;
                }

                public boolean promptPassphrase(String string) {
                    return false;
                }

                public boolean promptPassword(String string) {
                    return false;
                }

                public boolean promptYesNo(String string) {
                    return true;
                }

                public void showMessage(String string) {
                    //Do nothing
                }
            });
            session.setPassword(sftpInfo.getPassword());
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            InputStream stream = sftpChannel.get(sftpInfo.getPath2File());
            return IOUtils.toByteArray(stream);
        } catch (JSchException | SftpException | IOException e) {
            throw new FtpException(e);
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

    static String uploadFile(FtpStorageProperties ftpStorageProperties, MultipartFile file) throws IOException {
        final String fileFormat = ".pdf";
        Optional.ofNullable(file.getOriginalFilename())
                .ifPresent((String fn) -> {
                    boolean check = FileUtils.checkFileFormat(fn, fileFormat);
                    if (!check) {
                        throw new FileInvalidException();
                    }
                });

        String fileName = System.currentTimeMillis() + fileFormat;
        FtpUtils.putFile(ftpStorageProperties, file.getInputStream(), fileName);
        return fileName;
    }

    /**
     * Nhận vào thông tin server sftp và file
     * Check file xem đúng định dạng và là file hợp lệ hay không, nếu không đúng thì trả ra thông báo lỗi.
     * Sau đó thực hiện kết nối tới server và upload file lên server sftp
     * trả về tên file chính là thời gian upload để tránh việc bị trùng tên file dẫn đến mất dữ liệu
     *
     * @param sftpInfo
     * @param uploadStream
     * @param fileName
     */
    static void putFile(final FtpStorageProperties sftpInfo, InputStream uploadStream, String fileName) {
        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession(sftpInfo.getUser(), sftpInfo.getHost(),
                                              Integer.parseInt(sftpInfo.getPort()));
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(sftpInfo.getPwd());
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.put(uploadStream, sftpInfo.getUploadDir() + fileName);
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException | SftpException e) {
            throw new FtpException(e);
        }
    }
}
