package com.stcyclub.testhtmlgetlinkman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import com.stcyclub.testhtmlgetlinkman.application.MyApplication;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity2 extends Activity {
	public static final int CAMERA  = 0x01;
	  
	private String contactId, contactName;
	private WebView webView;
	private Handler handler = new Handler();
	// �Զ���ĵ�������
	SelectPicPopupWindow2 menuWindow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_activity2);
		init();
	}

	
	
	private void init() {
		// TODO Auto-generated method stub 
		webView = (WebView) findViewById(R.id.myweb2);
		// ��Ҫ����webview֧��javascript
		webView.getSettings().setJavaScriptEnabled(true);
		// ��Ҫ����ӿ��Թ�html�пɹ�javascript���õĽӿ��� 
		webView.addJavascriptInterface(new MyJavaScript(this, handler),
				"myjavascript");
		Intent intent = getIntent();// getIntent������Ŀ�а�����ԭʼintent����������������������intent��ֵ��һ��Intent���͵ı���intent
		Bundle bundle = intent.getExtras();// .getExtras()�õ�intent�������Ķ�������
		String url = bundle.getString("url");// getString()����ָ��key��ֵ
		Log.d("TAGGG", "url...."+url);
		if (null != MyApplication.ld) {
			MyApplication.ld.dismiss();
		}
		WebSettings websetting = webView.getSettings();   
		 /*����ҳ�淽ʽ2. ����ַŴ���С�İ�ť */
//		websetting.setSupportZoom(true); 
//		websetting.setBuiltInZoomControls(true); 
		webView.loadUrl(url);
	}
	/**
	 * ����¼�
	 * @param v
	 */
	public void btnClick(View v) {
		if(v.getId()==R.id.regGoBackLogin){
			MainActivity2.this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			
		}else if(v.getId()==R.id.share){
			// ʵ����SelectPicPopupWindow
			menuWindow = new SelectPicPopupWindow2(MainActivity2.this);
			// ��ʾ����
			menuWindow.showAtLocation(MainActivity2.this.findViewById(R.id.main),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // ����layout��PopupWindow����ʾ��λ��
		}else if(v.getId()==R.id.getByCamera){
			Intent intent = new Intent();
      		 Intent intent_camera = getPackageManager()
      				.getLaunchIntentForPackage("com.android.camera");
      		if (intent_camera != null) {
      			intent.setPackage("com.android.camera");
      		}
      		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
      		startActivityForResult(intent, CAMERA);// r
      		menuWindow.dismiss();
		}else if(v.getId()==R.id.getByAlbum){
			menuWindow.dismiss();
		}else if(v.getId()==R.id.btn_cancel){
			menuWindow.dismiss();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == CAMERA && resultCode == Activity.RESULT_OK && null != data){
			   String sdState=Environment.getExternalStorageState();
			   if(!sdState.equals(Environment.MEDIA_MOUNTED)){
			    Log.d("Tag", "sd card unmount");
			    return;
			   }
			   new DateFormat();
			   String name= DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))+".jpg";
			   Bundle bundle = data.getExtras();
			   //��ȡ������ص����ݣ���ת��ΪͼƬ��ʽ
			   Bitmap bitmap = (Bitmap)bundle.get("data");
			   FileOutputStream fout = null;
			   //�����ļ��洢·��
			   File file = new File("/sdcard/pintu/");
			   if(!file.exists()){
				   file.mkdirs();
			   }
			   String filename=file.getPath()+"/"+name;
			   try {
			    fout = new FileOutputStream(filename);
			    //��ͼƬ����ѹ��
			    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
			   } catch (FileNotFoundException e) {
			    e.printStackTrace();
			   }finally{
			    try {
			     fout.flush();
			     fout.close();
			    } catch (IOException e) {
			     e.printStackTrace();
			    }
			   }
			   //Ӧ�ý�ͼƬ���͵���������Ȼ����¼�����ҳ
//			   BitmapDrawable bd=new BitmapDrawable(bitmap);
//			   image.setBackground(bd);
//			   image.setImageBitmap(bitmap);;
			   
		}
	}

}
