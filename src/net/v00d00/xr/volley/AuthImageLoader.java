package net.v00d00.xr.volley;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;

public class AuthImageLoader extends ImageLoader {

	public AuthImageLoader(RequestQueue queue, ImageCache imageCache) {
		super(queue, imageCache);
	}

	@Override
	public Request<?> getRequest(String requestUrl, Listener<Bitmap> listener,
			int maxWidth, int maxHeight, Config config,
			ErrorListener errorListener) {

		return new AuthImageRequest(requestUrl, listener, maxWidth, maxHeight, config, errorListener);
	}
}
