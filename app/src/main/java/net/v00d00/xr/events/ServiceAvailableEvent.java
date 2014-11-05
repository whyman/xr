package net.v00d00.xr.events;

import net.v00d00.xr.services.WebsocketService;

/**
 * Created by ian on 09/10/14.
 */
public class ServiceAvailableEvent {

	private WebsocketService service;

	public ServiceAvailableEvent(WebsocketService service) {
		this.service = service;
	}

	public WebsocketService getService() {
		return service;
	}
}
