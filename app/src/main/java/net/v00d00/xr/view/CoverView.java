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

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.v00d00.xr.R;

public class CoverView extends RelativeLayout {

	private int position = 0;
	private TextView title;
	private AspectRatioImageView image;

	public CoverView(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.cover_view, this, true);

		image = (AspectRatioImageView) findViewById(R.id.cover_view_background);
		title = (TextView) findViewById(R.id.cover_view_title);
	}

	public CoverView(Context context, String title, String thumbnailPath) {
		this(context);
		this.title.setText(title);
		image.setThumbnailPath(thumbnailPath);
	}

	public void setTitle(String text) {
		title.setText(text);
	}

	public void setThumbnailPath(String path) {
		image.setThumbnailPath(path);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setThumbnailAspect(float ratio) {
		image.setAspectRatio(ratio);
	}
}
