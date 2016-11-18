package com.whoplate.paipable.http;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.SecureRandom;

public class XRandom {
    //random generatof ror all to use..
    private static SecureRandom SECURE_RANDOM;

    static synchronized public SecureRandom Get() {
        if (SECURE_RANDOM != null) {
            return SECURE_RANDOM;
        }

        try {
            InputStream is = new FileInputStream("/dev/urandom");
            byte[] b = new byte[32];
            is.read(b);

            is.close();

            //set a 256bit random seed
            SECURE_RANDOM = new SecureRandom(b);

            return SECURE_RANDOM;
        } catch (Exception e) {
            throw new RuntimeException("cannot open urandom...what the heck?");
        }
    }

    public static Float GetFloat() {
        return Get().nextFloat();
    }

    public static int GetInt() {
        return Get().nextInt();
    }

    public static void GetBytes(byte[] b) {
        Get().nextBytes(b);
    }
}

