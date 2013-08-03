package net.v00d00.xr;

import org.xbmc.android.jsonrpc.config.HostConfig;
import org.xbmc.android.jsonrpc.io.ConnectionManager;

import android.content.Context;


public class JsonRPC {
	private Context appcontext;
	private ConnectionManager cm;

	public JsonRPC(Context appContext) {
		this.appcontext = appContext;
		cm = new ConnectionManager(appContext, new HostConfig("192.168.1.100"));
	}

	public ConnectionManager getConnectionManager() {
		return cm;
	}
}
