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

import net.v00d00.xr.R;
import net.v00d00.xr.fragment.AbstractXRFragment;
import net.v00d00.xr.view.FixedHeightRatioImageView;

import org.xbmc.android.jsonrpc.api.AbstractCall;
import org.xbmc.android.jsonrpc.api.call.VideoLibrary;
import org.xbmc.android.jsonrpc.api.model.VideoModel;
import org.xbmc.android.jsonrpc.api.model.VideoModel.MovieDetail;
import org.xbmc.android.jsonrpc.io.ApiCallback;

import android.os.Bundle;
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
import android.widget.TextView;

public class MovieDetailFragment extends AbstractXRFragment implements OnItemClickListener, OnMenuItemClickListener {

	private MovieDetail movie;

	private View header;
	private FixedHeightRatioImageView headerImg;
	private TextView headerArtist;
	private TextView headerAlbum;

	public void setMovie(MovieDetail movie) {
		this.movie = movie;
		state = movie;
	}

	@Override
	public void load() {
		final VideoLibrary.GetMovieDetails call = new VideoLibrary.GetMovieDetails(
				movie.movieid,
				VideoModel.MovieFields.ART,
				VideoModel.MovieFields.TITLE,
				VideoModel.MovieFields.DIRECTOR,
				VideoModel.MovieFields.MPAA,
				VideoModel.MovieFields.RUNTIME);

		getConnectionManager().call(call, new ApiCallback<MovieDetail>() {
		    public void onResponse(final AbstractCall<MovieDetail> call) {
		    	getActivity().runOnUiThread(new Runnable() {
		    		public void run() {

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
		if (savedInstanceState != null)
			movie = savedInstanceState.getParcelable(AbstractXRFragment.STATE_KEY);

		if (header == null) {
			header = inflater.inflate(R.layout.track_list_header, container, false);
			headerImg = (FixedHeightRatioImageView) header.findViewById(R.id.album_cover);
			headerArtist = (TextView) header.findViewById(R.id.album_artist);
			headerAlbum = (TextView) header.findViewById(R.id.album_name);
		}

		headerArtist.setText(movie.mpaa);
		headerAlbum.setText(movie.title);
		headerImg.setThumbnailPath(movie.thumbnail);

		return header;
	}

	@Override
	public CharSequence getTitle() {
		return movie == null? "" : movie.title;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add("Test 1").setOnMenuItemClickListener(this);
		menu.add("Test 2").setOnMenuItemClickListener(this);
		menu.add("Test 3").setOnMenuItemClickListener(this);
		menu.add("Test 4").setOnMenuItemClickListener(this);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {

		Log.d("MENU ITEM", item.toString());

		return false;
	}
}
