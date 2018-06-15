package com.whaley.core.debug.logger;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class WriteHandler extends Handler {

    private final String folder;
    private final String fileName;
//    private final int maxFileSize;

    WriteHandler(Looper looper, String folder) {
        super(looper);
        this.folder = folder;
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.UK);
        this.fileName = format.format(new Date())+"_log.txt";
//        this.maxFileSize = maxFileSize;
    }

    @SuppressWarnings("checkstyle:emptyblock")
    @Override
    public void handleMessage(Message msg) {
        String content = (String) msg.obj;
        content = content.replace("<br>","\n");
        FileWriter fileWriter = null;
        File logFile = getLogFile(folder, fileName);
        try {
            fileWriter = new FileWriter(logFile, true);
            writeLog(fileWriter, content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            if (fileWriter != null) {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e1) { /* fail silently */ }
            }
        }
    }

    /**
     * This is always called on a single background thread.
     * Implementing classes must ONLY write to the fileWriter and nothing more.
     * The abstract class takes care of everything else including close the stream and catching IOException
     *
     * @param fileWriter an instance of FileWriter already initialised to the correct file
     */
    private void writeLog(FileWriter fileWriter, String content) throws IOException {
        fileWriter.append(content);
    }

    private File getLogFile(String folderName, String fileName) {

        File folder = new File(folderName);
        if (!folder.exists()) {
            //TODO: What if folder is not created, what happens then?
            folder.mkdirs();
        }
        return new File(folder,fileName);
//        int newFileCount = 0;
//        File newFile;
//        File existingFile = null;

//        newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
//        while (newFile.exists()) {
//            existingFile = newFile;
//            newFileCount++;
//            newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
//        }
//
//        if (existingFile != null) {
//            if (existingFile.length() >= maxFileSize) {
//                return newFile;
//            }
//            return existingFile;
//        }

//        return newFile;
    }
}