package net.v00d00.xr.events;

import net.v00d00.xr.model.AlbumDetail;

public class AlbumSelectedEvent {

	private AlbumDetail album;

	public AlbumSelectedEvent(AlbumDetail album) {
		this.album = album;
	}

	public AlbumDetail getAlbum() {
		return this.album;
	}
}
