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
import android.widget.ImageView;

public class FixedRatioImageView extends ImageView {

	private float heightRatio = 1.0f;
	private String uriPrefix;

	public FixedRatioImageView(Context context) {
		super(context);
	}

	public FixedRatioImageView(Context context, AttributeSet attributes) {
		super(context, attributes);
	}

	public FixedRatioImageView(Context context, String thumbnailPath) {
		this(context);
		setThumbnailPath(thumbnailPath);
	}

	public void setThumbnailPath(String path) {
		try {
			XRApplication.getApplication(getContext()).getPicasso()
				.load(getUriPrefix() + URLEncoder.encode(path, "utf-8"))
				.placeholder(R.drawable.placeholder)
				.fit()
				.into(this);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void setThumbnailPath(String path, float heightRatio) {
		this.heightRatio = heightRatio;
		setThumbnailPath(path);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    // Restrict the aspect ratio to 1:1, fitting within original specified dimensions
	    int width = MeasureSpec.getSize(widthMeasureSpec);;
	    widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
	    heightMeasureSpec = MeasureSpec.makeMeasureSpec((int)(width * heightRatio), MeasureSpec.EXACTLY);

	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private String getUriPrefix() {
		if (uriPrefix == null) {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
			uriPrefix = "http://" + sharedPref.getString("pref_host", "") + "/image/";
		}
		return uriPrefix;
	}
}
