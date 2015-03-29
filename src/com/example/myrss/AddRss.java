package com.example.myrss;

import java.net.URL;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import com.rss.data.ChannelDataHelper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

 public class AddRss extends Activity {
	
	private Button addRss;
	
	private Button verifyRss;
	
	private Button quit;
	
	private EditText rssText;
	
	ChannelDataHelper mChannelData;
	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_rss_dialog);
		
		mContext = this;
		mChannelData = new ChannelDataHelper(this);
		
		addRss = (Button)findViewById(R.id.add_rss);
		verifyRss = (Button)findViewById(R.id.verify_rss);
		quit = (Button )findViewById(R.id.quit_rss);
		rssText = (EditText)findViewById(R.id.rss_add);
		
		addRss.setOnClickListener(clickListener);
		verifyRss.setOnClickListener(clickListener);
		quit.setOnClickListener(clickListener);
	}
	
	OnClickListener clickListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
				
			case R.id.add_rss:
				saveRss();
				break;
				
			case R.id.verify_rss:
				if (verifyRss(rssText.getText().toString()) != null
				&& verifyRss(rssText.getText().toString()) != "默认")
					Toast.makeText(mContext, "验证通过", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(mContext, "验证失败", Toast.LENGTH_LONG).show();
				break;
				
			case R.id.quit_rss:
				Intent it = new Intent();
				it.setClass(AddRss.this, SelectChannel.class);
				
				startActivity(it);
				AddRss.this.finish();
				
				break;
				
				default:
					break;
			}
		}
	};
	
	
	protected void saveRss() {
		String rssAddress = rssText.getText().toString().trim();
		String rssName = verifyRss(rssAddress);
		if(rssName != null && rssName != "默认" && (-1 != mChannelData.SaveChannelInfo(rssName, rssAddress))) {
			Toast.makeText(mContext, "保存成功", Toast.LENGTH_LONG).show();
			rssText.setText("");
		} else {
			Toast.makeText(mContext, "保存失败", Toast.LENGTH_LONG).show();
		}
	}
	
	private String verifyRss(String urlString){
		try {
			URL url = new URL(urlString);
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlReader = parser.getXMLReader();
			
			getRssChannel rssHandler = new getRssChannel();
			xmlReader.setContentHandler(rssHandler);
			
			InputSource is = new InputSource(url.openStream());
			xmlReader.parse(is);
			
			return rssHandler.getChannel();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	class getRssChannel extends DefaultHandler {
		int flag = 1;
		String channel = "默认";
		
		public getRssChannel(){
			
		}
		
		public String getChannel(){
			return channel;
		}
		
		public void startDocument() throws SAXException {
			
		}
		
		public void endDocunment() throws SAXException {
			super.endDocument();
		}
		
		public void startElement(String uri, String localName, String qName, 
						Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			
			if (localName.equals("channel")) {
				flag = 1;
				return;
			}
			
			if (localName.equals("item")) {
				flag = 0;
				return;
			}
			
			if(flag == 1){
				if(localName.equals("title")){
					flag = 2;
				}
			}
		}
		
		public void endElement(String uri, String localName, String qName) throws SAXException{
			
		}
		
		public void characters (char[] ch, int start, int length){
			if (length <= 5) return;
			String theString = new String (ch, start, length);
			
			if (flag == 2) {
				channel = theString;
				flag = 0;
			}
		}

	}

}
