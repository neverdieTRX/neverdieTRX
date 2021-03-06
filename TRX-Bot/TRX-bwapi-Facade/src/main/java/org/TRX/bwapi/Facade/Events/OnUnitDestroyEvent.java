package org.TRX.bwapi.Facade.Events;

import java.awt.Event;

import bwapi.Unit;

public class OnUnitDestroyEvent extends Event {

	private static final long serialVersionUID = 8239460470368478291L;

	private Unit destroyedUnit;

	public OnUnitDestroyEvent(Object target, int id, Object arg, Unit unit) {
		super(target, id, arg);
		this.destroyedUnit = unit;
	}

	public Unit getDestroyedUnit() {
		return destroyedUnit;
	}
}
