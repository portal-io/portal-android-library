package com.whaley.core.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by yangzhi on 16/9/1.
 */
public class FileUtil {


//    public static File writeJsonToFile(String savePath,String json){
//        try {
//            // todo change the file location/name according to your needs
//            File futureStudioIconFile = new File(savePath);
//
//            InputStream inputStream = null;
//            OutputStream outputStream = null;
//
//            try {
//                byte[] fileReader = new byte[4096];
//
//                inputStream = new ByteArrayInputStream(json.getBytes());
//                outputStream = new FileOutputStream(futureStudioIconFile);
//
//                while (true) {
//                    int read = inputStream.read(fileReader);
//
//                    if (read == -1) {
//                        break;
//                    }
//
//                    outputStream.write(fileReader, 0, read);
//
////                    fileSizeDownloaded += read;
//
////                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
//                }
//
//                outputStream.flush();
//
//                return futureStudioIconFile;
//            } catch (IOException e) {
//                return null;
//            } finally {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//            }
//        } catch (IOException e) {
//            return null;
//        }
//    }
//
//    public static File writeResponseBodyToFile(String savePath,ResponseBody body) {
//        try {
//            // todo change the file location/name according to your needs
//            File futureStudioIconFile = new File(savePath);
//
//            InputStream inputStream = null;
//            OutputStream outputStream = null;
//
//            try {
//                byte[] fileReader = new byte[4096];
//
//                long fileSize = body.contentLength();
//                long fileSizeDownloaded = 0;
//
//                inputStream = body.byteStream();
//                outputStream = new FileOutputStream(futureStudioIconFile);
//
//                while (true) {
//                    int read = inputStream.read(fileReader);
//
//                    if (read == -1) {
//                        break;
//                    }
//
//                    outputStream.write(fileReader, 0, read);
//
//                    fileSizeDownloaded += read;
//
////                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
//                }
//
//                outputStream.flush();
//
//                return futureStudioIconFile;
//            } catch (IOException e) {
//                return null;
//            } finally {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//            }
//        } catch (IOException e) {
//            return null;
//        }
//    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) {
        long size = 0;
        try {
            size = 0;
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            }
        } catch (IOException e) {
            Logger.e(e, "getFileSize");
        }
        return size;
    }

    public static void saveBitmap(Bitmap bitamp, String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        long size = getBitmapsize(bitamp);
        int quality = 90;
        if(size > 1024*1024){
            quality = (int)(100*1024*1024*1.0f/size);
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitamp.compress(Bitmap.CompressFormat.PNG, quality, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            Logger.e(e, "saveBitmap");
        } catch (IOException e) {
            Logger.e(e, "saveBitmap");
        }

    }

    public static long getBitmapsize(Bitmap bitmap){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();

    }

    public static Bitmap fetchBitmapFromPath(String path){
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        long size = FileUtil.getFileSize(new File(path));
        final long miniSize = 262144;
        if(size > miniSize){
            bitmapOptions.inSampleSize = (int)(size / miniSize);
        } else {
            bitmapOptions.inSampleSize = 1;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path,
                bitmapOptions);
        return bitmap;
    }
}
