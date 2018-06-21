package com.whaley.core.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.SparseArray;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.facade.enums.RouteType;
import com.whaley.core.appcontext.Starter;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by yangzhi on 2017/7/13.
 */

public class NavigatorRouterMeta extends RouterMeta {


    private Bundle bundle;

    private Starter starter;

    private int requestCode;

    public NavigatorRouterMeta(String path) {
        super(path);
    }

    public NavigatorRouterMeta(String path, String group) {
        super(path, group);
    }

    public NavigatorRouterMeta(Uri url) {
        super(url);
    }


    public void navigation() {
        navigation(null);
    }

    /**
     * Navigation to the route with path in postcard.
     *
     * @param context Activity and so on.
     */
    public Object navigation(Context context) {
        return navigation(context,new RouterCallback() {
            @Override
            public void onFound(RouterMeta routerMeta) {
                if(routerMeta.getType()!= RouteType.FRAGMENT)
                    return;
                Postcard postcard = routerMeta.getPostcard();
                try {
                    Field field = Postcard.class.getDeclaredField("greenChannel");
                    field.setAccessible(true);
                    field.set(postcard,false);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLost(RouterMeta routerMeta) {

            }

            @Override
            public void onArrival(RouterMeta routerMeta) {

            }

            @Override
            public void onInterrupt(RouterMeta routerMeta) {

            }
        });
    }

    /**
     * Navigation to the route with path in postcard.
     *
     * @param context Activity and so on.
     */
    public Object navigation(Context context, RouterCallback callback) {
        return getPostcard().navigation(context, TransFormUtil.tranformToNavigationCallback(callback));
    }

    /**
     * Navigation to the route with path in postcard.
     *
     * @param mContext    Activity and so on.
     * @param requestCode startActivityForResult's param
     */
    public void navigation(Activity mContext, int requestCode) {
        navigation(mContext, requestCode, null);
    }


    public NavigatorRouterMeta setStarter(Starter starter) {
        this.starter = starter;
        return this;
    }

    public Starter getStarter() {
        return starter;
    }

    public NavigatorRouterMeta setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public int getRequestCode() {
        return requestCode;
    }

    /**
     * Navigation to the route with path in postcard.
     *
     * @param mContext    Activity and so on.
     * @param requestCode startActivityForResult's param
     */
    public void navigation(Activity mContext, int requestCode, RouterCallback callback) {
        getPostcard().navigation(mContext, requestCode, TransFormUtil.tranformToNavigationCallback(callback));
    }


    public Bundle getExtras() {
        return getPostcard().getExtras();
    }

    public int getTimeout() {
        return getPostcard().getTimeout();
    }

    /**
     * Set timeout of navigation this time.
     *
     * @param timeout timeout
     * @return this
     */
    public NavigatorRouterMeta setTimeout(int timeout) {
        getPostcard().setTimeout(timeout);
        return this;
    }

    @IntDef({
            Intent.FLAG_ACTIVITY_SINGLE_TOP,
            Intent.FLAG_ACTIVITY_NEW_TASK,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION,
            Intent.FLAG_DEBUG_LOG_RESOLUTION,
            Intent.FLAG_FROM_BACKGROUND,
            Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT,
            Intent.FLAG_ACTIVITY_CLEAR_TASK,
            Intent.FLAG_ACTIVITY_CLEAR_TOP,
            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS,
            Intent.FLAG_ACTIVITY_FORWARD_RESULT,
            Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY,
            Intent.FLAG_ACTIVITY_MULTIPLE_TASK,
            Intent.FLAG_ACTIVITY_NO_ANIMATION,
            Intent.FLAG_ACTIVITY_NO_USER_ACTION,
            Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP,
            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED,
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT,
            Intent.FLAG_ACTIVITY_TASK_ON_HOME,
            Intent.FLAG_RECEIVER_REGISTERED_ONLY
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface FlagInt {
    }


    /**
     * Set special flags controlling how this intent is handled.  Most values
     * here depend on the type of component being executed by the Intent,
     * specifically the FLAG_ACTIVITY_* flags are all for use with
     * {@link Context#startActivity Context.startActivity()} and the
     * FLAG_RECEIVER_* flags are all for use with
     * {@link Context#sendBroadcast(Intent) Context.sendBroadcast()}.
     */
    public NavigatorRouterMeta withFlags(@FlagInt int flag) {
        getPostcard().withFlags(flag);
        return this;
    }

    public int getFlags() {
        return getPostcard().getFlags();
    }

    /**
     * Set object value, the value will be convert to string by 'Fastjson'
     *
     * @param key   a String, or null
     * @param value a Object, or null
     * @return current
     */
    public NavigatorRouterMeta withObject(@Nullable String key, @Nullable Object value) {
        getPostcard().withObject(key, value);
        return this;
    }

    // Follow api copy from #{Bundle}

    /**
     * Inserts a String value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a String, or null
     * @return current
     */
    public NavigatorRouterMeta withString(@Nullable String key, @Nullable String value) {
        getPostcard().withString(key, value);
        return this;
    }

    /**
     * Inserts a Boolean value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a boolean
     * @return current
     */
    public NavigatorRouterMeta withBoolean(@Nullable String key, boolean value) {
        getPostcard().withBoolean(key, value);
        return this;
    }

    /**
     * Inserts a short value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a short
     * @return current
     */
    public NavigatorRouterMeta withShort(@Nullable String key, short value) {
        getPostcard().withShort(key, value);
        return this;
    }

    /**
     * Inserts an int value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value an int
     * @return current
     */
    public NavigatorRouterMeta withInt(@Nullable String key, int value) {
        getPostcard().withInt(key, value);
        return this;
    }

    /**
     * Inserts a long value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a long
     * @return current
     */
    public NavigatorRouterMeta withLong(@Nullable String key, long value) {
        getPostcard().withLong(key, value);
        return this;
    }

    /**
     * Inserts a double value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a double
     * @return current
     */
    public NavigatorRouterMeta withDouble(@Nullable String key, double value) {
        getPostcard().withDouble(key, value);
        return this;
    }

    /**
     * Inserts a byte value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a byte
     * @return current
     */
    public NavigatorRouterMeta withByte(@Nullable String key, byte value) {
        getPostcard().withByte(key, value);
        return this;
    }

    /**
     * Inserts a char value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a char
     * @return current
     */
    public NavigatorRouterMeta withChar(@Nullable String key, char value) {
        getPostcard().withChar(key, value);
        return this;
    }

    /**
     * Inserts a float value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a float
     * @return current
     */
    public NavigatorRouterMeta withFloat(@Nullable String key, float value) {
        getPostcard().withFloat(key, value);
        return this;
    }

    /**
     * Inserts a CharSequence value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a CharSequence, or null
     * @return current
     */
    public NavigatorRouterMeta withCharSequence(@Nullable String key, @Nullable CharSequence value) {
        getPostcard().withCharSequence(key, value);
        return this;
    }

    /**
     * Inserts a Parcelable value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a Parcelable object, or null
     * @return current
     */
    public NavigatorRouterMeta withParcelable(@Nullable String key, @Nullable Parcelable value) {
        getPostcard().withParcelable(key, value);
        return this;
    }

    /**
     * Inserts an array of Parcelable values into the mapping of this Bundle,
     * replacing any existing value for the given key.  Either key or value may
     * be null.
     *
     * @param key   a String, or null
     * @param value an array of Parcelable objects, or null
     * @return current
     */
    public NavigatorRouterMeta withParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        getPostcard().withParcelableArray(key, value);
        return this;
    }

    /**
     * Inserts a List of Parcelable values into the mapping of this Bundle,
     * replacing any existing value for the given key.  Either key or value may
     * be null.
     *
     * @param key   a String, or null
     * @param value an ArrayList of Parcelable objects, or null
     * @return current
     */
    public NavigatorRouterMeta withParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
        getPostcard().withParcelableArrayList(key, value);
        return this;
    }

    /**
     * Inserts a SparceArray of Parcelable values into the mapping of this
     * Bundle, replacing any existing value for the given key.  Either key
     * or value may be null.
     *
     * @param key   a String, or null
     * @param value a SparseArray of Parcelable objects, or null
     * @return current
     */
    public NavigatorRouterMeta withSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
        getPostcard().withSparseParcelableArray(key, value);
        return this;
    }

    /**
     * Inserts an ArrayList value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value an ArrayList object, or null
     * @return current
     */
    public NavigatorRouterMeta withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        getPostcard().withIntegerArrayList(key, value);
        return this;
    }

    /**
     * Inserts an ArrayList value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value an ArrayList object, or null
     * @return current
     */
    public NavigatorRouterMeta withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        getPostcard().withStringArrayList(key, value);
        return this;
    }

    /**
     * Inserts an ArrayList value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value an ArrayList object, or null
     * @return current
     */
    public NavigatorRouterMeta withCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        getPostcard().withCharSequenceArrayList(key, value);
        return this;
    }

    /**
     * Inserts a Serializable value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a Serializable object, or null
     * @return current
     */
    public NavigatorRouterMeta withSerializable(@Nullable String key, @Nullable Serializable value) {
        getPostcard().withSerializable(key, value);
        return this;
    }

    /**
     * Inserts a byte array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a byte array object, or null
     * @return current
     */
    public NavigatorRouterMeta withByteArray(@Nullable String key, @Nullable byte[] value) {
        getPostcard().withByteArray(key, value);
        return this;
    }

    /**
     * Inserts a short array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a short array object, or null
     * @return current
     */
    public NavigatorRouterMeta withShortArray(@Nullable String key, @Nullable short[] value) {
        getPostcard().withShortArray(key, value);
        return this;
    }

    /**
     * Inserts a char array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a char array object, or null
     * @return current
     */
    public NavigatorRouterMeta withCharArray(@Nullable String key, @Nullable char[] value) {
        getPostcard().withCharArray(key, value);
        return this;
    }

    /**
     * Inserts a float array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a float array object, or null
     * @return current
     */
    public NavigatorRouterMeta withFloatArray(@Nullable String key, @Nullable float[] value) {
        getPostcard().withFloatArray(key, value);
        return this;
    }

    /**
     * Inserts a CharSequence array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a CharSequence array object, or null
     * @return current
     */
    public NavigatorRouterMeta withCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
        getPostcard().withCharSequenceArray(key, value);
        return this;
    }

    /**
     * Inserts a Bundle value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a Bundle object, or null
     * @return current
     */
    public NavigatorRouterMeta withBundle(@Nullable String key, @Nullable Bundle value) {
        getPostcard().withBundle(key, value);
        return this;
    }

    public int getEnterAnim() {
        return getPostcard().getEnterAnim();
    }

    public int getExitAnim() {
        return getPostcard().getExitAnim();
    }

    /**
     * Set normal transition anim
     *
     * @param enterAnim enter
     * @param exitAnim  exit
     * @return current
     */
    public NavigatorRouterMeta withTransition(int enterAnim, int exitAnim) {
        getPostcard().withTransition(enterAnim, exitAnim);
        return this;
    }

    /**
     * Set options compat
     *
     * @param compat compat
     * @return this
     */
    @RequiresApi(16)
    public NavigatorRouterMeta withOptionsCompat(ActivityOptionsCompat compat) {
        getPostcard().withOptionsCompat(compat);
        return this;
    }

    /**
     * Green channel, it will skip all of interceptors.
     *
     * @return this
     */
    public NavigatorRouterMeta greenChannel() {
        getPostcard().greenChannel();
        return this;
    }

    /**
     * BE ATTENTION TO THIS METHOD WAS <P>SET, NOT ADD!</P>
     */
    public NavigatorRouterMeta with(Bundle bundle) {
        getPostcard().with(bundle);
        return this;
    }

}
