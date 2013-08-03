package net.v00d00.xr;

import net.v00d00.xr.volley.AuthImageLoader;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;

public class XRApplication extends Application {
    private RequestQueue requestQueue;
    private ImageLoader loader;

    private final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(Math.round(Runtime.getRuntime().maxMemory() * Math.min(0.5f, 100)));

    @Override
    public void onCreate() {
		final String xbmc = "xbmc";

		requestQueue = Volley.newRequestQueue(getApplicationContext());

		ImageCache imageCache = new ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                mImageCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return mImageCache.get(key);
            }
        };

		loader = new AuthImageLoader(requestQueue, imageCache);

        super.onCreate();
    }

    public RequestQueue getBitmapCache() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
    	return loader;
    }

    public static XRApplication getApplication(Context context) {
        return (XRApplication) context.getApplicationContext();
    }
}
