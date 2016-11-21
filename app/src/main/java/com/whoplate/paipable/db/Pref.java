package com.whoplate.paipable.db;

import com.bluelinelabs.logansquare.LoganSquare;
import com.whoplate.paipable.App;
import com.whoplate.paipable.file.XFile;
import com.whoplate.paipable.string.XString;
import com.whoplate.paipable.thread.XThread;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import okio.BufferedSink;
import okio.Okio;

//a file backed json storage just for preference..
public class Pref {
    public static String USERID = "USERID";
    public static String USERNAME = "USERNAME";
    public static String USERPHONE = "USERPHONE";

    //path of pref file...each system might have diff path
    private static String path;

    //pref map data//USE Pref class to access this.
    private static PrefPacket PREF_DATA = null;

    //protect against wasteful multiple chained writes that are very close to each other...
    private final static AtomicBoolean PENDING_WRITE = new AtomicBoolean(false);

    private static synchronized void init() {

        if (path == null) {
            if (XString.IsEmpty(App.AppDbDirectory)) {
                App.AppDbDirectory = App.INSTANCE.getFilesDir().getParentFile().getPath() + "/db/";
            }

            //make sure AppDbDirectory exists
            File file = new File(App.AppDbDirectory);
            if (!file.exists()) {
                file.mkdirs();
            }

            path = App.AppDbDirectory + "pref.db";
        }
    }

    //write encrypted json map[string]string to file
    public static synchronized void Save() {
        //make sure we don't do multiple writes if another is already pending...
        if (!PENDING_WRITE.get()) {
            //pending write...
            PENDING_WRITE.set(true);
            XThread.RunBackgroundIfNotBackground(new Runnable() {
                @Override
                public void run() {
                    writeDataToDisk();
                }
            });
        }
    }

    //only allow 1 write operations for all threads
    private static synchronized void writeDataToDisk() {
        init();

        final File f = new File(path);

        BufferedSink out = null;

        try {
            //file outputstream will automatically create file if it doesn't exists already
            out = Okio.buffer(Okio.sink(f));

            //out.write(Http2.Encrypt(encKey, com.alibaba.fastjson.JSON.toJSONBytes(PREF_DATA, SerializerFeature.WriteClassName)));
            final byte[] outBytes = LoganSquare.serialize(PREF_DATA).getBytes();

            //we need to set false here to protect again map modify while we are performing io....
            //pending write complete
            PENDING_WRITE.set(false);

            out.write(outBytes);
            out.flush();
        } catch (Exception e) {
            XDebug.Handle(e);
        } finally {
            //close file..
            try {
                out.close();
            } catch (IOException e) {
                XDebug.Handle(e);
            }

            //pending write complete
            PENDING_WRITE.set(false);
        }
    }

    public static synchronized void DeleteAll() {
        init();

        //init a temp empty one...
        PREF_DATA = new PrefPacket();

        new File(path).delete();
    }

    //load encrypted json and decode to map[string]string
    public static synchronized void LoadFromDisk(final boolean force) {
        init();

        //only load if not loaded before
        if (PREF_DATA != null && !force) {
            return;
        }


        final byte[] bytes = XFile.GetFile(path);

        if (bytes == null || bytes.length == 0) {
            //delete file
            DeleteAll();
            return;
        }

        try {
            PREF_DATA = LoganSquare.parse(new ByteArrayInputStream(bytes), PrefPacket.class);

        } catch (Exception e) {
            XDebug.Handle(e);

            //need to remove json file if corrupted...JsonPrefModel
            DeleteAll();
        }

        if (PREF_DATA == null || PREF_DATA.Data == null) {
            PREF_DATA = new PrefPacket();
        }
    }

    //get int type with default
    public static synchronized int Get(final String key, final int def) {
        final String r = Get(key, null);

        try {
            if (r != null) {
                return Integer.parseInt(r);
            }
        } catch (Exception e) {
            return def;
        }

        return def;
    }

    //get int type with default
    public static synchronized ArrayList<Integer> GetListInteger(final String key, final ArrayList<Integer> def) {
        final String r = Get(key, null);

        try {
            if (r != null && r.length() > 0) {
                return (ArrayList) LoganSquare.parseList(r, Integer.class);
            }
        } catch (Exception e) {
            XDebug.Handle(e);
            return def;
        }

        return def;
    }

    //get int type with default
    public static synchronized void SetListInteger(final String key, final List<Integer> v) {
        if (v == null) {
            return;
        }

        try {
            PREF_DATA.Data.put(key, LoganSquare.serialize(v, Integer.class));
        } catch (Exception e) {
            XDebug.Handle(e);
        }
    }

    //get boolean with default
    public static synchronized boolean Get(final String key, final boolean def) {
        final String r = Get(key, null);

        if (r == null) {
            return def;
        }

        if (r.length() == 1 && r.equals("t")) {
            return true;
        } else {
            return false;
        }
    }

    //get string with default...everything is string, string so this is the real get
    public static synchronized String Get(final String key, final String def) {
        LoadFromDisk(false);

        final String value = PREF_DATA.Data.get(key);
        if (value == null || XString.IsEmpty(value)) {
            return def;
        } else {
            return value;
        }
    }

    //set string...NOTE this does not persist data to disk...must call Save() after this..
    public static synchronized void Set(final String key, final String value) {
        LoadFromDisk(false);

        if (value != null && !XString.IsEmpty(value)) {
            PREF_DATA.Data.put(key, value);
        }
    }

    public static synchronized void Set(final String key, final int value) {
        Set(key, String.valueOf(value));
    }

    public static synchronized void Set(final String key, final boolean value) {
        Set(key, value ? "t" : "f");
    }

    public static synchronized void Remove(final String key) {
        PREF_DATA.Data.remove(key);
    }
}
