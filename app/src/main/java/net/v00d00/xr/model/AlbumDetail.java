package net.v00d00.xr.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AlbumDetail implements Parcelable {
	public String title;
	public Long id;
	public String thumbnail;
	public String displayartist;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeLong(id);
		dest.writeString(thumbnail);
		dest.writeString(displayartist);
	}
}
