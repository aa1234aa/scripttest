package com.bitnei.util;

/*
@author 黄永雄
@create 2019/9/29 13:30
*/


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static void dataExport(byte[] responseData, String filePath) {
        File file1 = new File(filePath);
        File fileParent = file1.getParentFile();
        if (!file1.exists()) {
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File file = null;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            file = new File(filePath);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(responseData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
