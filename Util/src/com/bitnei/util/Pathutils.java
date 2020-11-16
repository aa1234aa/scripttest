package com.bitnei.util;

/*
@author 黄永雄
@create 2019/9/29 13:28
*/

public class Pathutils {

    public static String getProjectRootPath(String windowsProjectPath, String linuxProjectPath) {
        if (System.getProperty("os.name").contains("Windows")) {
            return windowsProjectPath;
        } else {
            return linuxProjectPath;
        }
    }
}
