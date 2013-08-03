package net.v00d00.xr.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.v00d00.xr.R;
import net.v00d00.xr.XRApplication;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

public class CoverView extends RelativeLayout {

	private int position = 0;
	private TextView title;
	private NetworkImageView image;

	public CoverView(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.cover_view, this, true);

		image = (NetworkImageView) findViewById(R.id.cover_view_background);
		title = (TextView) findViewById(R.id.cover_view_title);

		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics()));
		setLayoutParams(lp);
	}

	public CoverView(Context context, String title, String thumbnailPath) {
		this(context);
		this.title.setText(title);
		setThumbnailPath(thumbnailPath);
	}

	public void setTitle(String text) {
		title.setText(text);
	}

	public void setThumbnailPath(String path) {
		Log.d("SET_THUMB", path);
		try {
			image.setImageUrl("http://192.168.1.100/image/" + URLEncoder.encode(path, "utf-8"), XRApplication.getApplication(getContext()).getImageLoader());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
