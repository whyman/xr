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

import org.xbmc.android.jsonrpc.io.ConnectionManager;

public interface LoadableFragment {
	public void setConnectionManager(ConnectionManager cm);
	public void load();

	public interface ConnectionManagerProvider {
		public static final String ARGUMENTS_KEY = "__CONNECTIONMANAGERPROVIDER__";
		ConnectionManager getConnectionManager();
	}
}
