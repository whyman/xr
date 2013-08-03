package net.v00d00.xr.volley;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageRequest;

public class AuthImageRequest extends ImageRequest {

	public AuthImageRequest(String url, Listener<Bitmap> listener,
			int maxWidth, int maxHeight, Config decodeConfig,
			ErrorListener errorListener) {
		super(url, listener, 300, 300, decodeConfig, errorListener);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>(1);
		headers.put("Authorization", "Basic " + Base64.encodeToString("xbmc:xbmc".getBytes(), Base64.NO_WRAP));
		return headers;
	}

}
