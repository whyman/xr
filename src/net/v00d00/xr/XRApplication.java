/*

XR - Modern Android XBMC remote
Copyright (C) 2013 Ian Whyman

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

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
