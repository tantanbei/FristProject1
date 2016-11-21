package com.whoplate.paipable.file;

import com.whoplate.paipable.util.XDebug;

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
            XDebug.Handle(e);
            try {
                inputStream.close();
            } catch (Exception ee) {
                XDebug.Handle(e);
            }
            return null;
        }

        final byte[] bytes = new byte[length];

        try {
            inputStream.read(bytes);
        } catch (IOException e) {
            XDebug.Handle(e);
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                XDebug.Handle(e);
            }
        }

        return bytes;
    }
}
