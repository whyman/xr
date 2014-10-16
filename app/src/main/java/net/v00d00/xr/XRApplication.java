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

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.squareup.picasso.Picasso;

import net.v00d00.xr.websocket.XRService;

import java.util.concurrent.Executors;

public class XRApplication extends Application {
	private Picasso picasso;

	@Override
	public void onCreate() {
		super.onCreate();

		picasso = new Picasso.Builder(getApplicationContext())
				.downloader(new BasicAuthLoader(getApplicationContext()))
				.executor(Executors.newSingleThreadExecutor())
				.indicatorsEnabled(true)
				.build();

		Intent intent = new Intent(this, XRService.class);
		startService(intent);
	}

	public Picasso getPicasso() {
		return picasso;
	}

	public static XRApplication getApplication(Context context) {
		if (context == null)
			return null;
		return (XRApplication) context.getApplicationContext();
	}
}
