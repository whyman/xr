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

package net.v00d00.xr.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.v00d00.xr.R;
import net.v00d00.xr.XRApplication;
import net.v00d00.xr.view.TrackView;

import org.xbmc.android.jsonrpc.api.AbstractCall;
import org.xbmc.android.jsonrpc.api.call.AudioLibrary;
import org.xbmc.android.jsonrpc.api.call.AudioLibrary.GetSongs.FilterAlbumId;
import org.xbmc.android.jsonrpc.api.call.Player;
import org.xbmc.android.jsonrpc.api.model.AudioModel;
import org.xbmc.android.jsonrpc.api.model.AudioModel.AlbumDetail;
import org.xbmc.android.jsonrpc.api.model.AudioModel.SongDetail;
import org.xbmc.android.jsonrpc.api.model.PlaylistModel.Item;
import org.xbmc.android.jsonrpc.api.model.PlaylistModel.Item.Songid;
import org.xbmc.android.jsonrpc.io.ApiCallback;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AlbumFragment extends AbstractXRFragment {

	private ListView trackList;
	private AlbumAdapter adapter;
	private AlbumDetail album;

	public void setAlbum(AlbumDetail album) {
		this.album = album;
	}

	@Override
	public void load() {
		// create api call object
		FilterAlbumId fa = new FilterAlbumId(album.albumid);

		final AudioLibrary.GetSongs call = new AudioLibrary.GetSongs(fa,
				AudioModel.SongFields.ARTIST,
				AudioModel.SongFields.TITLE,
				AudioModel.SongFields.TRACK,
				AudioModel.SongFields.DISPLAYARTIST,
				AudioModel.SongFields.DURATION);

		// execute				cm.call(new Play, callback)
		getConnectionManager().call(call, new ApiCallback<SongDetail>() {
		    public void onResponse(final AbstractCall<SongDetail> call) {
		    	getActivity().runOnUiThread(new Runnable() {
		    		public void run() {
				    	adapter.clear();
				        adapter.addAll(call.getResults());
				        adapter.notifyDataSetChanged();
		    		}
		    	});
		    }
		    public void onError(int code, String message, String hint) {
		        Log.d("TEST", "Error " + code + ": " + message);
		    }
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (adapter == null)
			adapter = new AlbumAdapter(getActivity(), new ArrayList<SongDetail>());

		trackList = (ListView) inflater.inflate(R.layout.fragment_album, container, false);

		View header = inflater.inflate(R.layout.track_list_header, trackList, false);
		final ImageView image = (ImageView) header.findViewById(R.id.album_cover);

		final ViewTreeObserver vto = image.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
		    public boolean onPreDraw() {

				try {
					XRApplication.getApplication(getActivity()).getPicasso()
						.load("http://192.168.1.100/image/" + URLEncoder.encode(album.thumbnail, "utf-8"))
						.resize(image.getMeasuredWidth(), image.getMeasuredHeight())
						.centerCrop()
						.placeholder(R.drawable.placeholder)
						.into(image);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				// Unbind ourselves once we have triggered the load
				image.getViewTreeObserver().removeOnPreDrawListener(this);

		        return true;
		    }
		});

		((TextView) header.findViewById(R.id.album_artist)).setText(album.displayartist);
		((TextView) header.findViewById(R.id.album_name)).setText(album.title);

		trackList.addHeaderView(header);
		trackList.setAdapter(adapter);

		trackList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
				final SongDetail detail = adapter.getItem(pos - 1);

				Context context = getActivity().getApplicationContext();
				String text = "Track: " + Integer.toString(detail.track);
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();

				getConnectionManager().call(new Player.Open(new Item(new Songid(detail.songid))), new ApiCallback<String>() {

					@Override
					public void onResponse(AbstractCall<String> call) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onError(int code, String message, String hint) {
						// TODO Auto-generated method stub

					}
				});
			}
		});
		return trackList;
	}

	private static class AlbumAdapter extends ArrayAdapter<SongDetail> {
		public AlbumAdapter(Context context, List<SongDetail> items) {
			super(context, 0, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TrackView view;
			if (convertView == null) {
				view = new TrackView(getContext());
			} else {
				view = (TrackView) convertView;
			}

			final SongDetail song = getItem(position);;
			view.setTitle(song.title);
			view.setArtist(song.displayartist);
			view.setDuration(song.duration);
			view.setTrackNumber(song.track);
			return view;
		}
	}

	@Override
	public CharSequence getTitle() {
		return album == null? "" : album.title;
	}
}
