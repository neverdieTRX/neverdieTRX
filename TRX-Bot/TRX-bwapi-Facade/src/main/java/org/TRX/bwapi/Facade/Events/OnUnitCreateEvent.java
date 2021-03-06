package org.TRX.bwapi.Facade.Events;

import java.awt.Event;

import bwapi.Unit;

public class OnUnitCreateEvent extends Event {

	private static final long serialVersionUID = 8239460470368478291L;

	private Unit createdUnit;

	public OnUnitCreateEvent(Object target, int id, Object arg, Unit unit) {
		super(target, id, arg);
		this.createdUnit = unit;
	}

	public Unit getCreatedUnit() {
		return createdUnit;
	}
}
