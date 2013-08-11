package net.v00d00.xr;


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
}
