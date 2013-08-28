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
