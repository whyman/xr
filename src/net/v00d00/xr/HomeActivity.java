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

import net.v00d00.xr.fragment.AbstractXRFragment;
import net.v00d00.xr.fragment.PlayingBarFragment;
import net.v00d00.xr.fragment.SideMenuFragment;
import net.v00d00.xr.fragment.movies.MovieFragment;
import net.v00d00.xr.fragment.music.AlbumDetailFragment;
import net.v00d00.xr.fragment.music.AlbumListFragment;
import net.v00d00.xr.fragment.music.MusicFragment;
import net.v00d00.xr.fragment.tv.TVFragment;

import org.xbmc.android.jsonrpc.api.model.AudioModel.AlbumDetail;
import org.xbmc.android.jsonrpc.io.ConnectionManager;

import android.app.ActionBar;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

public class HomeActivity extends SlidingFragmentActivity implements
			AbstractXRFragment.ConnectionManagerProvider,
			AlbumListFragment.Provider,
			PlayingBarFragment.Provider,
			SideMenuFragment.Provider {

	SlidingUpPanelLayout layout;
	JsonRPC jsonrpc;

	MusicFragment musicFragment;
	MovieFragment movieFragment;
	TVFragment tvFragment;

	PlayingBarFragment playingBarFragment;
	SlidingMenu sm;
	SideMenuFragment menuFragment;

	FrameLayout rightPane;
	FrameLayout leftPane;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.base);

		layout = (SlidingUpPanelLayout) findViewById(R.id.pager_layout);
		rightPane = (FrameLayout) findViewById(R.id.right_pane);
		leftPane = (FrameLayout) findViewById(R.id.left_pane);

		jsonrpc = new JsonRPC(getApplicationContext());

		// set the Behind View
		setBehindContentView(R.layout.side_menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();

			menuFragment = new SideMenuFragment();
			t.replace(R.id.side_menu_frame, menuFragment);

			playingBarFragment = new PlayingBarFragment();
			jsonrpc.getConnectionManager().registerObserver(playingBarFragment);
			t.replace(R.id.bottom_pane, playingBarFragment).commit();

			displayMusic();
		} else {
			menuFragment = (SideMenuFragment) getSupportFragmentManager().findFragmentById(R.id.side_menu_frame);
			playingBarFragment = (PlayingBarFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_pane);
		}

		setSlidingActionBarEnabled(false);

		// customize the SlidingMenu
		sm = getSlidingMenu();
		sm.setBehindWidthRes(R.dimen.side_menu_width);
		sm.setFadeEnabled(true);
		sm.setFadeDegree(0.9f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setBehindScrollScale(0.5f);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setShadowWidthRes(R.dimen.side_menu_shadow_width);

		ActionBar ab = getActionBar();
		if (ab != null){
			ab.setDisplayHomeAsUpEnabled(true);
			ab.setDisplayShowTitleEnabled(false);
		}
	}

	@Override
	protected void onStop() {
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
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
			toggle();
			return true;
        case R.id.action_settings:
        	showSettings();
        	return true;
        default:
            return super.onOptionsItemSelected(item);
		}
    }

	private void showSettings() {
		 getFragmentManager().beginTransaction()
         .replace(R.id.left_pane, new SettingsFragment())
         .addToBackStack(null).commit();
	}

	@Override
	public ConnectionManager getConnectionManager() {
		return jsonrpc.getConnectionManager();
	}

	@Override
	public void showAlbumListing(AlbumDetail album) {
		AlbumDetailFragment af = new AlbumDetailFragment();
		af.setAlbum(album);
		setRightPane(af);
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
		if (XRUtils.isTablet(this))
			fm.beginTransaction().replace(R.id.right_pane, f).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
		else
			fm.beginTransaction().replace(R.id.left_pane, f).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

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
		if (sm != null)
			sm.showContent();
	}
}
