package com.anfly.anflylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class ActivityUtil {
	/**
	 * 隐藏软键盘
	 * 跳转 Activity的时候 若已经打开软键盘 进行关闭 :android:windowSoftInputMode="adjustUnspecified|stateHidden"
	 * Activity销毁,跳转新的Activity 将已经打开的软键盘 关闭  在onPause()方法中调用
	 * @param activity
	 */
	public static  void hindSoftInput(Activity activity) {
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive()&&activity.getCurrentFocus()!=null){
			if (activity.getCurrentFocus().getWindowToken()!=null) {
				imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	public static  void showSoftInput(Activity activity, View editText) {
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

	}


	/**
	 * 获取手机屏幕的像素宽度
	 * @return
	 */
	public static int getScreenWidthMetrics(Activity activity){
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 获取手机屏幕的像素宽度
	 * @return
	 */
	public static int getScreenHeightMetrics(Activity activity){
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}


	public static  void showDorbar(Context context, boolean isShow) {
		Intent intent= null;
		if(isShow){
			intent=new Intent("com.android.systembar.show");
			context.sendBroadcast(intent);

		}else{
			intent=new Intent("com.android.systembar.hide");
			context.sendBroadcast(intent);

//            if (context instanceof Activity)
//            hideBottomUIMenu((Activity) context);
		}
	}
}
