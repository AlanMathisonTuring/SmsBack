package com.nutegg.smsback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nutegg.smsback.domain.SmsInfo;

public class MainActivity extends Activity {
	private String logName = "MainActivity";

	private List<SmsInfo> smsInfoList; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		smsInfoList = new ArrayList<SmsInfo>();
		Random random = new Random();
		for(int i=0;i<10;i++){
			smsInfoList.add(new SmsInfo(i,System.currentTimeMillis(),random.nextInt(2)+1,i+"你中饭吃了么?","13575757157"));
		}
		
	}

	//备份短信1(传统方法)
	public void saveSms1(View view){
		//假设已经获取到全部的短信
		//方法1:拼接一个XML格式的字符串通过输出流保存为XML文件到应用目录下
		//此方法如果内容中有<,>这种符号,XML文件会被破坏
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<smss>");
		for(SmsInfo smsInfo : smsInfoList){
			sb.append("<sms>");
			sb.append("<address>");
			sb.append(smsInfo.getAddress());
			sb.append("</address>");
			sb.append("</sms>");
			
			sb.append("<sms>");
			sb.append("<content>");
			sb.append(smsInfo.getContent());
			sb.append("</content>");
			sb.append("</sms>");
			
			sb.append("<sms>");
			sb.append("<type>");
			sb.append(smsInfo.getType());
			sb.append("</type>");
			sb.append("</sms>");
			
			sb.append("<sms>");
			sb.append("<data>");
			sb.append(smsInfo.getDate());
			sb.append("</data>");
			sb.append("</sms>");
		}		
		sb.append("</smss>");
		try{
			File file = new File(getFilesDir(),"SMSINFO.xml");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			fos.close();
			Toast.makeText(this, "短信备份成功", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(this, "短信备份失败", Toast.LENGTH_SHORT).show();
		}
		
	}
	//备份短信
		public void saveSms(View view){
			//假设已经获取到全部的短信
			//方法2:通过XML序列化器去生成一个XML文件.
			XmlSerializer serializer = Xml.newSerializer();
			File file = new File(getFilesDir(),"SMSINFO.xml");
			//File file = new File("/data/data","SMSINFO.xml");
			try {
				Log.i(logName, Environment.getDataDirectory().getPath());
				FileOutputStream fos = new FileOutputStream(file);
				Log.i(logName, "此处已经创建了输出流");
				serializer.setOutput(fos,"utf-8");
				Log.i(logName, "此处在序列化器中添加文件");
				serializer.startDocument("utf-8", true);
				serializer.startTag(null, "smss");
				for(SmsInfo smsInfo : smsInfoList){
					serializer.startTag(null, "sms");
					serializer.attribute(null, "id", smsInfo.getId()+"");
					serializer.startTag(null, "date");
					serializer.text(smsInfo.getDate()+"");
					serializer.endTag(null, "date");
					serializer.startTag(null, "content");
					serializer.text(smsInfo.getContent());
					serializer.endTag(null, "content");
					serializer.startTag(null, "type");
					serializer.text(smsInfo.getType()+"");
					serializer.endTag(null, "type");
					serializer.startTag(null, "adress");
					serializer.text(smsInfo.getAddress());
					serializer.endTag(null, "adress");
					serializer.endTag(null, "sms");
				}				
				serializer.endDocument();
				fos.close();
				Log.i(logName, "此处已经保存短信!");
				Toast.makeText(this, "短信备份成功", Toast.LENGTH_SHORT).show();
			} catch (Exception e){
				e.printStackTrace();
				Toast.makeText(this, "短信备份失败", Toast.LENGTH_SHORT).show();
			}
			
		}
		
		//读取短信
		public void readSms(View view){
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
				TextView textView = (TextView)MainActivity.this.findViewById(R.id.tv);
				ImageButton imageButton = (ImageButton)MainActivity.this.findViewById(R.id.ib);
				Button button = (Button)MainActivity.this.findViewById(R.id.bt);
				imageButton.setVisibility(View.INVISIBLE);
				button.setVisibility(View.INVISIBLE);
				Log.i("xxx", strb.toString());
				textView.setText(strb.toString());
			
			} catch (Exception e){
				e.printStackTrace();
				Toast.makeText(this, "短信读取失败", Toast.LENGTH_SHORT).show();
			}
			
		}
		
		public void nextPage(View view){
			Intent intent = new Intent();
			intent.setClassName("com.nutegg.smsback", "com.nutegg.smsread.SmsReadActivity.java");
			startActivity(intent);
		}
}
