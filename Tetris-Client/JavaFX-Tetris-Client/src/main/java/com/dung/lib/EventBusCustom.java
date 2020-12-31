package com.dung.lib;
import com.google.common.eventbus.EventBus;

public abstract class EventBusCustom {
	private static EventBus eventBus = new EventBus("main");
	
	public void attach(Object service) {
		eventBus.register(service);
	}
	
	public void detach(Object service) {
		eventBus.unregister(service);
	}
	
	public void postEvent(Object event) {
		eventBus.post(event);
	}
	
	public void exit() {
		System.exit(0);
	}
}
