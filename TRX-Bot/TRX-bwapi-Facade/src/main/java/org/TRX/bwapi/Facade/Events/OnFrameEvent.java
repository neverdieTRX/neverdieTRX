package org.TRX.bwapi.Facade.Events;

import java.awt.Event;

public class OnFrameEvent extends Event {

	private static final long serialVersionUID = -9221717959555034214L;

	public OnFrameEvent(Object target, int id, Object arg) {
		super(target, id, arg);
	}

}
