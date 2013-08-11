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

package net.v00d00.xr.auth;
import net.v00d00.xr.R;

import org.xbmc.android.jsonrpc.config.HostConfig;

import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;

public class XRAuthenticatorActivity extends AccountAuthenticatorActivity {
	public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
	public static final String PARAM_HOST_CONFIG = "hostconfig";

	private AccountManager accountManager;

	private HostConfig config;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		accountManager = AccountManager.get(getApplicationContext());
	}

	@Override
	protected void onStart() {
		super.onStart();

		setContentView(R.layout.auth_layout);
	}
}
