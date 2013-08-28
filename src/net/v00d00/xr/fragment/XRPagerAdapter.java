package net.v00d00.xr.fragment;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class XRPagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<AbstractXRFragment> fragments = new ArrayList<AbstractXRFragment>();

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
