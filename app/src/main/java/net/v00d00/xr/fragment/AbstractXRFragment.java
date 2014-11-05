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

package net.v00d00.xr.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;

import net.v00d00.xr.AsyncCallback;
import net.v00d00.xr.events.RequestCompleteEvent;
import net.v00d00.xr.events.RequestEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;

public abstract class AbstractXRFragment extends Fragment {

	public static final String STATE_KEY = "__state__";

	protected EventBus eventBus;
	private boolean shouldLoad = false;

	private Map<String, AsyncCallback> myRequests = new HashMap<>();

	protected Parcelable state;

	protected abstract void load();
	public abstract CharSequence getTitle();

	public boolean shouldLoad() {
		return (state == null);
	}

	@Override
	public void onStart() {
		super.onStart();
		eventBus = EventBus.getDefault();
		eventBus.registerSticky(this);

		maybeLoad();
	}

	protected void maybeLoad() {
		if (state == null)
			Log.d("AbstractXRFragment", "Empty state");
		if (shouldLoad()) {
			Log.d("AbstractXRFragment", "Calling load");
			load();
		} else {
			Log.d("AbstractXRFragment", "Not calling load.");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (state != null)
			outState.putParcelable(STATE_KEY, state);
	}

	@Override
	public void onStop() {
		super.onStop();
		eventBus.unregister(this);
	}

	protected void requestData(String method, AsyncCallback callback) {
		String id = UUID.randomUUID().toString();
		myRequests.put(id, callback);
		eventBus.post(new RequestEvent(id, method));
	}

	protected void requestData(String method, List<Object> params, AsyncCallback callback) {
		String id = UUID.randomUUID().toString();
		myRequests.put(id, callback);
		eventBus.post(new RequestEvent(id, method, params));
	}

	protected void requestData(String method, Map<String, Object> namedParams, AsyncCallback callback) {
		String id = UUID.randomUUID().toString();
		myRequests.put(id, callback);
		Log.d("requestData", eventBus.toString());
		eventBus.post(new RequestEvent(id, method, namedParams));
	}

	protected void requestData(String method, Map<String, Object> namedParams) {
		String id = UUID.randomUUID().toString();
		Log.d("requestData", eventBus.toString());
		eventBus.post(new RequestEvent(id, method, namedParams));
	}

	public void onEventMainThread(RequestCompleteEvent event) {
		if (event != null && event.id != null && myRequests.containsKey(event.id)) {
			AsyncCallback cb = myRequests.get(event.id);
			cb.onSuccess(event.data);
		}
	}
}
