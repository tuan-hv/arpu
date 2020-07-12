/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.utils;

public interface FileUtils {
    static boolean checkFileFormat(String file, String format) {
        String contractName = "";
        for (int i = file.length() - 1; i >= 0; i--) {
            if (file.charAt(i) == '.') {
                contractName = file.substring(i);
                break;
            }
        }
        return contractName.toLowerCase().contains(format.toLowerCase());
    }
}
