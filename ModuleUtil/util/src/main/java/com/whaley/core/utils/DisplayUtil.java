package com.whaley.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;


import com.orhanobut.logger.Logger;
import com.whaley.core.appcontext.AppContextProvider;

import java.lang.reflect.Method;

/**
 * 用于转换界面尺寸的工具类
 */
public class DisplayUtil {
	public static int screenW;
	public static int screenH;


	public static int convertDIP2PX( float dip) {
		Resources resources = AppContextProvider.getInstance().getContext().getResources();
		float scale = resources.getDisplayMetrics().density;
		resources = null;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	// 转换px为dip
	public static int convertPX2DIP(float px) {
		Resources resources = AppContextProvider.getInstance().getContext().getResources();
		float scale = resources.getDisplayMetrics().density;
		resources = null;
		return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
	}




	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp( float pxValue) {
		Resources resources = AppContextProvider.getInstance().getContext().getResources();
		final float fontScale = resources.getDisplayMetrics().scaledDensity;
		resources=null;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(float spValue) {
		Resources resources = AppContextProvider.getInstance().getContext().getResources();
		final float fontScale = resources.getDisplayMetrics().scaledDensity;
		resources=null;
		return (int) (spValue * fontScale + 0.5f);
	}



	//获取屏幕原始尺寸高度，包括虚拟功能键高度
	public static int getDpi(){
		int dpi = 0;
		WindowManager windowManager = (WindowManager) AppContextProvider.getInstance().getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
			method.invoke(display, displayMetrics);
			dpi=displayMetrics.heightPixels;

			int orientation=AppContextProvider.getInstance().getContext().getResources().getConfiguration().orientation;

			Logger.e("DisplayUtil","getDpi screenW="+screenW+",width = "+displayMetrics.widthPixels+",height="+displayMetrics.heightPixels);
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				Logger.e("DisplayUtil","getDpi orientation = LANDSCAPE");
//				screenH=displayMetrics.widthPixels;
//				screenW=displayMetrics.heightPixels;
			} else {
				Logger.e("DisplayUtil","getDpi orientation = PORTRAIT");

			}
			screenH=displayMetrics.heightPixels;
			screenW=displayMetrics.widthPixels;

		}catch(Exception e){
			Logger.e(e, "getDpi");
		}
		return dpi;
	}

	/**
	 * 获取 虚拟按键的高度
	 * @return
	 */
	public static  int getBottomStatusHeight(){
		int totalHeight = getDpi();

		int contentHeight = getScreenHeight();

		return totalHeight  - contentHeight;
	}

	/**
	 * 标题栏高度
	 * @return
	 */
	public static int getTitleHeight(Activity activity){
		return  activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
	}

	/**
	 * 获得状态栏的高度
	 *
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context)
	{

		int statusHeight = -1;
		try
		{
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e)
		{
			Logger.e(e, "getStatusHeight");
		}
		return statusHeight;
	}


	/**
	 * 获得屏幕高度
	 *
	 * @return
	 */
	public static int getScreenHeight()
	{
		WindowManager wm = (WindowManager) AppContextProvider.getInstance().getContext()
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}


	//获取是否存在NavigationBar
	public static boolean checkDeviceHasNavigationBar() {
		boolean hasNavigationBar = false;
		Resources rs = AppContextProvider.getInstance().getContext().getResources();
		int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
		if (id > 0) {
			hasNavigationBar = rs.getBoolean(id);
		}
		try {
			Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
			Method m = systemPropertiesClass.getMethod("get", String.class);
			String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
			if ("1".equals(navBarOverride)) {
				hasNavigationBar = false;
			} else if ("0".equals(navBarOverride)) {
				hasNavigationBar = true;
			}
		} catch (Exception e) {

		}
		return hasNavigationBar;

	}

}
