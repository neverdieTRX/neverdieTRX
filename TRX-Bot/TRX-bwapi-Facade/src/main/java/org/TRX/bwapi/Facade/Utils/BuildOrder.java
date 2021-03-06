package org.TRX.bwapi.Facade.Utils;

import bwapi.Unit;
import bwapi.UnitType;

/**
 * Not an 'Build order' in a convetional sense but more a singel Order that was
 * given to an worker.
 * 
 * 
 * @author Turox
 *
 */
public class BuildOrder {

	// get framecount game.getFrameCount();

	private int timeOfOrder;
	private UnitType unitType;
	private Unit worker;

	public BuildOrder(int timeOfOrder, UnitType unitType, Unit worker) {
		this.timeOfOrder = timeOfOrder;
		this.unitType = unitType;
		this.worker = worker;
		System.out.println(this.worker);

	}

	public int getTimeOfOrder() {
		return timeOfOrder;
	}

	public void setTimeOfOrder(int timeOfOrder) {
		this.timeOfOrder = timeOfOrder;
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public void setunitType(UnitType unitType) {
		this.unitType = unitType;
	}

	public Unit getWorker() {
		return worker;
	}

	public void setWorker(Unit worker) {
		this.worker = worker;
	}

}
