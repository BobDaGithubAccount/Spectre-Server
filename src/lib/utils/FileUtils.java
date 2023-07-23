package lib.utils;

import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {
    public static byte[] readBytesFromFile(String filePath) throws IOException {
        FileInputStream fis = null;
        byte[] fileBytes = null;

        try {
            fis = new FileInputStream(filePath);
            fileBytes = new byte[fis.available()];
            fis.read(fileBytes);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        return fileBytes;
    }
}
