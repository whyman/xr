package net.v00d00.xr.events;

import net.v00d00.xr.websocket.XRService;

/**
 * Created by ian on 09/10/14.
 */
public class ServiceAvailableEvent {

	private XRService service;

	public ServiceAvailableEvent(XRService service) {
		this.service = service;
	}

	public XRService getService() {
		return service;
	}
}
