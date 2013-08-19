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

import java.util.ArrayList;
import java.util.List;

import net.v00d00.xr.R;
import net.v00d00.xr.view.CoverView;

import org.xbmc.android.jsonrpc.api.AbstractCall;
import org.xbmc.android.jsonrpc.api.call.AudioLibrary;
import org.xbmc.android.jsonrpc.api.model.AudioModel;
import org.xbmc.android.jsonrpc.api.model.AudioModel.AlbumDetail;
import org.xbmc.android.jsonrpc.api.model.AudioModel.SongDetail;
import org.xbmc.android.jsonrpc.io.ApiCallback;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class SongListFragment extends AbstractXRFragment {

	private Parcelable state;
	private GridView gridView;
	private AlbumListAdapter adapter;

	public interface SongListProvider extends ConnectionManagerProvider {
		public void showAlbumListing(AlbumDetail album);
	}

	public SongListFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (adapter == null)
			adapter = new AlbumListAdapter(getActivity(), new ArrayList<SongDetail>());

		View rootView = inflater.inflate(R.layout.fragment_albums, container, false);
		gridView = (GridView) rootView.findViewById(R.id.album_grid);

		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
				/*AlbumDetail detail = adapter.getItem(pos);

				Context context = getActivity().getApplicationContext();
				String text = "Album Id: " + Integer.toString(detail.albumid);
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();

				provider.showAlbumListing(detail);*/
			}
		});


		return rootView;
	}

	@Override
	public void load() {
		final AudioLibrary.GetSongs call = new AudioLibrary.GetSongs(
				AudioModel.SongFields.ARTIST,
				AudioModel.SongFields.TITLE,
				AudioModel.SongFields.TRACK,
				AudioModel.SongFields.DISPLAYARTIST,
				AudioModel.SongFields.THUMBNAIL);

		// execute
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
		        Log.d("TEST", "Error " + code + ": " + message + hint);
		    }
		});
	}

	private static class AlbumListAdapter extends ArrayAdapter<SongDetail> {
		public AlbumListAdapter(Context context, List<SongDetail> items) {
			super(context, 0, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CoverView view;
			if (convertView == null) {
				view = new CoverView(getContext());
			} else {
				view = (CoverView) convertView;
			}

			final SongDetail song = getItem(position);;
			view.setPosition(position);
			view.setTitle(song.title + ": " + song.displayartist);
			view.setThumbnailPath(song.thumbnail);

			return view;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		state = gridView.onSaveInstanceState();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adapter != null)
			gridView.setAdapter(adapter);
		if (state != null)
			gridView.onRestoreInstanceState(state);
	}

	@Override
	public CharSequence getTitle() {
		return "Songs";
	}
}
