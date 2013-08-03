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
import java.net.URL;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.squareup.picasso.UrlConnectionLoader;

public class BasicAuthLoader extends UrlConnectionLoader {

	public BasicAuthLoader(Context context) {
		super(context);
	}

	@Override
    protected HttpURLConnection openConnection(String path) throws IOException {
		Log.d("Loader", "called");
        HttpURLConnection c = (HttpURLConnection)  new URL(path).openConnection();
        c.setRequestProperty("Authorization", "Basic " +
                Base64.encodeToString("xbmc:xbmc".getBytes(), Base64.NO_WRAP));
        return c;
    }

}
