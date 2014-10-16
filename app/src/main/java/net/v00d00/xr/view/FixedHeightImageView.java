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

public class FixedHeightImageView extends ThumbnailView {

	public FixedHeightImageView(Context context) {
		super(context);
	}

	public FixedHeightImageView(Context context, AttributeSet attributes) {
		super(context, attributes);
	}

	public FixedHeightImageView(Context context, String thumbnailPath) {
		this(context);
		setThumbnailPath(thumbnailPath);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (getDrawable() != null) {

			int imgW = getDrawable().getIntrinsicWidth();
			int imgH = getDrawable().getIntrinsicHeight();

			if (imgW != -1 && imgH != -1) {
				float widthRatio = imgW / imgH;
			    int height = MeasureSpec.getSize(heightMeasureSpec);

			    heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
			    widthMeasureSpec = MeasureSpec.makeMeasureSpec((int)(height * widthRatio), MeasureSpec.EXACTLY);
			}
		}
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
