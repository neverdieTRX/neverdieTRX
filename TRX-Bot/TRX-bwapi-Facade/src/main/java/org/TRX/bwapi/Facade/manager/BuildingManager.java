package org.TRX.bwapi.Facade.manager;

import javax.inject.Inject;
import javax.inject.Named;

import org.TRX.bwapi.Facade.GameListener;

import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

@Named
public class BuildingManager {

	@Inject
	private GameListener gameListener;

	@Inject
	private WorkerManager workerManager;

	public void buildBuilding(UnitType t, TilePosition destination, Unit myUnit) {

		if (myUnit != null && destination != null) {

			TilePosition buildTile = getBuildTile(myUnit, t, destination);
			if (buildTile != null) {
				myUnit.build(t, buildTile);
				System.out.println("building");
			}
		}
	}

	public void handelSupply() {
		System.out.println("funktioniert ");
		System.out.println(this.gameListener.getSelf());
		System.out.println("supplyHandeling " + this.gameListener.getSelf().supplyTotal());
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
					if (this.gameListener.getGame().canBuildHere(new TilePosition(i, j), buildingType)) {
						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : this.gameListener.getGame().getAllUnits()) {
							if (u.getID() == builder.getID())
								continue;
							if ((Math.abs(u.getTilePosition().getX() - i) < 4)
									&& (Math.abs(u.getTilePosition().getY() - j) < 4))
								unitsInWay = true;
						}
						if (!unitsInWay) {
							return new TilePosition(i, j);
						}
						// creep for Zerg
						if (buildingType.requiresCreep()) {
							boolean creepMissing = false;
							for (int k = i; k <= i + buildingType.tileWidth(); k++) {
								for (int l = j; l <= j + buildingType.tileHeight(); l++) {
									if (!this.gameListener.getGame().hasCreep(k, l))
										creepMissing = true;
									break;
								}
							}
							if (creepMissing)
								continue;
						}
					}
				}
			}
			maxDist += 2;
		}

		if (ret == null)
			this.gameListener.getGame().printf("Unable to find suitable build position for " + buildingType.toString());
		return ret;
	}

}
