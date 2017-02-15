package com.whoplate.paipable.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.string.XString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

//import okio.Segment;
//import okio.SegmentPool;

public class XFile {
    //version generator for temp files...
    private static final AtomicInteger version = new AtomicInteger(0);

    //cache system cache folder path, we need to if we want to create any type of local file
    private static final String SYSTEM_CACHE_PATH = App.INSTANCE.getCacheDir().getPath();

    private static String EXTERNAL_CACHE_PATH;

    //get a good unique value for next temp file that does not conflict with exists files..
    static public int UniqueVersionId() {
        return version.getAndAdd(1);
    }

    public static String GetInteralCachePath(String path) {
        final String fullPath = App.INSTANCE.getCacheDir() + path;

        //make sure everything exists..
        EnsureDirExists(fullPath);

        return fullPath;
    }

    public static void EnsureDirExists(String dir) {
        //make sure everything exists..
        final File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    //note this may return internal path if external path is not usable...
    public static String GetExternalCachePath(String path) {
        final String extPath = CheckExternalMediaMountedAndReturnCacheDir();

        final String fullPath = (extPath != null ? extPath : App.INSTANCE.getCacheDir()) + path;

        //make sure everything exists..
        EnsureDirExists(fullPath);

        return fullPath;
    }

    private static boolean IsExternalMediaMounted() {
        try {
            final String state = Environment.getExternalStorageState();

            return Environment.MEDIA_MOUNTED.equals(state);
        } catch (Exception e) {
            XDebug.Handle(e);
            return false;
        }
    }

    private static synchronized boolean TestPathWritable(String path) {
        try {
            //check to see if writable
            File tempFile = new File(path + "_test.txt");

            //just in case previous crashed
            tempFile.delete();

            if (!tempFile.createNewFile()) {
                return false;
            } else {
                //triple check..
                return tempFile.delete();
            }
        } catch (Exception e) {
            return false;
        }
    }

    private static String CheckExternalMediaMountedAndReturnCacheDir() {
        if (EXTERNAL_CACHE_PATH != null) {
            return EXTERNAL_CACHE_PATH;
        }

        if (!IsExternalMediaMounted()) {
            return null;
        }

        final String externalCachePath = Environment.getExternalStorageDirectory().getAbsolutePath() + Const.DOMAIN_EXT_PATH;

        File tempFile = new File(externalCachePath);

        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }

        if (TestPathWritable(externalCachePath)) {
            EXTERNAL_CACHE_PATH = externalCachePath;
            return EXTERNAL_CACHE_PATH;
        } else {
            return null;
        }
    }

    @NonNull
    public static File NewFile(String uniqueName) {
        return new File(SYSTEM_CACHE_PATH + File.separator + uniqueName);
    }

    private static long GetFreeInternalSpace() {
        long freeSpace = App.INSTANCE.getFilesDir().getUsableSpace();
        Log.d("xing", "GetFreeInternalSpace: " + freeSpace);
        return freeSpace;
    }

    public static boolean HasEnoughInternalSpace() {
        //must have about 100MB free
        return GetFreeInternalSpace() >= 100000000;
    }

    //get only the file name, excluding parent path/dirs
    public static String GetFileNameFrom(@NonNull Uri uri) {
        final String[] parts = uri.getPath().split("/");

        if (parts == null || parts.length == 0) {
            return null;
        }

        return parts[parts.length - 1];
    }

    public static String GetMimeType(@NonNull final String fileName) {
        final String extension = GetExtension(fileName);

        Log.d("xing", "extension:" + extension);

        if (XString.IsEmpty(extension)) {
            return null;
        }

        switch (extension) {
            case "jpeg":
            case "jpg":
                return "image/jpeg";
            case "webp":
                return "image/webp";
            case "gif":
                return "image/gif";
            case "png":
                return "image/png";
            case "bmp":
                return "image/bmp";
            case "tif":
            case "tiff":
                return "image/tiff";
            case "html":
            case "htm":
                return "text/html";
            case "txt":
                return "text/plain";
            default:
                break;
        }

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public static String GetExtension(@NonNull final String fileName) {
        final int dotPos = fileName.lastIndexOf('.');
        if (0 <= dotPos) {
            return fileName.substring(dotPos + 1).toLowerCase().trim();
        } else {
            return null;
        }
    }

    public static byte[] GetBytesFromUri(@NonNull final Context context, @NonNull final Uri uri) {
        InputStream inputStream;
        int length;

        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            length = inputStream.available();
        } catch (Exception e) {
            XDebug.Handle(e);
            return null;
        }

        byte[] bytes = new byte[length];

        try {
            inputStream.read(bytes);
        } catch (Exception e) {
            XDebug.Handle(e);
            return null;
        } finally {
            Closer.Close(inputStream);
        }

        return bytes;
    }

    public static byte[] GetBytesFromPath(@NonNull String path) {
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
            Closer.Close(inputStream);
            return null;
        }

        final byte[] bytes = new byte[length];

        try {
            inputStream.read(bytes);
        } catch (IOException e) {
            XDebug.Handle(e);
            return null;
        } finally {
            Closer.Close(inputStream);
        }

        return bytes;
    }

    public static int GetSizeFromUri(@NonNull Context context, @NonNull Uri uri) {
        InputStream in = null;
        try {
            in = context.getContentResolver().openInputStream(uri);
            return in.available();
        } catch (Exception e) {
            XDebug.Handle(e);
            return 0;
        } finally {
            Closer.Close(in);
        }
    }

    public static int GetSize(@NonNull String path) {
        return GetSize(new File(path));
    }

    public static int GetSize(@Nullable File f) {
        if (f == null || !f.exists()) {
            return 0;
        }

        return (int) f.length();
    }

    //rename one file to another
    public static void Rename(@NonNull final File from, @NonNull final File to) throws IOException {
        if (to.exists()) {
            to.delete();
        }

        if (!from.renameTo(to)) {
            throw new IOException("failed to rename " + from + " to " + to);
        }
    }

    public static boolean CreateEmptyFileIfNotExits(@NonNull final File f) {
        if (!f.exists()) {
            try {
                return f.createNewFile();
            } catch (Exception e) {
                XDebug.Handle(e);
                return false;
            }
        } else {
            return false;
        }
    }

    //remove directory including itself
    public static boolean DeleteDirectory(@Nullable final File dir) {
        if (dir == null || !dir.exists()) {
            return false;
        }

        DeleteDirectoryContent(dir);

        return dir.delete();
    }

    //only remove the contents of the directory...
    private static void DeleteDirectoryContent(@Nullable final File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }

        if (dir.isDirectory()) {
            final File[] children = dir.listFiles();

            //dir.list() may return null
            if (children == null || children.length == 0) {
                return;
            }

            for (File aChildren : children) {
                if (aChildren.isDirectory()) {
                    DeleteDirectory(aChildren);
                } else {
                    aChildren.delete();
                }
            }
        }
    }

    //make sure the file exists and is empty...
    public static void EnsureFileExistsAndEmpty(@NonNull File f) throws IOException {
        if (f.exists()) {
            f.delete();
        }

        f.createNewFile();
    }

    //write input stream to file
//    public static void WriteInputStream(@NonNull File file, @NonNull InputStream in) throws IOException {
//        OutputStream out = null;
//
//        //steal a segment for it's bytes..
//        final Segment buf = SegmentPool.take();
//
//        try {
//            out = new FileOutputStream(file);
//
//            int read;
//
//            while ((read = in.read(buf.data)) != -1) {
//                out.write(buf.data, 0, read);
//            }
//
//        } finally {
//            Closer.Close(out);
//            Closer.Close(in);
//
//            SegmentPool.recycle(buf);
//        }
//    }

    //write byte array to file
    public static void WriteBytes(@Nullable final File file, final byte[] bytes) throws IOException {

        if (file == null || bytes == null) {
            return;
        }

        //the init lenght of bytes array
        int length = bytes.length;

        OutputStream out = null;

        //the count to read once
        final int size = 4096;

        try {
            //override the content
            out = new FileOutputStream(file);

            //the start index to read
            int start = 0;

            //the bytes account left not wrote
            while (length > 0) {

                //the number bytes to wrote
                int writeCount = length < size ? length : size;

                //if left bytes less than 4096 , then this is the last time we write else write 4096 bytes
                out.write(bytes, start, writeCount);

                //get the left number of bytes not wrote
                length -= writeCount;

                //get the start index to write for next time
                start += writeCount;
            }
        } finally {
            Closer.Close(out);
        }
    }

    //NOTE! Exists also check for zero length file
    public static boolean Exists(@NonNull String path) {
        if (XString.IsEmpty(path)) {
            return false;
        }

        File temp = new File(path);

        return temp.exists() && temp.length() > 0;
    }

    //NOTE! Exists also check for zero length file
    public static boolean Exists(@Nullable Context context, Uri uri) {
        if (context == null || uri == null) {
            return false;
        }

        //Log.e("zjl", "uri.getPath():" + uri.getPath() + ", uri.toString():" + uri.toString());
        InputStream in = null;
        try {
            in = context.getContentResolver().openInputStream(uri);
            if (in == null || in.available() == 0) {
                return false;
            }
        } catch (Exception e) {
            XDebug.Handle(e);
            return false;
        } finally {
            Closer.Close(in);
        }

        return true;
    }

    //note CopyFile  closes Inputstream!!!
//    public static void CopyFile(@NonNull final InputStream inputStream, @NonNull final File dst) throws IOException {
//
//        CopyFile(inputStream, dst, null, null);
//    }

    //note CopyFile  closes Inputstream!!!
//    public static void CopyFile(@NonNull final InputStream inputStream, @NonNull final File dst, final byte[] additionalHead, final byte[] additionalEnd) throws IOException {
//
//        FileOutputStream outputStream = null;
//        final Segment s = SegmentPool.take();
//        final byte[] bytes = s.data;
//
//        try {
//
//            outputStream = new FileOutputStream(dst);
//
//            if (additionalHead != null) {
//                outputStream.write(additionalHead);
//            }
//
//            int read;
//            while ((read = inputStream.read(bytes)) != -1) {
//                outputStream.write(bytes, 0, read);
//            }
//
//            if (additionalEnd != null) {
//                outputStream.write(additionalEnd);
//            }
//
//            //flush to disk...close also implies flush as well..
//            outputStream.flush();
//        } catch (Exception e) {
//            XDebug.Handle(e);
//        } finally {
//            Closer.Close(inputStream);
//            Closer.Close(outputStream);
//
//            SegmentPool.recycle(s);
//        }
//
//    }

//    public static void CopyFile(@NonNull Context context, @NonNull Uri uri, @NonNull File dst) throws IOException {
//
//        InputStream inputStream = null;
//        FileOutputStream outputStream = null;
//        final Segment s = SegmentPool.take();
//        final byte[] bytes = s.data;
//
//        try {
//            inputStream = context.getContentResolver().openInputStream(uri);
//
//            outputStream = new FileOutputStream(dst);
//
//            int read;
//            while ((read = inputStream.read(bytes)) != -1) {
//                outputStream.write(bytes, 0, read);
//            }
//
//            //flush to disk...close also implies flush as well..
//            outputStream.flush();
//        } catch (Exception e) {
//            XDebug.Handle(e);
//        } finally {
//            Closer.Close(inputStream);
//            Closer.Close(outputStream);
//
//            SegmentPool.recycle(s);
//        }
//
//    }

    public static String GetRealPathFromURI(@Nullable Uri contentURI) {

        if (contentURI == null) {
            return null;
        }
        String result = null;

        Cursor cursor = App.INSTANCE.getContentResolver().query(contentURI, null, null, null, null);

        try {
            if (cursor == null) { // Source is Dropbox or other similar local file path
                return contentURI.getPath();
            } else {
                Log.d("xing", "url.toPath() + " + contentURI.getPath());

                cursor.moveToFirst();
                for (String name : cursor.getColumnNames()) {
                    Log.d("xing", "uri cursor column name:" + name);
                }

                final int idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

                if (idx >= 0) {
                    result = cursor.getString(idx);
                    Log.d("xing", "uri decoded path:" + result);
                }

                //result might be null
                return result;
            }
        } finally {
            Closer.Close(cursor);
        }
    }

    public static void CreateFile(@NonNull File f) {
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
        } catch (Throwable e) {
            XDebug.Handle(e);
        }
    }

    public static void SwapFile(@NonNull File f1, @NonNull File f2, @NonNull String swapFilePath) {
        File swapFile = new File(swapFilePath);
        f1.renameTo(swapFile);
        f2.renameTo(f1);
        swapFile.renameTo(f2);
    }

//    //read the first part of a header...limited by since of segement data size which is currently 8196
//    public static String ReadFileHeader(File f, int numberOfBytesToRead) throws IOException {
//        final Segment s = SegmentPool.take();
//        FileInputStream in = new FileInputStream(f);
//
//        //file size might be shorter than desired read...
//        final int fileSize = XFile.GetSize(f);
//
//        try {
//            //only read to numberOfBytes of fileSize which is smaller
//            final int readSize = in.read(s.data, 0, fileSize >= numberOfBytesToRead ? numberOfBytesToRead : fileSize);
//            return new String(s.data, 0, readSize);
//        } finally {
//            Closer.Close(in);
//            SegmentPool.recycle(s);
//        }
//    }

    public static void Delete(final String path) {
        if (path == null) {
            return;
        }

        Delete(new File(path));
    }

    public static void Delete(final File f) {
        f.delete();
    }
}

