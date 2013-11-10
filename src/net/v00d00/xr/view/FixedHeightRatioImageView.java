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
import android.util.AttributeSet;

public class FixedHeightRatioImageView extends ThumbnailView {

	private float heightRatio = 1.0f;

	public FixedHeightRatioImageView(Context context) {
		super(context);
	}

	public FixedHeightRatioImageView(Context context, AttributeSet attributes) {
		super(context, attributes);
	}

	public FixedHeightRatioImageView(Context context, String thumbnailPath) {
		this(context);
		setThumbnailPath(thumbnailPath);
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
}
