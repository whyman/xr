package net.v00d00.xr.view;

import net.v00d00.xr.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TrackView extends RelativeLayout {

	private TextView title;
	private TextView artist;

	public TrackView(Context context) {
			super(context);
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.track_view, this, true);

			title = (TextView) findViewById(R.id.track_view_title);
			artist = (TextView) findViewById(R.id.track_view_artist);
		}

	public CharSequence getArtist() {
		return artist.getText();
	}

	public void setArtist(CharSequence artist) {
		this.artist.setText(artist);
	}

	public CharSequence getTitle() {
		return title.getText();
	}

	public void setTitle(CharSequence title) {
		this.title.setText(title);
	}
}
