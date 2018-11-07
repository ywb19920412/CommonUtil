package com.strongit.android.base.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * <p>
 * Title:WatermarkImageUtil
 * </p>
 * <p>
 * Description:水印图片生成工具类
 * </p>
 * <p>
 * Company:StrongIT
 * </p>
 * 
 * @version
 * @author pingr
 * @date 2017-5-10 下午3:41:13
 */
public class WatermarkImageUtil {
	// 水印透明度
	private static int alpha = 50;
	// 水印文字颜色
	private static int color = Color.RED;

	/**
	 * 根据指定文字生成水印图片
	 * 
	 * @param width
	 *            水印图片宽
	 * @param height
	 *            水印图片高
	 * @param logoText
	 *            水印文字
	 * @param srcImgPath
	 *            文件生成目录
	 * @param imageName
	 *            要保存文件名
	 * @param degree
	 *            仅支持45或者-45
	 */
	public static void markImageByText(Integer width, Integer height,
			String logoText, String srcImgPath, String imageName, Integer degree) {
		if (degree != 45 && degree != -45) {
			return;
		}
		OutputStream os = null;
		BufferedOutputStream bos = null;
		try {
			// 根据指定宽与高生成一个Bitmap对象
			Bitmap bitmap = Bitmap
					.createBitmap(width, height, Config.ARGB_8888);
			// 初始化画布绘制的图像到icon上
			Canvas canvas = new Canvas(bitmap);
			// 图层的背景色
			canvas.drawColor(Color.WHITE);// 图层的背景色
			// 创建画笔
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
					| Paint.DEV_KERN_TEXT_FLAG);
			// 文字大小
			paint.setTextSize(30.0f);
			// 字体类型
			paint.setTypeface(Typeface.DEFAULT);
			// 字体颜色
			paint.setColor(color);
			// 设置不透明度
			paint.setAlpha(alpha);// 设置不透明度
			// 根据角度计算水印平铺范围和偏移量,仅计算偏移45度的情况
			float degreeTemp = degree;
			double degreeHD = Math.PI * degreeTemp / 180;
			float x1 = (float) (Math.abs(Math.cos(degreeHD)) * width);
			float y1 = (float) (Math.abs(Math.sin(degreeHD)) * width);
			float x2 = (float) (Math.abs(Math.sin(degreeHD)) * height);
			float y2 = (float) (Math.abs(Math.cos(degreeHD)) * height);
			float canvasWidth = y1 + y2;
			float canvasHeight = x1 + x2;
			float transX = 0;
			float transY = 0;
			if (degreeTemp == 45) {
				transX = (float) (x1 * Math.sin(degreeHD));
				transY = -(float) (x1 * Math.cos(degreeHD));
			} else if (degreeTemp == -45) {
				transX = (float) (y2 * Math.sin(degreeHD));
				transY = (float) (y2 * Math.cos(degreeHD));
			}
			canvas.translate(transX, transY);
			canvas.rotate(degree);

			// 自定义文字对应的宽度，注以下单个文字的宽度是通过计算得出，如果调整了字体大小。应重新计算。
			int textWidth = logoText.length() * 15;
			// 生成水印
			for (int i = 0; i <= canvasWidth; i += textWidth + 100)
				for (int j = 0; j <= canvasHeight; j += 150) {
					canvas.drawText(logoText, i, j, paint);// 将文字写入
				}
			// 保存所有图层
			canvas.save(canvas.ALL_SAVE_FLAG);
			canvas.restore();
			// 指定文件生成的目录
			File file = new File(srcImgPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			// 将要保存图片的路径
			File file1 = new File(srcImgPath, imageName);
			os = new FileOutputStream(file1);
			bos = new BufferedOutputStream(os);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os) {
					os.close();
				}

				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

//	public static void main(String[] args) {
//		String srcImgPath = "/data/data/"+geta;
//		String targerPath = "test.jpg";
//		String logoText = "水印文字";
//		markImageByText(720, 1024, logoText, srcImgPath, targerPath, 45);
//	}
}
