package net.v00d00.xr.events;

/**
 * Created by ian on 31/10/14.
 */
public class DoAlbumPlayEvent extends DoPlayEvent {
	private long offset = 0;

	public DoAlbumPlayEvent(long id) {
		super(id);
	}

	public DoAlbumPlayEvent(long id, long startOffset) {
		super(id);
		this.offset = startOffset;
	}

	public long getOffset() {
		return offset;
	}
}
