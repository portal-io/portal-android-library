package com.whaley.core.uiframe.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.whaley.core.interactor.UseCase;
import com.whaley.core.interactor.UseCaseManager;
import com.whaley.core.uiframe.view.PageView;

/**
 * Created by YangZhi on 2017/7/10 17:31.
 */

public abstract class PagePresenter<PAGEVIEW extends PageView> implements Presenter<PAGEVIEW>{

    protected static String TAG;

    private final PAGEVIEW pageview;

    private final UseCaseManager useCaseManager;

    public PagePresenter(PAGEVIEW pageview){
        TAG = getClass().getSimpleName();
        this.pageview = pageview;
        this.useCaseManager = new UseCaseManager();
    }

    @Override
    public PAGEVIEW getUIView() {
        return pageview;
    }

    public void onCreate(Bundle arguments, Bundle saveInstanceState){
    }

    public void onViewCreated(Bundle arguments,Bundle saveInstanceState){
    }

    public void onViewDestroyed(){
        useCaseManager.dispose();
    }

    public void onResume(){
    }

    public void onPause(){
    }

    public void onStart(){
    }

    public void onRestart(){
    }

    public void onStop(){
    }

    public void onDestroy(){

    }

    public void onAttached(){
    }

    public void onDetached(){
    }

    public void onNewIntent(Intent intent){
    }

    public void onSaveInstanceState(Bundle outState){
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
    }

    /**
     * 当点击返回
     *
     * @return 是否处理此次返回点击事件
     */
    public boolean onBackPressed() {
        return false;
    }


    public void addUseCase(UseCase useCase){
        useCaseManager.add(useCase);
    }

    public void removeUseCase(UseCase useCase){
        useCaseManager.remove(useCase);
    }

    public void clearUseCase(){
        useCaseManager.clear();
    }
}
