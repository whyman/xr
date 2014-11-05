package net.v00d00.xr.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import net.v00d00.xr.events.DoAlbumPlayEvent;
import net.v00d00.xr.events.RequestEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;

public class PlaylistService extends Service {

	EventBus eventBus;
	public class LocalBinder extends Binder {
		public PlaylistService getService() {
			return PlaylistService.this;
		}
	}

	private final IBinder binder = new LocalBinder();

	@Override
	public void onCreate() {
		eventBus = EventBus.getDefault();
		eventBus.register(this);
		Log.d("PlaylistService", "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		eventBus.unregister(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	protected void requestData(String method, Map<String, Object> namedParams) {

	}

	public void onEvent(DoAlbumPlayEvent event) {
		if (event.getOffset() == 0) {
			Map<String, Object> params = new HashMap<>();
			Map<String, Long> albumitem = new HashMap<>();
			albumitem.put("albumid", event.getId());
			params.put("item", albumitem);
			eventBus.post(new RequestEvent(UUID.randomUUID().toString(), "Player.Open", params));
		} else {
			// urgh
		}
	}
}
