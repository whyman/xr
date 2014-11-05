package net.v00d00.xr.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Message;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Notification;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

import net.v00d00.xr.events.PlayEvent;
import net.v00d00.xr.events.RequestCompleteEvent;
import net.v00d00.xr.events.RequestEvent;

import java.util.Map;

import de.greenrobot.event.EventBus;

public class WebsocketService extends Service {

	EventBus eventBus;
	WebSocket socket;

	public class LocalBinder extends Binder {
		public WebsocketService getService() {
			return WebsocketService.this;
		}
	}

	private final IBinder binder = new LocalBinder();

	@Override
	public void onCreate() {
		Log.d("websocket", "onCreate");
		eventBus = EventBus.getDefault();
		eventBus.register(this);

		AsyncHttpClient client = AsyncHttpClient.getDefaultInstance();
		client.websocket("ws://192.168.1.100:9090/jsonrpc", "my-protocol", new AsyncHttpClient.WebSocketConnectCallback() {
			@Override
			public void onCompleted(Exception ex, WebSocket webSocket) {

				if (ex != null) {
					ex.printStackTrace();
					return;
				}

				socket = webSocket;
				Log.d("websocket", "connected");

				webSocket.setStringCallback(new WebSocket.StringCallback() {
					public void onStringAvailable(String s) {
						Log.d("websocket", "I got a string: " + s);
						try {
							JSONRPC2Message parsed = JSONRPC2Message.parse(s);

							if (parsed instanceof JSONRPC2Request) {
								Log.d("jsonrpc", "The message is a Request");
							}
							else if (parsed instanceof JSONRPC2Notification) {
								Log.d("jsonrpc", "The message is a Notification");
								JSONRPC2Notification notification = (JSONRPC2Notification) parsed;
								Log.d("jsonrcp:notification", notification.toJSONString());
								if (notification.getMethod().equals("Player.OnPlay")) {
									Map<String, Object> params = notification.getNamedParams();
									Map<String, Object> data = (Map<String, Object>) params.get("data");
									Map<String, Object> item = (Map<String, Object>) data.get("item");

									eventBus.post(new PlayEvent((String) item.get("type"), (Long) item.get("id")));
								}
							}
							else if (parsed instanceof JSONRPC2Response) {
								Log.d("jsonrpc", "The message is a Response");
								JSONRPC2Response response = (JSONRPC2Response) parsed;

								if (response.indicatesSuccess()) {
									Log.d("jsonrpc", "calling onSuccess");
									eventBus.post(new RequestCompleteEvent((String) response.getID(), response.getResult()));
								} else {
									Log.d("jsonrpc", "calling onError");
									//callback.onFailure(response.getError().getMessage());
								}
							}

						} catch (JSONRPC2ParseException e) {
							e.printStackTrace();
						}
					}
				});

				webSocket.setClosedCallback(new CompletedCallback() {
					@Override
					public void onCompleted(Exception ex) {
						Log.d("websocket", "completed");
					}
				});
			}
		});
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("websocket", "onstartcommand");
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

	public void onEvent(RequestEvent event) {
		JSONRPC2Request reqOut;
		if (event.namedParams != null)
			reqOut = new JSONRPC2Request(event.method, event.namedParams, event.id);
		else if (event.params != null)
			reqOut = new JSONRPC2Request(event.method, event.params, event.id);
		else
			reqOut = new JSONRPC2Request(event.method, event.id);
		if (socket != null)
			socket.send(reqOut.toJSONString());
	}
}
