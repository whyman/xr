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

package net.v00d00.xr.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.v00d00.xr.R;
import net.v00d00.xr.XRApplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class ThumbnailView extends ImageView {

	private String uriPrefix;

	public ThumbnailView(Context context) {
		super(context);
	}

	public ThumbnailView(Context context, AttributeSet attributes) {
		super(context, attributes);
	}

	public ThumbnailView(Context context, String thumbnailPath) {
		this(context);
		setThumbnailPath(thumbnailPath);
	}

	public void setThumbnailPath(String path) {
		setThumbnailPath(path, false);
	}

	public void setThumbnailPath(String path, boolean skipCache) {
		try {
			if (path != null && path.length() > 0) {
				if (skipCache) {
					XRApplication.getApplication(getContext()).getPicasso()
						.load(getUriPrefix() + URLEncoder.encode(path, "utf-8"))
						.placeholder(R.drawable.placeholder)
						.skipMemoryCache()
						.fit()
						.into(this);
				} else {
					XRApplication.getApplication(getContext()).getPicasso()
					.load(getUriPrefix() + URLEncoder.encode(path, "utf-8"))
					.placeholder(R.drawable.placeholder)
					.fit()
					.into(this);
				}
			} else {
				XRApplication.getApplication(getContext()).getPicasso()
				.load(R.drawable.placeholder)
				.into(this);
			}
		} catch (UnsupportedEncodingException e) {
			Log.e("ThumbnailView", e.getMessage());
		}
	}

	protected String getUriPrefix() {
		if (uriPrefix == null) {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
			uriPrefix = "http://" + sharedPref.getString("pref_host", "") + "/image/";
		}
		return uriPrefix;
	}
}
