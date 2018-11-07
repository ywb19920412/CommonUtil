package com.strongit.android.base.util;

import java.io.File;
import java.util.List;
import java.util.UUID;

import com.strongit.android.base.util.UserModuleUtil.Module;
import com.strongit.android.phone.oa.login.model.UserModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class AppUtils {
	/**
	 * 获取设备的IMEI码
	 * 
	 * @author yangwb
	 * @date 2017-5-16 上午10:27:33
	 * @param context
	 * @return IMEI码
	 */
	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String IMEI = "";
		if (tm.getDeviceId() != null) {
			IMEI = MD5.getIMEIStr(tm.getDeviceId());
		} else {
			IMEI = getMyUUID(context);
		}
		return IMEI;
	}

	/**
	 * 获取生成的uuid 设备随机生成的唯一识别码
	 * 
	 * @author yangwb
	 * @date 2017-5-16 上午10:27:33
	 * @param
	 * @return String
	 * @return
	 */
	public static String getMyUUID(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// 依次 定义 设备ID, SIM序列号, AndroidID, tmPhone暂时没用到忽略
		final String tmDevice, tmSerial, tmPhone, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		// 生成UUID
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		Log.d("debug", "uuid=" + uniqueId);
		// 返回生成的uuid字符串
		return uniqueId;
	}

	/**
	 * 获取当前程序的版本号
	 * 
	 * @author yangwb
	 * @date 2017-5-16 上午10:49:10
	 * @param context
	 * @return 当前程序的版本号
	 */
	@SuppressWarnings("unused")
	public static int getVersionCode(Context context) {
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 判断有无SD卡
	 * @author: yangwb
	 * @date: 创建时间:2016-11-3 下午5:11:20
	 * @parameter:
	 * @return:
	 */
	public static boolean isSDExist() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取设备内置存储卡全部空闲大小 单位B
	 * @author: yangwb
	 * @date: 创建时间:2016-11-4 上午11:22:25
	 * @parameter:
	 * @return:
	 */
	@SuppressLint("NewApi")
	public static long getPhoneSelSDCardFreeSize() {
		if (!isSDExist()) {
			return 0;
		}
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		return stat.getAvailableBytes();
	}

	/**
	 * 拨打手机
	 * 
	 * @author Administrator
	 * @date 2017-5-19 上午11:39:09
	 * @param phone
	 */
	public static void callPhone(Context context, String phone) {
		if (phone == null || phone.equals("")) {
			Toast.makeText(context, "该联系人号码为空！", Toast.LENGTH_SHORT).show();
		} else {
			if (!hasTel(context)) {
				// 判断系统是否安装拨号软件 @pengch
				Toast.makeText(context, "当前系统没有安装拨号软件,请安装后再试!",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Uri uri = Uri.parse("tel:" + phone);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(intent);
		}
	}

	/**
	 * 拨打电话
	 * 
	 * @author Administrator
	 * @date 2017-5-19 上午11:39:09
	 * @param phone
	 */
	public static void callTel(Context context, String tel) {
		callPhone(context, tel);
	}

	/**
	 * 拨打短号
	 * 
	 * @author Administrator
	 * @date 2017-5-19 上午11:39:09
	 * @param phone
	 */
	public static void callDuanHao(Context context, String duanhao) {
		callPhone(context, duanhao);
	}

	/**
	 * 发送邮件
	 * 
	 * @author Administrator
	 * @date 2017-5-19 上午11:39:09
	 * @param phone
	 */
	public static void sendEmail(Context context, String personEmail) {
		if (personEmail == null || personEmail.equals("")) {
			Toast.makeText(context, "邮件地址为空！", Toast.LENGTH_SHORT).show();
		} else {
			if (!hasMail(context)) {
				// 判断系统是否安装邮箱软件 @pengch
				Toast.makeText(context, "当前系统没有安装邮箱软件,请安装后再试!",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Uri uri = Uri.parse("mailto:" + personEmail);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(intent);
		}
	}

	/**
	 * 显示输入法
	 * 
	 * @author pengch
	 * @date 2017-5-23 上午9:19:22
	 * @param context
	 */
	public static void showInputMethod(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 隐藏输入法
	 * 
	 * @author pengch
	 * @date 2017-5-23 上午9:19:33
	 * @param context
	 */
	public static void hideInputMethod(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}

	/**
	 * 判断系统是否安装拨号软件
	 * 
	 * @author pengch
	 * @date 2016-10-19 上午10:21:54
	 * @param
	 * @return boolean
	 * @param context
	 * @return
	 */
	private static boolean hasTel(Context context) {
		PackageManager pm = context.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("tel:"));
		List<ResolveInfo> list = pm.queryIntentActivities(intent,
				PackageManager.GET_INTENT_FILTERS);
		final int size = (list == null) ? 0 : list.size();
		return size > 0;
	}

	/**
	 * 判断系统是否安装邮箱软件
	 * 
	 * @author pengch
	 * @date 2016-10-19 上午10:21:54
	 * @param
	 * @return boolean
	 * @param context
	 * @return
	 */
	private static boolean hasMail(Context context) {
		PackageManager pm = context.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("mailto:"));
		List<ResolveInfo> list = pm.queryIntentActivities(intent,
				PackageManager.GET_INTENT_FILTERS);
		final int size = (list == null) ? 0 : list.size();
		return size > 0;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	/**
     * 判断是否有网络连接
     * @author yangwb
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
