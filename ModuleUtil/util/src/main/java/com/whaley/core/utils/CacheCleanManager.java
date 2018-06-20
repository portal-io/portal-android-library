package com.whaley.core.utils;

import android.content.Context;
import android.os.Environment;

import com.orhanobut.logger.Logger;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.AppFileStorage;

import java.io.File;

public class CacheCleanManager {

	public static void cleanInternalCache(Context context) {
		deleteFile(context.getCacheDir());
	}

	public static void cleanDatabases(Context context) {
		deleteFile(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}

	public static void cleanSharedPreference(Context context) {
		deleteFile(new File("/data/data/"
				+ context.getPackageName() + "/shared_prefs"));
	}

	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFile(context.getExternalCacheDir());
		}
	}

	public static void cleanAppCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFile(new File(AppFileStorage.getAppDirPath()));
		}
	}

	public static void cleanApplicationData(Context context) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanAppCache(context);

	}

	public static String getTotalCacheSize(Context context) throws Exception {
		long cacheSize = getFolderSize(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cacheSize += getFolderSize(context.getExternalCacheDir());
			cacheSize += getFolderSize(new File(AppFileStorage.getAppDirPath()));
		}
		return FileUtils.formatFileSize(cacheSize);
	}

	private static void deleteFile(File file) {
        if(file == null){
            return;
        }
		if (file.isFile()) {
			file.delete();
			return;
		}
		if(file.isDirectory()){
			File[] childFiles = file.listFiles();
			for (int i = 0; i < childFiles.length; i++) {
				deleteFile(childFiles[i]);
			}
		}
	}
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			Logger.e(e, "getFolderSize");
		}
		return size;
	}
}