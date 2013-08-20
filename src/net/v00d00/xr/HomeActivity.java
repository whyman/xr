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

import java.util.ArrayList;

import net.v00d00.xr.fragment.AbstractXRFragment;
import net.v00d00.xr.fragment.AlbumFragment;
import net.v00d00.xr.fragment.AlbumListFragment;
import net.v00d00.xr.fragment.ArtistListFragment;
import net.v00d00.xr.fragment.PlayingBarFragment;
import net.v00d00.xr.fragment.SongListFragment;

import org.xbmc.android.jsonrpc.api.model.AudioModel.AlbumDetail;
import org.xbmc.android.jsonrpc.io.ConnectionManager;

import android.app.ActionBar;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.provider.MediaStore.Audio.PlaylistsColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.viewpagerindicator.TitlePageIndicator;

public class HomeActivity extends SlidingFragmentActivity implements AbstractXRFragment.ConnectionManagerProvider, AlbumListFragment.Provider, PlayingBarFragment.Provider {

	SlidingUpPanelLayout layout;
	JsonRPC jsonrpc;

	ViewPager pager;
	TitlePageIndicator indicator;
	XRPagerAdapter adapter = null;
	PlayingBarFragment playingBar;

	FrameLayout rightPane;

	AbstractXRFragment rightHand;

	private class XRPagerAdapter extends FragmentStatePagerAdapter {

		private ArrayList<AbstractXRFragment> fragments;

		public XRPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public void setFragmentList(ArrayList<AbstractXRFragment> list) {
			fragments = list;
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return fragments.get(position).getTitle();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.view_pager);

		layout = (SlidingUpPanelLayout) findViewById(R.id.pager_layout);
		pager = (ViewPager) findViewById(R.id.pager);
		indicator = (TitlePageIndicator) findViewById(R.id.titles);
		rightPane = (FrameLayout) findViewById(R.id.right_pane);

		jsonrpc = new JsonRPC(getApplicationContext());

		if (adapter == null) {
			adapter = new XRPagerAdapter(getSupportFragmentManager());
			ArrayList<AbstractXRFragment> fl = new ArrayList<AbstractXRFragment>();
			AlbumListFragment albumListFragment = new AlbumListFragment();
			albumListFragment.setProvider(this);
			fl.add(albumListFragment);

			ArtistListFragment artistListFragment = new ArtistListFragment();
			fl.add(artistListFragment);

			SongListFragment songListFragment = new SongListFragment();
			fl.add(songListFragment);

			adapter.setFragmentList(fl);
		}
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);

		// set the Behind View
		setBehindContentView(R.layout.side_menu);
		setSlidingActionBarEnabled(true);

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindWidthRes(R.dimen.side_menu_width);
		sm.setFadeEnabled(true);
		sm.setFadeDegree(0.8f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setBehindScrollScale(0.5f);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setShadowWidthRes(R.dimen.side_menu_shadow_width);

		ActionBar ab = getActionBar();
		if (ab != null){
			ab.setDisplayHomeAsUpEnabled(true);
			ab.setDisplayShowTitleEnabled(false);
		}

		PlayingBarFragment pbf = new PlayingBarFragment();
		jsonrpc.getConnectionManager().registerObserver(pbf);
		getSupportFragmentManager().beginTransaction().add(R.id.bottom_pane, pbf).commit();
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
        case R.id.action_music:
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
         .replace(R.id.pager_layout, new SettingsFragment())
         .addToBackStack(null).commit();
	}

	@Override
	public ConnectionManager getConnectionManager() {
		return jsonrpc.getConnectionManager();
	}

	@Override
	public void showAlbumListing(AlbumDetail album) {
		AlbumFragment af = new AlbumFragment();
		af.setAlbum(album);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 640, getResources().getDisplayMetrics()),
				LinearLayout.LayoutParams.MATCH_PARENT,
				1.0f
			);
		params.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
		rightPane.setLayoutParams(params);

		if (rightHand == null)
			getSupportFragmentManager().beginTransaction().add(R.id.right_pane, af).commit();
		else
			getSupportFragmentManager().beginTransaction().replace(R.id.right_pane, af).commit();

		rightHand = af;

	}

	@Override
	public void setDragView(View view) {
		layout.setDragView(view);
	}
}
