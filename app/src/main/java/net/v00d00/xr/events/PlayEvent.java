package net.v00d00.xr.events;

/**
 * Created by ian on 11/10/14.
 */
public class PlayEvent {
	public String type;
	public Long id;

	public PlayEvent(String type, Long id) {
		this.type = type;
		this.id = id;
	}
}
