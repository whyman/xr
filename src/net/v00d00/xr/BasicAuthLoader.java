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

import java.io.IOException;
import java.net.HttpURLConnection;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.squareup.picasso.UrlConnectionDownloader;

public class BasicAuthLoader extends UrlConnectionDownloader {

	private String authId;
	Context context;

	public BasicAuthLoader(Context context) {
		super(context);
		this.context = context;
	}

	private String getBasicAuthId() {
		if (authId == null) {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
			String user = sharedPref.getString("pref_user", "");
			String pass = sharedPref.getString("pref_pass", "");

			authId = "Basic " + Base64.encodeToString((user + ":" + pass).getBytes(), Base64.NO_WRAP);
		}

		return authId;
	}

	@Override
	protected HttpURLConnection openConnection(Uri path) throws IOException {
		HttpURLConnection c = super.openConnection(path);
		c.setRequestProperty("Authorization", getBasicAuthId());
		return c;
	}

}
