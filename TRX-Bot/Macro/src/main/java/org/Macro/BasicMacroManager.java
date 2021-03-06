package org.Macro;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.Macro.common.OrderHandler;
import org.TRX.bwapi.Facade.GameListener;
import org.TRX.bwapi.Facade.Utils.BuildOrder;
import org.TRX.bwapi.Facade.manager.BuildingManager;
import org.TRX.bwapi.Facade.manager.WorkerManager;

import bwapi.Unit;
import bwapi.UnitType;

/**
 * Handels all the Macro In Starcraft Macro describes all of baseconstruction
 * handeling upgrades and exansions
 * 
 * @author Turox
 */
@Named
public class BasicMacroManager {

	@Inject
	GameListener gameListener;

	@Inject
	WorkerManager workerManager;

	@Inject
	BuildingManager buildingManager;

	@Inject
	OrderHandler orderHandler;

	@Inject
	BuildingProductionManager buildingProductionManager;

	@Inject
	UnitProductionManager UnitProductionManager;

	private int lastCheckedOrders;

	@PostConstruct
	private void init() {
		this.lastCheckedOrders = 0;
	}

	public void update() {
		this.workerManager.update();
		this.handelSupply();
		this.UnitProductionManager.update();
		this.buildingProductionManager.update();

		if (this.gameListener.getGame().getFrameCount() >= (this.lastCheckedOrders + 120)) {
			this.orderHandler.updateOrders();
			this.lastCheckedOrders = this.gameListener.getGame().getFrameCount();
		}
	}

	private void handelSupply() {
		if (this.orderHandler.getAmountOfOrders(UnitType.Terran_Supply_Depot) >= 1) {
			return;
		}
		if ((this.gameListener.getSelf().supplyTotal() - this.gameListener.getSelf().supplyUsed() < 8)
				&& this.gameListener.getSelf().minerals() >= 100
				&& this.gameListener.getSelf().supplyUsed() < 400) {
			Unit myUnit = this.workerManager.getFreeWorker();
			this.buildingManager.buildBuilding(UnitType.Terran_Supply_Depot,
					this.gameListener.getSelf().getStartLocation(), myUnit);
			this.orderHandler.addOrder(
					new BuildOrder(gameListener.getGame().getFrameCount(), UnitType.Terran_Supply_Depot, myUnit));
		}
	}

}
