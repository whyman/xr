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

import org.xbmc.android.jsonrpc.config.HostConfig;
import org.xbmc.android.jsonrpc.io.ConnectionManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


public class JsonRPC {
	private Context appcontext;
	private ConnectionManager cm;

	public JsonRPC(Context appContext) {
		this.appcontext = appContext;

		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(appContext);
		String address = sharedPref.getString("pref_host", "");
		String user = sharedPref.getString("pref_user", null);
		String pass = sharedPref.getString("pref_pass", null);
		int httpPort = Integer.parseInt(sharedPref.getString("pref_http", "0"));
		int tcpPort = Integer.parseInt(sharedPref.getString("pref_tcp", "0"));

		Log.d("XR", address + user + pass);

		cm = new ConnectionManager(appContext, new HostConfig(address, httpPort, tcpPort, user, pass));
	}

	public ConnectionManager getConnectionManager() {
		return cm;
	}
}
