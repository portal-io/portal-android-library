package com.whaley.core.image;

import android.view.View;
import android.widget.ImageView;


import com.orhanobut.logger.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangzhi on 16/8/3.
 */
public class ImageLoader {

    static boolean isLoadNetwork;

    private static ConcurrentHashMap<Object, ConcurrentHashMap<ImageView, ImageRequest>> requestsMap = new ConcurrentHashMap<>();

    private static Object lock = new Object();

    public static ImageRequest.RequestManager with(Object tag) {
        ImageRequest.RequestManager requestManager = new ImageRequest.RequestManager();
        requestManager.tag(tag);
        return requestManager;
    }

    public static long lastTime;

    public static void LogTime(String tag) {
        long time = System.currentTimeMillis();
        Logger.v("ImageLoader", "loadImage time " + tag + (time - lastTime));
        lastTime = time;
    }

    public static void loadImage(final ImageRequest request) {
        try {
            GlideImageLoader.loadImage(request);
//            addRequest(request);
        } catch (Exception e) {
            Logger.e(e, "loadImage");
        }
    }


//    private static void addRequest(ImageRequest request) {
////        synchronized (lock) {
////            Object tag = request.getTag();
////            if (tag != null && request.getImageView() != null) {
////                ConcurrentHashMap<ImageView, ImageRequest> requests = requestsMap.get(tag);
////                if (requests != null) {
////                    requests.put(request.getImageView(), request);
////                }
////            }
////        }
//    }
//
//
//    public static void onCreate(Object tag) {
////        synchronized (lock) {
////            ConcurrentHashMap<ImageView, ImageRequest> requests = requestsMap.get(tag);
////            if (requests == null) {
////                requests = new ConcurrentHashMap<>();
////                requestsMap.put(tag, requests);
////            }
////        }
//    }
//
//    public static void onPause(Object tag) {
////        synchronized (lock){
////            ConcurrentHashMap<ImageView, ImageRequest> requests = requestsMap.get(tag);
////            if(requests!=null&&requests.size()>0) {
////                for (ImageView imageView : requests.keySet()) {
////                    imageView.setImageResource(0);
////                }
////            }
////        }
//    }
//
//    public static void onResume(Object tag) {
////        synchronized (lock){
////            ConcurrentHashMap<ImageView, ImageRequest> requests = requestsMap.get(tag);
////            if(requests!=null&&requests.size()>0) {
////                for (ImageView imageView : requests.keySet()) {
////                    ImageRequest imageRequest=requests.get(imageView);
////                    loadImage(imageRequest);
////                }
////            }
////        }
//    }
//
//
//    public static void onDestroy(Object tag) {
////        synchronized (lock){
////            ConcurrentHashMap<ImageView, ImageRequest> requests = requestsMap.get(tag);
////            if (requests!=null)
////                requests.clear();
////            requestsMap.remove(tag);
////        }
//    }


    public static void clearView(View view) {
        GlideImageLoader.clearView(view);
    }


    public static void onHideChanged(Object tag, boolean isHide) {
        GlideImageLoader.onHideChanged(tag, isHide);
    }


    public static void pauseRequests(Object tag) {
        GlideImageLoader.pauseRequests(tag);
    }

    public static void resumeRequests(Object tag) {
        GlideImageLoader.resumeRequests(tag);
    }


    public static boolean isPause(Object tag) {
        return GlideImageLoader.isPause(tag);
    }


    public static void cancelRequests(Object tag) {
//        GlideImageLoader.cancelRequests(tag);
    }


    public static void onLowMemory() {
        GlideImageLoader.onLowMemory();
    }

    ;

    public static void onTrimMemory(int level) {
        GlideImageLoader.onTrimMemory(level);
    }

}
