package com.stcyclub.testhtmlgetlinkman;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final int CAMERA = 0x01;
	private String contactId, contactName;
	private WebView webView;
	private Handler handler = new Handler();
	// 自定义的弹出框类
	SelectPicPopupWindow menuWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		webView = (WebView) findViewById(R.id.myweb);
		// 重要：让webview支持javascript
		webView.getSettings().setJavaScriptEnabled(true);
		// 重要：添加可以供html中可供javascript调用的接口类
		webView.addJavascriptInterface(new MyJavaScript(this, handler),
				"myjavascript");
		// 加载index.html
		webView.loadUrl("file:///android_asset/index.html");
	}

	public void btnClick(View v) {
		// 实例化SelectPicPopupWindow
		menuWindow = new SelectPicPopupWindow(MainActivity.this, itemsOnClick);
		// 显示窗口
		menuWindow.showAtLocation(MainActivity.this.findViewById(R.id.main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {

			default:
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("TAG", "requestCode" + requestCode + "\t resultCode" + resultCode
				+ "\t" + data);
		String filename = null;
		String name = null;
		if (requestCode == CAMERA && resultCode == Activity.RESULT_OK
				&& null != data) {
			String sdState = Environment.getExternalStorageState();
			if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
				Log.d("Tag", "sd card unmount");
				return;
			}
			new DateFormat();
			name = DateFormat.format("yyyyMMdd_hhmmss",
					Calendar.getInstance(Locale.CHINA))
					+ ".jpg";
			Bundle bundle = data.getExtras();
			// 获取相机返回的数据，并转换为图片格式
			Bitmap bitmap = (Bitmap) bundle.get("data");
			FileOutputStream fout = null;
			File file = new File("/sdcard/pintu/");
			if (!file.exists()) {
				file.mkdirs();
			}
//			filename = file.getPath() + "/" + name;
//			 try {
//				 fout = new FileOutputStream(filename);
//				 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);	
//			 } catch (FileNotFoundException e) {
//				 e.printStackTrace();
//			 } finally {
//			 try {
//				 fout.flush();
//				 fout.close();
//			 } catch (IOException e) {
//				 e.printStackTrace();
//			 }
//			 }
			
			String str ="iVBORw0KGgoAAAANSUhEUgAAAKsAAAC0CAYAAADraNxXAAAEaElEQVR4Xu3YS04UahhFUWgxMYbNmOjT0kACKct6ija2Z92mQTn/t1cqlfv4+vr648F/LhC4wCOsgUomflwAVhAyF4A1k8pQWBnIXADWTCpDYWUgcwFYM6kMhZWBzAVgzaQyFFYGMheANZPKUFgZyFwA1kwqQ2FlIHMBWDOpDIWVgcwFYM2kMhRWBjIXgDWTylBYGchcANZMKkNhZSBzAVgzqQyFlYHMBWDNpDIUVgYyF4A1k8pQWBnIXADWTCpDYWUgcwFYM6kMhZWBzAVgzaQyFFYGMheANZPKUFgZyFwA1kwqQ2FlIHMBWDOpDIWVgcwFYM2kMhRWBjIXgDWTylBYGchcANZMKkNhZSBzAVgzqQyFlYHMBWDNpDIUVgYyF4A1k8pQWBnIXADWTCpDYWUgcwFYM6kMhZWBzAVgzaQyFFYGMheANZPKUFgZyFwA1kwqQ2FlIHMBWDOpDIWVgcwFYM2kMhRWBjIXgDWTylBYGchcANZMKkNhZSBzAVgzqQyFlYHMBWDNpDIUVgYyF4A1k8pQWBnIXADWTCpDYWUgcwFYM6kMhZWBzAVgzaQyFFYGMheANZPKUFgZyFwA1kwqQ6exvry8fAh4fn7+kvD5Z8c0Tv3M4Z/dQunU73v/e8e/89Kee3/nLbsqPzOL9RDIOYjXMN8D55bfdwj3/d8+/v3nsFewfXfnLNZjGKcOee2T8B6s537fJZCw/loF1qOvAZ/nufQpdukT9+np6eHt7e3ryrd8fTj3NQBWWH/7fnrqE/JerJ+fnIdYj//dS8g/f/bwZ2CF9Z9hvfbVAtbvfWv1NeAvfQ3wyfo9iLf87Vmsl/530aVPyGvfL899Z/3T/z11bectkf+Xn5nF+r8EXHoHrEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142/9CRINeTax7S6yAAAAAElFTkSuQmCC";			
			
			//bitmap=stringtoBitmap(str);
			 
			String str1=bitmaptoString(bitmap);
			Log.d("TAG....", str1.toString());
			Log.d("TAG....", str.length()+"");
			// 输出设置到html上面
			String url ="javascript:printInfo('data:image/png;base64,"+str1+"')";
			webView.loadUrl(url);

			// BitmapDrawable bd=new BitmapDrawable(bitmap);
			// image.setBackground(bd);
			// image.setImageBitmap(bitmap);;

		}
		// StringBuffer sb=new StringBuffer();
		// Log.d("TAG", "filename..."+filename);
		// Log.d("TAG",
		// "filename..."+Environment.getExternalStorageDirectory()+File.separator+"a.jpg");
		// Log.d("TAG",
		// "filename..."+"file://"+Environment.getExternalStorageDirectory()+File.separator+"pintu/"+name);
		// String fileUrl="file:///mnt/sdcard/pintu/20140221_114052.jpg";
		// // sb.append("<img src=\"file:///"+filename+"\"/>");
		// sb.append("<img src="+fileUrl+">");
		//
		// // webView.loadDataWithBaseURL(null, sb.toString(), "text/html",
		// "utf-8", null);
		// webView.loadDataWithBaseURL(null,sb.toString(), "text/html", "UTF-8",
		// null);
	}

	public String bitmaptoString(Bitmap bitmap) {
		// 将Bitmap转换成Base64字符串
		StringBuffer string = new StringBuffer();
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		
		try {
			bitmap.compress(CompressFormat.PNG, 100, bStream);
			bStream.flush();
			bStream.close();
			byte[] bytes = bStream.toByteArray();
			string.append(Base64.encodeToString(bytes, Base64.NO_WRAP));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("string.."+string.length());
		return string.toString();
	}
	public Bitmap stringtoBitmap(String string){
	    //将字符串转换成Bitmap类型
	    Bitmap bitmap=null;
	    try {
		    byte[]bitmapArray;
		    bitmapArray=Base64.decode(string, Base64.DEFAULT);
		    bitmap=BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
	} catch (Exception e) {
		e.printStackTrace();
	}
	   
	    return bitmap;
	    }

	private String getPhoneContacts(String contactId2) {
		Cursor cursor = null;
		String name = "";
		String phone = "";
		try {
			// Uri uri = People.CONTENT_URI;
			Uri uri = ContactsContract.Contacts.CONTENT_URI;
			cursor = getContentResolver().query(uri, null,
					ContactsContract.Contacts._ID + "=?",
					new String[] { contactId }, null);
			if (cursor.moveToFirst()) {
				name = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				phone = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._COUNT));
				Log.d("Tag", phone);
			} else {
				Toast.makeText(this, "No contact found.", Toast.LENGTH_LONG)
						.show();
			}
		} catch (Exception e) {
			name = "";
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return name;
	}

}
