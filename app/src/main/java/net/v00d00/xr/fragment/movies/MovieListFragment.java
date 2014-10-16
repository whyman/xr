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

package net.v00d00.xr.fragment.movies;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import net.v00d00.xr.R;
import net.v00d00.xr.fragment.AbstractXRFragment;
import net.v00d00.xr.model.MovieDetail;
import net.v00d00.xr.view.CoverView;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends AbstractXRFragment implements OnItemClickListener, OnMenuItemClickListener {

	private GridView gridView;
	private MovieListAdapter adapter;

	public interface Provider {
		public void showMovieDetail(MovieDetail movie);
	}

	public MovieListFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (adapter == null)
			adapter = new MovieListAdapter(getActivity(), new ArrayList<MovieDetail>());

		View rootView = inflater.inflate(R.layout.fragment_albums, container, false);
		gridView = (GridView) rootView.findViewById(R.id.album_grid);

		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		registerForContextMenu(gridView);

		if (savedInstanceState != null) {
			Parcelable par = savedInstanceState.getParcelable(AbstractXRFragment.STATE_KEY);
			if (par != null)
				gridView.onRestoreInstanceState(par);
		}

		return rootView;
	}

	@Override
	public void load() {
        /*
		ListModel.Sort sort = new Sort(true, Method.TITLE, Order.ASCENDING);

		// create api call object
		final VideoLibrary.GetMovies call = new VideoLibrary.GetMovies(sort, VideoModel.MovieFields.TITLE, VideoModel.MovieFields.THUMBNAIL);

		// execute
		getConnectionManager().call(call, new ApiCallback<MovieDetail>() {
		    public void onResponse(final AbstractCall<MovieDetail> call) {
		    	getActivity().runOnUiThread(new Runnable() {
		    		public void run() {
				    	adapter.clear();
				        adapter.addAll(call.getResults());
				        adapter.notifyDataSetChanged();
		    		}
		    	});
		    }
			@Override
			public void onError(int code, String message, String hint) {
				// TODO Auto-generated method stub
			}
		});
		*/
	}

	private static class MovieListAdapter extends ArrayAdapter<MovieDetail> {
		public MovieListAdapter(Context context, List<MovieDetail> items) {
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

			final MovieDetail album = getItem(position);;
			view.setPosition(position);
			//view.setTitle(album.title);
			//view.setThumbnailPath(album.thumbnail, 1.4f);

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
		return "Movies";
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add("Play Album").setOnMenuItemClickListener(this);
		menu.add("Test 2").setOnMenuItemClickListener(this);
		menu.add("Test 3").setOnMenuItemClickListener(this);
		menu.add("Test 4").setOnMenuItemClickListener(this);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {

		Log.d("MENU ITEM", item.toString());

		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
		MovieDetail detail = adapter.getItem(pos);

		Context context = getActivity().getApplicationContext();
		//String text = "Movie Id: " + Integer.toString(detail.movieid);
		int duration = Toast.LENGTH_SHORT;

		//Toast toast = Toast.makeText(context, text, duration);
		//toast.show();

		//((Provider) getActivity()).showMovieDetail(detail);
	}

}
