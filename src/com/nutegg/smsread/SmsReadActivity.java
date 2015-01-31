package com.nutegg.smsread;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nutegg.smsback.R;
import com.nutegg.smsback.domain.SmsInfo;

public class SmsReadActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_readsms);
		TextView textView = (TextView)SmsReadActivity.this.findViewById(R.id.tv);
		readSms(textView);
	}
	
	//读取短信
	public void readSms(TextView textView){
		//假设已经获取到全部的短信
		//方法2:通过XML序列化器去生成一个XML文件.
		XmlPullParser parser = Xml.newPullParser();
		File file = new File(getFilesDir(),"SMSINFO.xml");
		//File file = new File("/data/data","SMSINFO.xml");
		try {
			FileInputStream fis = new FileInputStream(file);
			parser.setInput(fis,"utf-8");
			List<SmsInfo> smsInfoList = null;
			SmsInfo smsInfo = null;
			int type = parser.getEventType();
			while(type != XmlPullParser.END_DOCUMENT){
				switch(type){
				case XmlPullParser.START_TAG:
					if("smss".equals(parser.getName())){
						smsInfoList = new ArrayList<SmsInfo>();
					}else if("sms".equals(parser.getName())){
						smsInfo = new SmsInfo();
						String id = parser.getAttributeValue(0);
						smsInfo.setId(Integer.parseInt(id));
					}else if("date".equals(parser.getName())){
						String date = parser.nextText();
						smsInfo.setDate(Long.parseLong(date));
					}else if("content".equals(parser.getName())){
						String content = parser.nextText();
						smsInfo.setContent(content);
					}else if("type".equals(parser.getName())){
						String type_ = parser.nextText();
						smsInfo.setType(Integer.parseInt(type_));
					}else if("adress".equals(parser.getName())){
						String adress = parser.nextText();
						smsInfo.setAddress(adress);
					}
					break;
				case XmlPullParser.END_TAG:
					if("sms".equals(parser.getName())){
						smsInfoList.add(smsInfo);
						smsInfo = null;
					}
					break;
				}
				type = parser.next();
			}
			
			StringBuffer strb= new StringBuffer();
			for(SmsInfo smsInfos : smsInfoList){
				String sms = smsInfos.toString();
				strb.append(sms);
				strb.append("\n");
			}
			textView.setText(strb);
		
		} catch (Exception e){
			e.printStackTrace();
			Toast.makeText(this, "短信读取失败", Toast.LENGTH_SHORT).show();
		}
		
	}

	
}
