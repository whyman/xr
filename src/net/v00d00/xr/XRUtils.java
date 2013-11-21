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

import android.content.Context;
import android.content.res.Configuration;

public class XRUtils {
	public static CharSequence formatSeconds(int totalSecs) {
		int hours = totalSecs / 3600;
		int minutes = (totalSecs % 3600) / 60;
		int seconds = totalSecs % 60;

		StringBuilder sb = new StringBuilder();
		if (hours > 0) {
			sb.append(String.format("%02d", hours));
			sb.append(":");
		}
		sb.append(String.format("%02d", minutes));
		sb.append(":");
		sb.append(String.format("%02d", seconds));
		return sb.toString();
	}

	public static boolean isTablet(Context context) {
		if (context.getResources().getBoolean(R.bool.is10))
			return true;
		if (context.getResources().getBoolean(R.bool.is7)) {
			if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
				return true;
		}
		return false;
	}
}
