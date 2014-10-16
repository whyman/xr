package net.v00d00.xr.events;

/**
 * Created by ian on 09/10/14.
 */
public class RequestCompleteEvent {
	public String id;
	public Object data;

	public RequestCompleteEvent(String id, Object data) {
		this.id = id;
		this.data = data;
	}
}
