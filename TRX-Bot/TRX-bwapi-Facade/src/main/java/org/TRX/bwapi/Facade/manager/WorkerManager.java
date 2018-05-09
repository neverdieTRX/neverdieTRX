package org.TRX.bwapi.Facade.manager;

import java.util.List;

import javax.inject.Inject;

import org.TRX.bwapi.Facade.GameListener;

import bwapi.Order;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;

/**
 * Handels everything worker related.
 *
 * @author marcus
 *
 */
public class WorkerManager {

	@Inject
	GameListener gameListener;

	public void update() {
		this.handelWorker();
	}

	public Unit getFreeWorker() {
		List<Unit> myUnits = this.gameListener.getSelf().getUnits();
		for (Unit myUnit : myUnits) {
			if (myUnit.getType() != UnitType.Terran_SCV) {
				continue;
			}
			if (myUnit.isConstructing() == false && myUnit.isRepairing() == false) {
				return myUnit;
			}
		}
		return myUnits.get(0);
	}

	private void handelWorker() {
		for (Unit myUnit : this.gameListener.getSelf().getUnits()) {
			trainWorker(myUnit);
			if (myUnit.getType().isWorker() && myUnit.isIdle()
					&& myUnit.getOrder().equals(Order.PlaceBuilding) == false) {
				sendWorkerToMine(myUnit);
			}
		}
	}

	private void sendWorkerToMine(Unit myUnit) {
		Unit closestMineral = null;
		double distance = 100000;
		for (Unit neutralUnit : this.gameListener.getGame().neutral().getUnits()) {
			if (neutralUnit.getType().isMineralField()) {
				if (BWTA.getGroundDistance(myUnit.getTilePosition(),
						neutralUnit.getTilePosition()) < distance) {
					distance = BWTA.getGroundDistance(myUnit.getTilePosition(), neutralUnit.getTilePosition());
					closestMineral = neutralUnit;
				}
			}
		}
		if (closestMineral != null) {
			myUnit.gather(closestMineral, false);
		}
	}

	private void trainWorker(Unit myUnit) {
		if (myUnit.getType() == UnitType.Terran_Command_Center && myUnit.isIdle()
				&& this.gameListener.getSelf().minerals() >= 50) {
			myUnit.train(UnitType.Terran_SCV);
		}
	}

}
