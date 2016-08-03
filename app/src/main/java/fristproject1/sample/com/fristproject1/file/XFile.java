package fristproject1.sample.com.fristproject1.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XFile {
    static public byte[] GetFile(final String path){

        InputStream inputStream = null;
        int length;

        final File f = new File(path);
        if (!f.exists()) {
            return null;
        }

        try {
            inputStream = new FileInputStream(f);
            length = inputStream.available();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final byte[] bytes = new byte[length];

        try {
            inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }
}
