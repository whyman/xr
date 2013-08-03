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
