package net.v00d00.xr.events;

/**
 * Created by ian on 31/10/14.
 */
public class DoPlayEvent {
	private long id;

	public DoPlayEvent(long songid) {
		this.id = songid;
	}

	public long getId() {
		return id;
	}
}
