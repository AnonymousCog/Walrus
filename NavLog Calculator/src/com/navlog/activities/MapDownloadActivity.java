package com.navlog.activities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.example.navlog.calculator.R;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MapDownloadActivity extends Activity {
	
	private long downloadReference;
	private DownloadManager manager;
	String fileName;
	private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
		 
		  @Override
		  public void onReceive(Context context, Intent intent) 
		  {
			String path = Environment.getExternalStorageDirectory() + "/download/";
			//String fileName = "CJ-27-20-South.zip";
			
		    unpackZip(path, fileName+".zip");
		    File zipFile = new File(path, fileName+".zip");
		    zipFile.delete();
		  }
		 }; 


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_download);
		
		IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(downloadReceiver, filter);
		
	}
	
	@Override
	protected void onStop()
	{
	    unregisterReceiver(downloadReceiver);
	    super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_download, menu);
		return true;
	}
	
	
	
	public void downloadMap(View view)
	{
		Button b = (Button)view;
		fileName = b.getText().toString().substring(9);
		if(isDownloadManagerAvailable(this.getBaseContext()))
		{
		
			String url = getURL(fileName);
			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
			request.setDescription("Downloading Map");
			request.setTitle(fileName);
			// in order for this if to run, you must use the android 3.2 to compile your app
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			    request.allowScanningByMediaScanner();
			    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			}
			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName+".zip");
			
			

			// get download service and enqueue file
			manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
			 downloadReference = manager.enqueue(request);
			

		}
		
	}
	
	private String getURL(String aFileName)
	{
		String url;
		if(aFileName.equals("CJ-27-20-South"))
		{
			 url = "https://dl.dropbox.com/s/x1em1vx0rrby0yi/CJ-27-20-South.zip?token_hash=AAEcdpTPTz0kJeO3avkBhbg2nJ5LD_SwlDg8UVKUtKB-Pw&dl=1";
		}
		else //if(aFileName.equals("CJ-27-20 North"))
		{
			url = "https://dl.dropboxusercontent.com/s/95y07z5eosnhe2v/CJ-27-20-North.zip?token_hash=AAG2u6ODbkUXopsjmDaXnprTgfjrkksE4zvOIVmSZBqJIw&dl=1";
		}
		return url;
	}
	
	public static boolean isDownloadManagerAvailable(Context context) {
	    try {
	        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
	            return false;
	        }
	        Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.addCategory(Intent.CATEGORY_LAUNCHER);
	        intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
	        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
	                PackageManager.MATCH_DEFAULT_ONLY);
	        return list.size() > 0;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	private boolean unpackZip(String path, String zipname)
	{       
	     InputStream is;
	     ZipInputStream zis;
	     Context context = (Context) this;
         String privatePath = context.getDir("maps", Context.MODE_PRIVATE).getPath()+"/";
	     try 
	     {
	         String filename=null;
	         is = new FileInputStream(path + zipname);
	         zis = new ZipInputStream(new BufferedInputStream(is));          
	         ZipEntry ze;
	         byte[] buffer = new byte[1024];
	         int count;

	         while ((ze = zis.getNextEntry()) != null) 
	         {
	             // zapis do souboru
	             filename = ze.getName();

	             // Need to create directories if not exists, or
	             // it will generate an Exception...
	             if (ze.isDirectory()) {
	                File fmd = new File(privatePath + filename);
	                fmd.mkdirs();
	                continue;
	             }
	             
	             FileOutputStream fout = new FileOutputStream(privatePath + filename);

	             // cteni zipu a zapis
	             while ((count = zis.read(buffer)) != -1) 
	             {
	                 fout.write(buffer, 0, count);             
	             }

	             fout.close();               
	             zis.closeEntry();
	         }

	         zis.close();
	          

	     } 
	     catch(IOException e)
	     {
	         e.printStackTrace();
	         return false;
	     }

	    return true;
	}
	
	

	
	
	public boolean copyFile(File src,File dst)throws IOException{
        if(src.getAbsolutePath().toString().equals(dst.getAbsolutePath().toString())){

            return true;

        }else{
            InputStream is=new FileInputStream(src);
            OutputStream os=new FileOutputStream(dst);
            byte[] buff=new byte[1024];
            int len;
            while((len=is.read(buff))>0){
                os.write(buff,0,len);
            }
            is.close();
            os.close();
        }
        return true;
    }
	
	
	

}


