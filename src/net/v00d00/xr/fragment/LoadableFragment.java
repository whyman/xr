package net.v00d00.xr.fragment;

import org.xbmc.android.jsonrpc.io.ConnectionManager;

public interface LoadableFragment {
	public void setConnectionManager(ConnectionManager cm);
	public void load();

	public interface ConnectionManagerProvider {
		public static final String ARGUMENTS_KEY = "__CONNECTIONMANAGERPROVIDER__";
		ConnectionManager getConnectionManager();
	}
}
