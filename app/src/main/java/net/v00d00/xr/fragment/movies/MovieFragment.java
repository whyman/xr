package net.v00d00.xr.fragment.movies;

import java.util.ArrayList;

import net.v00d00.xr.R;
import net.v00d00.xr.fragment.AbstractXRFragment;
import net.v00d00.xr.fragment.XRPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MovieFragment extends Fragment {

	XRPagerAdapter adapter = null;
	ViewPager pager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pager, container, false);

		pager = (ViewPager) view.findViewById(R.id.pager);

		if (adapter == null) {
			adapter = new XRPagerAdapter(getChildFragmentManager());
			ArrayList<AbstractXRFragment> fl = new ArrayList<AbstractXRFragment>();

			MovieListFragment albumListFragment = new MovieListFragment();
			fl.add(albumListFragment);

			GenreListFragment artistListFragment = new GenreListFragment();
			fl.add(artistListFragment);

			adapter.setFragmentList(fl);
		}
		pager.setAdapter(adapter);

		return view;
	}
}
