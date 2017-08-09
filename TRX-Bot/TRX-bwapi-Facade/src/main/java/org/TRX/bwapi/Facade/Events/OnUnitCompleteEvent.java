package org.TRX.bwapi.Facade.Events;

import java.awt.Event;

import bwapi.Unit;

public class OnUnitCompleteEvent extends Event {
	private static final long serialVersionUID = 8239460470368478291L;

	private Unit completedUnit;

	public OnUnitCompleteEvent(Object target, int id, Object arg, Unit unit) {
		super(target, id, arg);
		this.completedUnit = unit;
	}

	public Unit getCompletedUnit() {
		return completedUnit;
	}
}
