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

package net.v00d00.xr.fragment.music;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.v00d00.xr.AsyncCallback;
import net.v00d00.xr.R;
import net.v00d00.xr.events.AlbumSelectedEvent;
import net.v00d00.xr.fragment.AbstractXRFragment;
import net.v00d00.xr.model.AlbumDetail;
import net.v00d00.xr.model.SongDetail;
import net.v00d00.xr.view.AspectRatioImageView;
import net.v00d00.xr.view.TrackView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumDetailFragment extends AbstractXRFragment implements OnItemClickListener {

	private ListView trackList;
	private AlbumAdapter adapter;
	private AlbumDetail album;

	private View header;
	private AspectRatioImageView headerImg;
	private TextView headerArtist;
	private TextView headerAlbum;

	public void onEvent(AlbumSelectedEvent event) {
		setAlbum(event.getAlbum());
		updateAlbum();
		load();
	}

	public void setAlbum(AlbumDetail album) {
		Log.d("AlbumDetailFragment", Long.toString(album.id));

		this.album = album;
		state = album;
	}

	@Override
	public boolean shouldLoad() {
		return (album != null);
	}

	@Override
	public void load() {

		Log.d("AlbumDetailFragment", "load called");

		Map<String, Object> params = new HashMap<>();
		params.put("properties", Arrays.asList( "title", "track", "displayartist", "duration"));
		Map<String, Long> filter = new HashMap<>();
		filter.put("albumid", album.id);
		params.put("filter", filter);

		requestData("AudioLibrary.GetSongs", params, new AsyncCallback() {
			@Override
			public void onSuccess(Object result) {
				JSONObject obj = (JSONObject) result;
				List<SongDetail> songs = new ArrayList<>();

				JSONArray albums = (JSONArray) obj.get("songs");
				for (Object songObj : albums) {
					Map<String, Object> album = (Map<String, Object>) songObj;

					SongDetail detail = new SongDetail();
					detail.title = (String) album.get("title");
					detail.id = (Long) album.get("songid");
					detail.displayartist = (String) album.get("displayartist");
					detail.duration = (Long) album.get("duration");
					detail.track = (Long) album.get("track");

					songs.add(detail);
				}

				SongDetail song = new SongDetail();
				Log.d("songs", result.toString());

				adapter.clear();
				adapter.addAll(songs);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(String error) {

			}
		});

        /*
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

		    		}
		    	});
		    }
		    public void onError(int code, String message, String hint) {
		        Log.d("TEST", "Error " + code + ": " + message);
		    }
		});
		*/
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null)
			album = savedInstanceState.getParcelable(AbstractXRFragment.STATE_KEY);
		if (adapter == null)
			adapter = new AlbumAdapter(getActivity(), new ArrayList<SongDetail>());
		else {
			adapter.clear();
			adapter.notifyDataSetChanged();
		}

		if (trackList == null)
			trackList = (ListView) inflater.inflate(R.layout.fragment_album, container, false);
		else
			((ViewGroup) trackList.getParent()).removeView(trackList);

		if (header == null) {
			header = inflater.inflate(R.layout.track_list_header, trackList, false);
			trackList.addHeaderView(header);
		}

		headerImg = (AspectRatioImageView) header.findViewById(R.id.album_cover);
		headerImg.setImageDrawable(null);
		headerArtist = (TextView) header.findViewById(R.id.album_artist);
		headerAlbum = (TextView) header.findViewById(R.id.album_name);


		trackList.setAdapter(adapter);
		trackList.setOnItemClickListener(this);
		registerForContextMenu(trackList);

		updateAlbum();

		return trackList;
	}

	private void updateAlbum() {
		headerImg.setThumbnailPath(album.thumbnail);
		headerArtist.setText(album.displayartist);
		headerAlbum.setText(album.title);
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

			final SongDetail song = getItem(position);
			view.setTitle(song.title);
			view.setArtist(song.displayartist);
			view.setDuration(song.duration);
			view.setTrackNumber(song.track);
			return view;
		}
	}

	@Override
	public CharSequence getTitle() {
		return null;// album == null? "" : album.title;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
		final SongDetail detail = adapter.getItem(pos - 1);

		//PlaylistHandler.playAlbum(album.id, 0);

		Map<String, Object> params = new HashMap<>();
		Map<String, Long> songitem = new HashMap<>();
		songitem.put("songid", detail.id);
		params.put("item", songitem);

		requestData("Player.Open", params, new AsyncCallback() {
			@Override
			public void onSuccess(Object result) {

			}

			@Override
			public void onFailure(String error) {

			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getActivity().getMenuInflater();
	    inflater.inflate(R.menu.album_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
        /*
		switch (item.getItemId()) {
		case R.id.album_context_play_album:
			PlaylistHandler.playAlbum(getConnectionManager(), new Albumid(album.albumid), 0);
			break;
		case R.id.album_context_play_album_from_here:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		    PlaylistHandler.playAlbum(getConnectionManager(), new Albumid(album.albumid), info.position != 0 ? info.position - 1 : 0);
		    break;
		default:
		    */
			return super.onContextItemSelected(item);
		//}
		//return true;
	}

}
