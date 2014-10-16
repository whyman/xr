package net.v00d00.xr.fragment.music;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.v00d00.xr.R;
import net.v00d00.xr.fragment.AbstractXRFragment;
import net.v00d00.xr.fragment.XRPagerAdapter;

import java.util.ArrayList;

public class MusicFragment extends Fragment {

	private AlbumDetailFragment albumDetailFragment;

	XRPagerAdapter adapter = null;
	ViewPager pager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pager, container, false);

		pager = (ViewPager) view.findViewById(R.id.pager);

		if (adapter == null) {
			adapter = new XRPagerAdapter(getChildFragmentManager());
			ArrayList<AbstractXRFragment> fl = new ArrayList<AbstractXRFragment>();

			AlbumListFragment albumListFragment = new AlbumListFragment();
			fl.add(albumListFragment);

			ArtistListFragment artistListFragment = new ArtistListFragment();
			fl.add(artistListFragment);

			SongListFragment songListFragment = new SongListFragment();
			fl.add(songListFragment);

			adapter.setFragmentList(fl);
		}
		pager.setAdapter(adapter);

		return view;
	}

	public AlbumDetailFragment getAlbumDetailFragement() {
		if (albumDetailFragment == null)
			albumDetailFragment = new AlbumDetailFragment();
		return albumDetailFragment;
	}
}
