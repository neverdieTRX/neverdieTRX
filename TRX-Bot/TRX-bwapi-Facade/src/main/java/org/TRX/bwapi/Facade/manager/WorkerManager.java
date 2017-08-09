package org.TRX.bwapi.Facade.manager;

import java.util.List;

import javax.inject.Inject;

import org.TRX.bwapi.Facade.GameListener;

import bwapi.Order;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;

public class WorkerManager {

	@Inject
	GameListener gameListener;

	public void update() {
		this.handelWorker();
	}

	public Unit getFreeWorker() {
		List<Unit> myUnits = this.gameListener.getSelf().getUnits();
		for (Unit myUnit : myUnits) {
			if (myUnit.getType() == UnitType.Terran_SCV) {
				if (myUnit.isConstructing() == false && myUnit.isRepairing() == false) {
					return myUnit;
				}
			}
		}
		return myUnits.get(0);
	}

	private void handelWorker() {
		// iterate through my units
		for (Unit myUnit : this.gameListener.getSelf().getUnits()) {

			// if there's enough minerals, train an SCV
			if (myUnit.getType() == UnitType.Terran_Command_Center && myUnit.isIdle()
					&& this.gameListener.getSelf().minerals() >= 50) {
				myUnit.train(UnitType.Terran_SCV);
			}

			if (myUnit.getType().isWorker() && myUnit.isIdle()
					&& myUnit.getOrder().equals(Order.PlaceBuilding) == false) {
				Unit closestMineral = null;
				double distance = 100000;
				// find the closest mineral
				for (Unit neutralUnit : this.gameListener.getGame().neutral().getUnits()) {
					if (neutralUnit.getType().isMineralField()) {
						if (BWTA.getGroundDistance(myUnit.getTilePosition(),
								neutralUnit.getTilePosition()) < distance) {
							distance = BWTA.getGroundDistance(myUnit.getTilePosition(), neutralUnit.getTilePosition());
							closestMineral = neutralUnit;
						}

						//
						// if (closestMineral == null
						// || myUnit.getDistance(neutralUnit) <
						// myUnit.getDistance(closestMineral)) {
						// closestMineral = neutralUnit;
						// }
					}
				}
				// if a mineral patch was found, send the drone to gather it
				if (closestMineral != null) {
					myUnit.gather(closestMineral, false);
				}
			}
		}

	}

}
