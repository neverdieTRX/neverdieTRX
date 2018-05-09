package org.TRX.bwapi.Facade.manager;

import javax.inject.Inject;
import javax.inject.Named;

import org.TRX.bwapi.Facade.GameListener;

import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Handels the Construction of new Buildings and the determination on where to build it.
 * 
 * @author marcus
 */
@Named
public class BuildingManager {

	@Inject
	private GameListener gameListener;

	
	@Inject
	private WorkerManager workerManager;
	
	public void buildBuilding(UnitType buildingType, TilePosition destination, Unit worker) {
		if (worker == null && destination == null) {
			return;
		}
		TilePosition buildTile = getBuildTile(worker, buildingType, destination);
		if (buildTile != null) {
			worker.build(buildingType, buildTile);
		}
	}

	public void buildBuilding(UnitType t, TilePosition destination) {
		Unit worker = this.workerManager.getFreeWorker();
		if (worker == null && destination == null) {
			return;
		}
		TilePosition buildTile = getBuildTile(worker, t, destination);
		if (buildTile != null) {
			worker.build(t, buildTile);
		}
	}
	
	public void handelSupply() {
		if ((this.gameListener.getSelf().supplyTotal() - this.gameListener.getSelf().supplyUsed() < 6)
				&& this.gameListener.getSelf().minerals() >= 100 && this.gameListener.getSelf().supplyUsed() < 400) {
			Unit myUnit = this.workerManager.getFreeWorker();
			this.buildBuilding(UnitType.Terran_Supply_Depot, this.gameListener.getSelf().getStartLocation(), myUnit);
		}
	}

	private TilePosition getBuildTile(Unit builder, UnitType buildingType, TilePosition aroundTile) {
		TilePosition ret = null;
		int maxDist = 3;
		int stopDist = 80;
		
		// Refinery, Assimilator, Extractor
		if (buildingType.isRefinery()) {
			for (Unit n : this.gameListener.getGame().neutral().getUnits()) {
				if ((n.getType() == UnitType.Resource_Vespene_Geyser)
						&& (Math.abs(n.getTilePosition().getX() - aroundTile.getX()) < stopDist)
						&& (Math.abs(n.getTilePosition().getY() - aroundTile.getY()) < stopDist))
					return n.getTilePosition();
			}
		}
		while ((maxDist < stopDist) && (ret == null)) {
			for (int i = aroundTile.getX() - maxDist; i <= aroundTile.getX() + maxDist; i = i + 2) {
				for (int j = aroundTile.getY() - maxDist; j <= aroundTile.getY() + maxDist; j = j + 2) {
					if (!this.gameListener.getGame().canBuildHere(new TilePosition(i, j), buildingType)) {
						continue;
					}
					if (checkIfTileIsBlocked(builder, i, j)) {
						continue;
					}
					return new TilePosition(i, j);
				}
			}
			maxDist += 2;
		}
		if (ret == null)
			this.gameListener.getGame().printf("Unable to find suitable build position for " + buildingType.toString());
		return ret;
	}

	private boolean checkIfTileIsBlocked(Unit builder, int i, int j) {
		for (Unit u : this.gameListener.getGame().getAllUnits()) {
			if (u.getID() == builder.getID())
				continue;
			if ((Math.abs(u.getTilePosition().getX() - i) < 4)
					&& (Math.abs(u.getTilePosition().getY() - j) < 4))
				return true;
		}
		return false;
	}
}
