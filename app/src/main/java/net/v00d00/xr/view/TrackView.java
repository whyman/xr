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
import net.v00d00.xr.XRUtils;

public class TrackView extends RelativeLayout {

	private TextView title;
	private TextView artist;
	private TextView track;
	private TextView duration;

	public TrackView(Context context) {
			super(context);
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.track_view, this, true);

			title = (TextView) findViewById(R.id.track_view_title);
			artist = (TextView) findViewById(R.id.track_view_artist);
			track = (TextView) findViewById(R.id.track_view_number);
			duration = (TextView) findViewById(R.id.track_view_length);
		}

	public void setArtist(CharSequence artist) {
		this.artist.setText(artist);
	}

	public void setTitle(CharSequence title) {
		this.title.setText(title);
	}

	public void setTrackNumber(long number) {
		track.setText(String.format("%02d", number));
	}

	public void setDuration(long duration) {
		this.duration.setText(XRUtils.formatSeconds(duration));
	}
}
