/**
 * Created by Walker.Zhang on 2015/4/14.
 * Revision History:
 * Date          Who              Version      What
 * 2015/4/14     Walker.Zhang     0.1.0        Created.
 */
package com.flying.util.common;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static boolean createFile(File file) throws IOException {
        if (!file.exists()) {
            makeDir(file.getParentFile());
        } else
            return false;
        return file.createNewFile();
    }

    public static boolean makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            File f = dir.getParentFile();
            makeDir(f);
        }
        return dir.mkdir();
    }
}
