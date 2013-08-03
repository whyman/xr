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

import java.io.File;
import java.io.IOException;

import net.v00d00.xr.fragment.AlbumFragment;
import net.v00d00.xr.fragment.AlbumListFragment;
import net.v00d00.xr.fragment.LoadableFragment;

import org.xbmc.android.jsonrpc.api.model.AudioModel.AlbumDetail;
import org.xbmc.android.jsonrpc.io.ConnectionManager;

import android.app.ActionBar;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class HomeActivity extends SlidingFragmentActivity implements LoadableFragment.ConnectionManagerProvider, AlbumListFragment.Provider {

	LinearLayout layout;
	JsonRPC jsonrpc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_pager);

		layout = (LinearLayout) findViewById(R.id.pager);

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

		jsonrpc = new JsonRPC(getApplicationContext());

		try {
			File httpCacheDir = new File(getApplication().getCacheDir(), "http");
			long httpCacheSize = 50 * 1024 * 1024; // 50 MiB
			HttpResponseCache.install(httpCacheDir, httpCacheSize);
		} catch (IOException e) {
			Log.i("XR", "HTTP response cache installation failed:" + e);
		}

		showAlbums();
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
        default:
            return super.onOptionsItemSelected(item);
		}
    }

	@Override
	public ConnectionManager getConnectionManager() {
		return jsonrpc.getConnectionManager();
	}

	private void replaceFragment(Fragment f) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.pager, f);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	private void showAlbums() {
		AlbumListFragment f = new AlbumListFragment();
		f.setConnectionManager(getConnectionManager());
		f.setProvider(this);
		replaceFragment(f);
	}

	@Override
	public void showAlbumListing(AlbumDetail album) {
		AlbumFragment newFragment = new AlbumFragment();
		newFragment.setConnectionManager(getConnectionManager());
		newFragment.setAlbum(album);
		replaceFragment(newFragment);
	}

}
