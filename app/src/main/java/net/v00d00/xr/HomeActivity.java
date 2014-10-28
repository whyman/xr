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

package net.v00d00.xr;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.v00d00.xr.events.AlbumSelectedEvent;
import net.v00d00.xr.events.ServiceAvailableEvent;
import net.v00d00.xr.fragment.AbstractXRFragment;
import net.v00d00.xr.fragment.PlayingBarFragment;
import net.v00d00.xr.fragment.SideMenuFragment;
import net.v00d00.xr.fragment.movies.MovieDetailFragment;
import net.v00d00.xr.fragment.movies.MovieFragment;
import net.v00d00.xr.fragment.movies.MovieListFragment;
import net.v00d00.xr.fragment.music.AlbumDetailFragment;
import net.v00d00.xr.fragment.music.AlbumListFragment;
import net.v00d00.xr.fragment.music.MusicFragment;
import net.v00d00.xr.fragment.tv.TVFragment;
import net.v00d00.xr.model.AlbumDetail;
import net.v00d00.xr.model.MovieDetail;
import net.v00d00.xr.websocket.XRService;

import de.greenrobot.event.EventBus;

public class HomeActivity extends FragmentActivity implements
			PlayingBarFragment.Provider,
			SideMenuFragment.Provider,
			AlbumListFragment.Provider,
			MovieListFragment.Provider {

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			XRService.LocalBinder binder = (XRService.LocalBinder) service;
			xrService = binder.getService();
			isBound = true;
			eventBus.post(new ServiceAvailableEvent(xrService));
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			isBound = false;
		}
	};

	private EventBus eventBus;
	private XRService xrService;
	private boolean isBound = false;

	private SlidingUpPanelLayout layout;

	private MusicFragment musicFragment;
	private MovieFragment movieFragment;
	private TVFragment tvFragment;

	private PlayingBarFragment playingBarFragment;
	private SideMenuFragment navigationDrawerFragment;

	private FrameLayout rightPane;
	private FrameLayout leftPane;
	private DrawerLayout navigationDrawer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.base);

		layout = (SlidingUpPanelLayout) findViewById(R.id.pager_layout);
		rightPane = (FrameLayout) findViewById(R.id.right_pane);
		leftPane = (FrameLayout) findViewById(R.id.left_pane);
		navigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();

			navigationDrawerFragment = new SideMenuFragment();
			t.replace(R.id.navigation_drawer, navigationDrawerFragment);

			playingBarFragment = new PlayingBarFragment();
			t.replace(R.id.bottom_pane, playingBarFragment).commit();

			displayMusic();
		} else {
			playingBarFragment = (PlayingBarFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_pane);
		}

		layout.setPanelSlideListener(playingBarFragment);
		navigationDrawer.setDrawerShadow(R.drawable.drop_shadow, GravityCompat.START);

		ActionBar ab = getActionBar();
		if (ab != null){
			ab.setDisplayHomeAsUpEnabled(true);
			ab.setDisplayShowTitleEnabled(true);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		eventBus = EventBus.getDefault();
		eventBus.register(this);

		Intent intent = new Intent(this, XRService.class);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}

		eventBus.unregister(this);
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (navigationDrawer.isDrawerVisible(GravityCompat.START))
				navigationDrawer.closeDrawer(GravityCompat.START);
			else
				navigationDrawer.openDrawer(GravityCompat.START);
			return true;
        case R.id.action_settings:
        	Intent intent = new Intent(this, SettingsActivity.class);
        	startActivity(intent);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
		}
    }

	private void setRightPane(AbstractXRFragment f) {

		LinearLayout.LayoutParams params;

		Log.d("IS TABLET", Boolean.toString(XRUtils.isTablet(this)));

		if (XRUtils.isTablet(this)) {
			params = new LinearLayout.LayoutParams(
					(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 640, getResources().getDisplayMetrics()),
					LinearLayout.LayoutParams.MATCH_PARENT,
					1.0f
				);
		} else {
			rightPane.setVisibility(View.GONE);
			params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT,
					1.0f
			);
		}
		params.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

		rightPane.setLayoutParams(params);

		FragmentManager fm = getSupportFragmentManager();
		FragmentManager.enableDebugLogging(true);
		if (!f.isAdded()) {
			if (XRUtils.isTablet(this))
				fm.beginTransaction().replace(R.id.right_pane, f).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
			else {
				fm.beginTransaction().replace(R.id.left_pane, f).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
			}
		}

	}

	@Override
	public void setDragView(View view) {
		layout.setDragView(view);
	}

	@Override
	public void displayMusic() {
		if (musicFragment == null)
			musicFragment = new MusicFragment();
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.left_pane, musicFragment).commit();
		hideMenu();
	}

	@Override
	public void displayMovies() {
		if (movieFragment == null)
			movieFragment = new MovieFragment();
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.left_pane, movieFragment).commit();
		hideMenu();
	}

	@Override
	public void displayTVShows() {
		if (tvFragment == null)
			tvFragment = new TVFragment();
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.left_pane, tvFragment).commit();
		hideMenu();
	}

	@Override
	public void displayPhotos() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayRemote() {
		// TODO Auto-generated method stub

	}

	private void hideMenu() {
		//if (sm != null)
		//	sm.showContent();
	}

	@Override
	public void showAlbumListing(AlbumDetail album) {
		//meh
	}

	@Override
	public void showMovieDetail(MovieDetail movie) {
		MovieDetailFragment mf = new MovieDetailFragment();
		//mf.setMovie(movie);
		setRightPane(mf);
	}

	public void onEvent(AlbumSelectedEvent event) {
		Log.d("HomeActivity", "onEvent called");
		AlbumDetailFragment af = musicFragment.getAlbumDetailFragement();
		af.setAlbum(event.getAlbum());
		setRightPane(af);
	}
}
