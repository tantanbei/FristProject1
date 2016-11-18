package com.whoplate.paipable.util;

import android.database.sqlite.SQLiteStatement;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

public class Closer {

    public static void Close(FileOutputStream c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                XDebug.Handle(e);
            }
        }
    }

    //add this due to API < 19 has no closeable
    public static void Close(DataOutputStream c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                XDebug.Handle(e);
            }
        }
    }

    //add this due to API < 19 has no closeable
    public static void Close(DataInputStream c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                XDebug.Handle(e);
            }
        }
    }

    //add this due to API < 19 has no closeable
    public static void Close(Socket c) {
        if (c != null) {
            try {
                if (!c.isClosed()) {
                    c.close();
                }
            } catch (IOException e) {
                XDebug.Handle(e);
            }
        }
    }

    //add this due to API < 19 has no closeable
    public static void Close(android.database.Cursor c) {
        if (c != null) {
            try {
                if (!c.isClosed()) {
                    c.close();
                }
            } catch (Exception e) {
                XDebug.Handle(e);
            }
        }
    }

    public static void Close(SQLiteStatement c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {
                XDebug.Handle(e);
            }
        }
    }

    //add this due to API < 19 has no closeable
    public static void Close(DatagramSocket c) {
        if (c != null) {
            try {
                if (!c.isClosed()) {
                    c.close();
                }
            } catch (Exception e) {
                XDebug.Handle(e);
            }
        }
    }

    //only over  api 19  the socket implement closeAble
    public static void Close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                XDebug.Handle(e);
            }
        }
    }

}

