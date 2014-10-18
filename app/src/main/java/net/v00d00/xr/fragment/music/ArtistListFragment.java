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
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.v00d00.xr.AsyncCallback;
import net.v00d00.xr.R;
import net.v00d00.xr.fragment.AbstractXRFragment;
import net.v00d00.xr.model.ArtistDetail;
import net.v00d00.xr.view.CoverView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistListFragment extends AbstractXRFragment {

	private Parcelable state;
	private GridView gridView;
	private AlbumListAdapter adapter;

	public interface ArtistListProvider {

	}

	public ArtistListFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (adapter == null)
			adapter = new AlbumListAdapter(getActivity(), new ArrayList<ArtistDetail>());

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
		Log.d("Artists", "load called");

		Map<String, Object> params = new HashMap<>();
		params.put("properties", Arrays.asList("thumbnail"));
		Map<String, Object> sort = new HashMap<>();
		sort.put("order", "ascending");
		sort.put("method", "artist");
		params.put("sort", sort);

		requestData("AudioLibrary.GetArtists", params, new AsyncCallback() {
			@Override
			public void onSuccess(Object result) {
				Log.d("Artists", "success");
				JSONObject obj = (JSONObject) result;
				List<ArtistDetail> artists = new ArrayList<>();

				JSONArray albums = (JSONArray) obj.get("artists");
				for (Object artistObj : albums) {
					Map<String, Object> album = (Map<String, Object>) artistObj;

					ArtistDetail detail = new ArtistDetail();
					detail.name = (String) album.get("label");
					detail.id = (Long) album.get("artistid");
					detail.thumbnail = (String) album.get("thumbnail");

					artists.add(detail);
				}

				adapter.addAll(artists);
				adapter.notifyDataSetChanged();

			}

			@Override
			public void onFailure(String error) {

			}
		});
	}

	private static class AlbumListAdapter extends ArrayAdapter<ArtistDetail> {
		public AlbumListAdapter(Context context, List<ArtistDetail> items) {
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

			final ArtistDetail artist = getItem(position);;
			view.setPosition(position);
			view.setTitle(artist.name);
			view.setThumbnailPath(artist.thumbnail);

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
		return "Artists";
	}
}
