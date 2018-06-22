package com.whaley.core.bi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;


import com.google.gson.JsonElement;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.bi.db.BIBean;
import com.whaley.core.bi.db.BIDatabsaeManager;
import com.whaley.core.bi.model.BILogsModel;
import com.whaley.core.bi.model.BIModle;
import com.whaley.core.bi.model.BIResponse;
import com.whaley.core.bi.model.LogInfoParam;
import com.whaley.core.bi.model.MetadataModel;
import com.whaley.core.bi.model.UserModel;
import com.whaley.core.debug.Debug;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.DebugUtil;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.MD5Util;
import com.whaley.core.utils.StrUtil;
import com.whaley.core.utils.VersionUtil;
import com.whaleyvr.core.network.http.HttpManager;
import com.whaleyvr.core.network.http.exception.EmptyDataError;
import com.whaleyvr.core.network.http.response.BaseObserver;
import com.whaleyvr.core.network.http.response.Response;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.internal.Util;

/**
 * Author: qxw
 * Date: 2016/11/2
 */

public class BILogServiceManager {

    private static final String TAG = "BILogServiceManager";
    private static final String STR_BI_TASK = "BI_task";
    private static final String MD5_KEY = "eOfNyUQr1mBSb3ijDYh3GqVSq5lJZVeKJX81Us8ZyQcFPpDWOOK6Uu5WinKgHNJv";

    private static boolean isTestBI = true;


    public boolean isTest() {
        return isTestBI;
    }

    public void setIsTest(boolean isTest) {
        this.isTestBI = isTest;
        mPreferences.edit().putBoolean(KEY_IS_TESE, isTestBI).apply();
    }

    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "WhaleyTask #" + STR_BI_TASK + "#" + mCount.getAndIncrement());
        }
    };

    static final ExecutorService BI_THREAD_EXCUTOR_SERVICE = Executors.newFixedThreadPool(3, THREAD_FACTORY);


    BIApi biApi;
    /**
     * 满足10条上传，每次上传10条
     */
    private static int uploadSize = 25;

    String metadata;
    private static BILogServiceManager mInstance = null;


    private int logId = 0;

    private boolean isUploading = false;

    SharedPreferences mPreferences;
    private static final String SP_NAME = "MID-BILOG";
    private static final String KEY_METADATA_NAME = "key_metadata_model";
    private static final String KEY_USERID = "key_userId";
    private static final String KEY_IS_TESE = "key_bi_test";
    UserModelProvider userModelProvider;

    public static synchronized BILogServiceManager getInstance() {
        if (mInstance == null) {
            mInstance = new BILogServiceManager();
            mInstance.setBiApi();
        }
        return mInstance;
    }

    private void setUploadSize(int size) {
        uploadSize = size;
    }

    private void setBiApi() {
        biApi = HttpManager.getInstance().getRetrofit(BIApi.class).create(BIApi.class);
        requestPost();
    }

    public BILogServiceManager() {
        mPreferences = AppContextProvider.getInstance().getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (Debug.isDebug())
            isTestBI = mPreferences.getBoolean(KEY_IS_TESE, false);
    }


    public void initData(String channel, UserModelProvider userModelProvider) {
        this.userModelProvider = userModelProvider;
        setMetadataModel(channel);
    }

    /**
     * 上传log.
     *
     * @param builder
     */
    public void recordLog(final LogInfoParam.Builder builder) {
        if (builder == null) {
            return;
        }
        Log.d(TAG, "saveLog start");
        Observable.just(saveLog(builder))
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            return checkIfUpload();
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        Log.d(TAG, "aBoolean =" + aBoolean + " ,isUploading =" + isUploading);
                        if (aBoolean && !isUploading) {
                            Log.d(TAG, "requestPost");
                            requestPost();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        Log.d(TAG, "saveLog main end");
    }

    /**
     * 将log插入数据库
     */

    public boolean saveLog(LogInfoParam.Builder builder) {
        int isTest = 0;
        if (Debug.isDebug()) {
            isTest = 1;
        }
        if ((isTest == 0 || isTestBI)) {
            logId += 1;
            builder.setLogId(String.valueOf(logId))
                    .setTest(String.valueOf(isTest))
                    .setHappenTime(String.valueOf(System.currentTimeMillis()));
            return BIDatabsaeManager.getInstance().insert(getMetadata(), builder.build());
        } else {
            return false;
        }
    }

    /**
     * 检查是否满足上传条件.
     */

    private boolean checkIfUpload() {
        if (isUploading) {
            return false;
        }
        long count = BIDatabsaeManager.getInstance().queryCount();
        if (count >= uploadSize) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 拼接log数据.
     */
    private UpdataModel startUploadLog() {
        try {
            final List<BIBean> list = BIDatabsaeManager.getInstance().queryAllList();
            if (list.size() <= 0) {
                return new UpdataModel();
            }
            List<BILogsModel> biLogsModels = new ArrayList<>();
            for (BIBean biBean : list) {
                LogInfoParam logInfo = GsonUtil.getGson().fromJson(biBean.getLogInfo(), LogInfoParam.class);
                BILogsModel biLogsModel = new BILogsModel();
                biLogsModel.itemid = biBean.itemid;
                biLogsModel.logInfo = logInfo;
                biLogsModel.metadata = GsonUtil.getGson().fromJson(biBean.getMetadata(), MetadataModel.class);
                biLogsModels.add(biLogsModel);
            }
            BIModle biModle = new BIModle();
            String logs = GsonUtil.getGson().toJson(biLogsModels);
            biModle.logs=biLogsModels;
            biModle.ts = System.currentTimeMillis();
            biModle.md5 = MD5Util.getMD5String(logs + biModle.ts + MD5_KEY);
            String logArray = GsonUtil.getGson().toJson(biModle);
            return new UpdataModel(logArray, list);
        } catch (Exception e) {
            Log.e(TAG, e + "startUploadLog");
            return new UpdataModel();
        }
    }

    private synchronized void requestPost() {
        isUploading = true;
        Log.d(TAG, "requestPost start");
        Observable.just(startUploadLog())
                .flatMap(new Function<UpdataModel, ObservableSource<UpdataModel>>() {
                    @Override
                    public ObservableSource<UpdataModel> apply(@NonNull final UpdataModel updataModel) throws Exception {
                        if (updataModel.isHaveLog) {
                            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), updataModel.getLogArray());
                            return requestPost(requestBody).map(new Function<BIResponse, UpdataModel>() {
                                @Override
                                public UpdataModel apply(@NonNull BIResponse biResponse) throws Exception {
                                    Log.d(TAG, biResponse == null ? "not data" : "status=" + biResponse.getStatus());
                                    if (biResponse != null && biResponse.getStatus() == 200) {
                                        return updataModel;
                                    } else {
                                        throw new Exception();
                                    }
                                }
                            });
                        } else {
                            throw new Exception();
                        }
                    }
                })
                .doOnNext(new Consumer<UpdataModel>() {
                    @Override
                    public void accept(@NonNull UpdataModel updataModel) throws Exception {
                        Log.e(TAG, "doOnNext");
                        boolean isDelete = BIDatabsaeManager.getInstance().deleteListByIds(updataModel.getList());
                        Log.e(TAG, "isDelete=" + isDelete);
                        if (Debug.isDebug()) {
                            WriteTxtFile(updataModel.getLogArray());
                            Log.e(TAG, "WriteTxtFile=" + updataModel.getLogArray());
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<UpdataModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull UpdataModel updataModel) {
                        Log.e(TAG, "onNext");
                        isUploading = false;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError");
                        isUploading = false;
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                        isUploading = false;
                    }
                });
    }


    private Observable<BIResponse> requestPost(RequestBody requestBody) {
//        return biApi.requestPost(requestBody);
        if (Debug.isDebug()) {
            return biApi.requestTestPost(requestBody);
        } else {
            return biApi.requestPost(requestBody);
        }
    }


    private void setMetadataModel(String channel) {
        String account_id = "";
        String usreId = "";
        String isRandom = "0";
        String systemName = "";
        UserModel userBean = userModelProvider.getUserModel();
        String metadata = getMetadata();
        MetadataModel metadataModel = GsonUtil.getGson().fromJson(metadata, MetadataModel.class);
        if (metadataModel == null) {
            metadataModel = new MetadataModel();
            if (userBean != null) {
                account_id = userBean.getAccount_id();
                usreId = getUsreId(userBean.getUsreId());
                systemName = userBean.getSystemName();
            }
        } else {
            if (userBean != null) {
                account_id = userBean.getAccount_id();
                usreId = getUsreId(userBean.getUsreId());
                systemName = userBean.getSystemName();
            }
            if (StrUtil.isEmpty(usreId)) {
                isRandom = metadataModel.getIsRandom();
                usreId = metadataModel.getUserId();
            }
        }
        if (StrUtil.isEmpty(usreId)) {
            usreId = AppUtil.getDeviceId();
        }
        if (StrUtil.isEmpty(usreId)) {
            isRandom = "1";
            usreId = getRandomUsreId(channel);
        }
        String sessionId = MD5Util.getMD5String(System.currentTimeMillis() + "");
        String versionCode = VersionUtil.getVersionCode() + "";
        metadataModel.setSessionId(sessionId);
        metadataModel.setAccountId(account_id);
        metadataModel.setIsRandom(isRandom);
        metadataModel.setUserId(usreId);
        metadataModel.setPromotionChannel(channel);
        metadataModel.setApkSeries(versionCode);
        metadataModel.setApkVersion(VersionUtil.getVersionName());
        metadataModel.setBuildDate(versionCode);
        metadataModel.setProductModel(android.os.Build.MODEL);
        metadataModel.setRomVersion(android.os.Build.VERSION.RELEASE);
        metadataModel.setSystemName(systemName);
        saveMetadata(GsonUtil.getGson().toJson(metadataModel));

    }

    public void saveMetadata(String metadata) {
        this.metadata = metadata;
        mPreferences.edit().putString(KEY_METADATA_NAME, metadata).apply();
    }

    public String getMetadata() {
        if (metadata != null) {
            return metadata;
        } else {
            return mPreferences.getString(KEY_METADATA_NAME, null);
        }
    }


    public static void WriteTxtFile(String strcontent) {
//        //每次写入时，都换行写
        RandomAccessFile raf = null;
        try {
            String path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/whaleyvr/download";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            File f = new File(path + "/birz" + System.currentTimeMillis() + ".txt");
            if (!f.exists()) {
//                Log.d("TestFile", "Create the file:" + strFilePath);
                f.createNewFile();
            }
            raf = new RandomAccessFile(f, "rw");
            raf.seek(f.length());
            raf.write(strcontent.getBytes());
        } catch (Exception e) {
            Log.e("WriteTxtFile", "WriteTxtFile Error on write File.");
        } finally {
            try {
                raf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @author qxw
     * 更新Metadata
     * @time 2017/2/17 14:28
     */
    public void updataMetadata() {
        UserModel userBean = userModelProvider.getUserModel();
        String metadata = getMetadata();
        MetadataModel metadataModel = GsonUtil.getGson().fromJson(metadata, MetadataModel.class);
        metadataModel.setAccountId(userBean.getAccount_id());
        String userId = getUsreId(userBean.getUsreId());
        if (StrUtil.isEmpty(userId)) {
            userId = AppUtil.getDeviceId();
        }
        if ("1".equals(metadataModel.getIsRandom())) {
            metadataModel.setIsRandom("0");
            metadataModel.setUserId(userId);
        } else {
            metadataModel.setUserId(userId);
        }
        if (StrUtil.isEmpty(metadataModel.getUserId())) {
            metadataModel.setIsRandom("1");
            metadataModel.setUserId(getRandomUsreId(metadataModel.getPromotionChannel()));
        }
        saveMetadata(GsonUtil.getGson().toJson(metadataModel));
    }

    public String getUsreId(String usreId) {
        String temp[] = null;
        try {
            temp = usreId.split("_");
            return temp[0];
        } catch (Exception e) {

        }
        return usreId;
    }

    private String getRandomUsreId(String channel) {
        String usreId = mPreferences.getString(KEY_USERID, "");
        if (StrUtil.isEmpty(usreId)) {
            usreId = MD5Util.getMD5String(System.currentTimeMillis() + VersionUtil.getVersionCode() + channel + android.os.Build.MODEL + android.os.Build.VERSION.RELEASE);
            mPreferences.edit().putString(KEY_USERID, usreId).apply();
        }
        return usreId;
    }

    static class ErrorReturnFunction implements Function<Throwable, String> {
        @Override
        public String apply(@NonNull Throwable throwable) throws Exception {
            return "";
        }
    }

    static class UpdataModel {
        String logArray;
        List<BIBean> list;
        boolean isHaveLog;

        public UpdataModel() {
        }

        public UpdataModel(String logArray, List<BIBean> list) {
            this.logArray = logArray;
            this.list = list;
            isHaveLog = true;
        }

        public String getLogArray() {
            return logArray;
        }

        public void setLogArray(String logArray) {
            this.logArray = logArray;
        }

        public List<BIBean> getList() {
            return list;
        }

        public void setList(List<BIBean> list) {
            this.list = list;
        }
    }

}

