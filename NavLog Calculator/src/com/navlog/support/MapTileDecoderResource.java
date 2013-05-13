package com.navlog.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.qozix.mapview.tiles.MapTileDecoder;

public class MapTileDecoderResource implements MapTileDecoder 
{

private static final BitmapFactory.Options OPTIONS = new BitmapFactory.Options();
static {
	OPTIONS.inPreferredConfig = Bitmap.Config.RGB_565;
}

@Override
public Bitmap decode( String fileName, Context context ) {
	File privateDir = context.getDir("maps", Context.MODE_PRIVATE);
	File fileWithinDir = new File(privateDir, fileName);
	try {
		InputStream input = new FileInputStream(fileWithinDir);
		if ( input != null ) {
			try {
				return BitmapFactory.decodeStream( input, null, OPTIONS );										
			} catch ( OutOfMemoryError oom ) {
				// oom - you can try sleeping (this method won't be called in the UI thread) or try again (or give up)
			} catch ( Exception e ) {
				// unknown error decoding bitmap
			}
		}
	} catch ( IOException io ) {
		// io error - probably can't find the file
	} catch ( Exception e ) {
		// unknown error opening the asset
	}
	return null;
}


}