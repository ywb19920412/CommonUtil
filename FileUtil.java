package com.strongit.android.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.StatFs;
import android.widget.Toast;

import com.strongit.android.phone.oa.daiban.activity.DaiBanDetailActivity;
import com.strongit.android.phone.oa.email.util.DownloadEmailAttachPopWindow;
import com.strongit.android.phone.oa.login.model.User;
import com.strongit.android.view.dialog.TipOptionDialog;
import com.strongit.android.view.dialog.TipOptionDialog.OnTipDialogConfirmListener;
import com.strongit.phone.oa.R;

public class FileUtil {
	
	public static boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.equalsIgnoreCase(aEnd))
				return true;
		}
		return false;
	}

	
	/**
	 * 文件大小单位换算
	 * 
	 * @author: yangwb
	 * @date: 创建时间:2016-11-3 上午11:17:40
	 * @parameter:
	 * @return:
	 */
	public static String getFileSize(long filesize) {
		DecimalFormat df = new DecimalFormat("#.00");
		StringBuffer buffer = new StringBuffer();
		if (filesize < 1024) {
			buffer.append(filesize).append("B");
		} else if (filesize < 1024 * 1024) {
			buffer.append(df.format((double) filesize / 1024)).append("K");
		} else if (filesize < 1024 * 1024 * 1024) {
			buffer.append(df.format((double) filesize / 1024 / 1024)).append(
					"M");
		} else {
			buffer.append(df.format((double) filesize / 1024 / 1024 / 1024))
					.append("G");
		}
		return buffer.toString();
	}

	/**
	 * 删除下载的附件
	 * 
	 * @author: yangwb
	 * @date: 创建时间:2016-11-3 下午2:24:40
	 * @parameter:
	 * @return:
	 */
	public void deleteFile(File file) {
		if (file.exists()){
			if(file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					if (files.length == 0) {
						file.delete();
					} else {
						for (int i = 0; i < files.length; i++) {
							if (files[i].isDirectory()) {
								deleteFile(files[i]);
							}
							files[i].delete();
						}
					}
				}
			}else{
				file.delete();
			}
		}
	}

	
	/**
	 * 获取根路径
	 * 
	 * @author: yangwb
	 * @date: 创建时间:2016-11-4 上午8:57:30
	 * @parameter:
	 * @return:
	 */
	public static String getPath() {
		String path = "";
		if (isSDExist()) {
			String sdDir = Environment.getExternalStorageDirectory().getPath();
			path = sdDir + "/" + "strongit_OA/cache/";
		}
		return path;
	}

	

	
	/**
	 * SD卡中是否存在文件
	 * 
	 * @author yangwb
	 * @date 2016年9月29日09:43:41
	 * @param filePath
	 * @return
	 */
	public static boolean isFileInSD(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
	
	/**
	 * 清除已存在的文件
	 * 
	 * @author pengch
	 * @date 2016-11-29 上午10:06:16
	 * @param
	 * @return void
	 * @param fileAttachPath
	 */
	public static void clearFile(String fileAttachPath) {
		if (fileAttachPath == null || "".equals(fileAttachPath)) {
			return;
		}
		File file = new File(fileAttachPath);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * Java文件操作 获取文件扩展名
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return "";
	}
	
	/**
	 * Java文件操作 获取不带扩展名的文件名
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}
	/**
	  * 获取单个文件的MD5值
	  * @author yangwb
	  * @param file
	  * @return
	  */
	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
	/**
	 * 复制单个文件
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static String copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { //文件存在时
				InputStream inStream = new FileInputStream(oldPath); //读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ( (byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; //字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
				fs.close();
				inStream.close();
			}
		}
		catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
		return newPath;
	}
}
