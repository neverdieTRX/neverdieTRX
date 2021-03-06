package org.Macro;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.Info.AmountOfBuildings;
import org.Info.CurrentGameInfo;
import org.Macro.common.OrderHandler;
import org.TRX.bwapi.Facade.GameListener;
import org.TRX.bwapi.Facade.Utils.BuildOrder;
import org.TRX.bwapi.Facade.manager.BuildingManager;
import org.TRX.bwapi.Facade.manager.WorkerManager;

import bwapi.Unit;
import bwapi.UnitType;

@ApplicationScoped
public class BuildingProductionManager {

	@Inject
	private GameListener gameListener;

	@Inject
	private WorkerManager workerManager;

	@Inject
	private OrderHandler orderHandler;

	@Inject
	private BuildingManager buildingManager;

	@Inject
	private AmountOfBuildings amountOfBuildings;

	@Inject
	private CurrentGameInfo currentGameInfo;

	private int lastUpdated;

	@PostConstruct
	private void init() {
		this.lastUpdated = 0;
	}

	public void update() {
		if (this.gameListener.getGame().getFrameCount() >= (this.lastUpdated + 80)) {
			this.lastUpdated = this.gameListener.getGame().getFrameCount();
			System.out.println("wird aufgerufen");
			this.amountOfBuildings.getAmountOfBuildingsToBe().forEach((key, value) -> {
				// if (this.amountOfBuildings.getAmountOfBuildingCurrent(key) <
				// value) {
				this.buildProductionFacility(key, value);
				// }
			});
		}
	}

	private void buildProductionFacility(UnitType unitType, Integer amount) {
		if (this.amountOfBuildings.getAmountOfBuildingCurrent(unitType) < amount) {
			if (this.orderHandler.getAmountOfOrders(
					unitType) < (amount - this.amountOfBuildings.getAmountOfBuildingCurrent(unitType))) {
				// maybe look for a method get Finnished Constructed building
				// better
				Unit myWorker = this.workerManager.getFreeWorker();
				this.buildingManager.buildBuilding(unitType,
						this.currentGameInfo.getProductionPlace().getTilePosition(), myWorker);
				BuildOrder newOrder = new BuildOrder(this.gameListener.getGame().getFrameCount(), unitType, myWorker);
				this.orderHandler.addOrder(newOrder);
			}
		}
	}

}
