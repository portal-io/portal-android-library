package com.whaley.core.uiframe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Router;

/**
 * Created by YangZhi on 2017/7/10 18:37.
 */

public class ContainerActivity extends BaseActivity {

    protected static final String STR_FRAGMENT_PATH = "fragmentName";

    protected static final String STR_FRAGMENT_CLASS_NAME = "fragmentClassName";

    protected Fragment fragment;

    private String fragmentTag;

    protected BaseFragment baseFragment;

    private String fragmentPath;

    private String fragmentClassName;

    public static void goPage(Starter starter, int requestCode, Class fragmentClazz) {
        Intent intent = createIntent(starter, fragmentClazz.getName(), null);
        goPage(starter, requestCode, intent);
    }

    public static void goPage(Starter starter, int requestCode, String fragmentPath) {
        Intent intent = createIntent(starter, null, fragmentPath);
        goPage(starter, requestCode, intent);
    }

    public static void goPage(Starter starter, int requesCode, Intent intent) {
        starter.startActivityForResult(intent, requesCode);
    }

    public static Intent createIntent(Starter starter, String fragmentClazzName, String fragmentClassPath) {
        Intent intent = new Intent(starter.getAttatchContext(), ContainerActivity.class);
        intent.putExtra(STR_FRAGMENT_CLASS_NAME, fragmentClazzName);
        intent.putExtra(STR_FRAGMENT_PATH, fragmentClassPath);
        return intent;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        fragmentPath = getIntent().getStringExtra(STR_FRAGMENT_PATH);
        fragmentClassName = getIntent().getStringExtra(STR_FRAGMENT_CLASS_NAME);
        if (savedInstanceState == null) {
            addFragment();
        } else {
            if (!TextUtils.isEmpty(fragmentPath)) {
                fragment = getSupportFragmentManager().findFragmentByTag(fragmentPath);
            }
            if (fragment == null && !TextUtils.isEmpty(fragmentClassName)) {
                fragment = getSupportFragmentManager().findFragmentByTag(fragmentClassName);
            }
            if (fragment != null && fragment instanceof BaseFragment) {
                baseFragment = (BaseFragment) fragment;
            }
        }
    }

    protected Class getFragmentClazz() {
        return null;
    }


    protected void addFragment() {
        initSingleFragment();
        if (fragment == null) {
            logInstanceError();
        } else {
            if (fragment instanceof BaseFragment) {
                baseFragment = (BaseFragment) fragment;
            }
            fragment.setArguments(getIntent().getExtras());

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded())
                ft.remove(fragment);
            ft.add(getFragmentContentID(), fragment, fragmentTag)
                    .commitAllowingStateLoss();
        }
    }

    protected void initSingleFragment() {

        if (!TextUtils.isEmpty(fragmentPath)) {
            fragment = routeFragmentInstance(fragmentPath);
            if (fragment != null) {
                fragmentTag = fragmentPath;
            }

        }
        if (fragment == null && !TextUtils.isEmpty(fragmentClassName)) {
            fragment = createFragmentInstance(fragmentClassName);
            if (fragment != null) {
                fragmentTag = fragmentClassName;
            }
        }

        if (fragment == null) {
            Class clazz = getFragmentClazz();
            if (clazz != null)
                fragmentClassName = clazz.getName();
            if (!TextUtils.isEmpty(fragmentClassName)) {
                fragment = createFragmentInstance(fragmentClassName);
            }
            if (fragment != null) {
                fragmentTag = fragmentClassName;
            }
        }
    }

    protected int getFragmentContentID() {
        return Window.ID_ANDROID_CONTENT;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (baseFragment != null)
            baseFragment.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (baseFragment == null || !baseFragment.onBackPressed()) {
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (baseFragment != null && baseFragment.onTouchEvent(event))
            return true;
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (baseFragment != null && baseFragment.dispatchTouchEvent(ev))
            return true;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (baseFragment != null && baseFragment.onKeyDown(keyCode, event))
            return true;
        return super.onKeyDown(keyCode, event);
    }

    private void logInstanceError() {
        Log.e("ContainerActivity 创建fragment实例失败,fragmentClassName=" + fragmentClassName + ",fragmentPath=" + fragmentPath);
    }

    private Fragment routeFragmentInstance(String fragmentPath) {
        return Router.getInstance().buildObj(fragmentPath).getObj();
    }

    private Fragment createFragmentInstance(String fragmentClassName) {
        try {
            Fragment fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
            return fragment;
        } catch (ClassNotFoundException e) {
            Log.e(e, TAG + "createFragmentInstance");
        } catch (InstantiationException e) {
            Log.e(e, TAG + "createFragmentInstance");
        } catch (IllegalAccessException e) {
            Log.e(e, TAG + "createFragmentInstance");
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (baseFragment != null) {
            baseFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (baseFragment != null) {
            baseFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }
}
