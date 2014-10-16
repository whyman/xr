package net.v00d00.xr.events;

import java.util.List;
import java.util.Map;

/**
 * Created by ian on 09/10/14.
 */
public class RequestEvent {

	public String id;
	public String method;
	public List<Object> params;
	public Map<String, Object> namedParams;

	public RequestEvent(String id, String method) {
		this.id = id;
		this.method = method;
	}

	public RequestEvent(String id, String method, List<Object> params) {
		this.id = id;
		this.method = method;
		this.params = params;
	}

	public RequestEvent(String id, String method, Map<String, Object> namedParams) {
		this.id = id;
		this.method = method;
		this.namedParams = namedParams;
	}
}
