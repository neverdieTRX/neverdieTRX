package org.Info;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.TRX.bwapi.Facade.GameListener;
import org.TRX.bwapi.Facade.Events.OnUnitCompleteEvent;

import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;

@ApplicationScoped
public class AmountOfBuildings {

	@Inject
	GameListener gameListener;

	private Map<UnitType, Integer> amountOfBuildingsToBe;

	private Map<UnitType, Integer> amountOfBuildingsCurrent;

	@PostConstruct
	private void init() {
		this.amountOfBuildingsToBe = new HashMap<UnitType, Integer>();
		this.amountOfBuildingsCurrent = new HashMap<UnitType, Integer>();

		this.setAmountOfBuildingCurrent(UnitType.Terran_Barracks, 0);
	}

	public void onUnitComplete(@Observes OnUnitCompleteEvent onUnitComleteEvent) {
		Unit createdUnit = onUnitComleteEvent.getCompletedUnit();
		if (!createdUnit.getPlayer().equals(this.gameListener.getSelf())) {
			return;
		}
		if (createdUnit.getType().isBuilding() == false) {
			return;
		}
		int newAmount = 1;
		if (this.amountOfBuildingsCurrent.containsKey(createdUnit.getType())) {
			newAmount = this.amountOfBuildingsCurrent.get(createdUnit.getType()) + 1;
		}
		this.amountOfBuildingsCurrent.put(createdUnit.getType(), newAmount);
	}

	public Integer getAmountOfBuildingToBe(UnitType unitType) {
		return this.amountOfBuildingsToBe.get(unitType);
	}

	public Integer getAmountOfBuildingCurrent(UnitType unitType) {
		return this.amountOfBuildingsCurrent.get(unitType);
	}

	public void setAmountOfBuildingToBe(UnitType unitType, Integer amount) {
		this.amountOfBuildingsToBe.put(unitType, amount);
	}

	public Map<UnitType, Integer> getAmountOfBuildingsCurrent() {
		return amountOfBuildingsCurrent;
	}

	public void setAmountOfBuildingCurrent(UnitType unitType, Integer amount) {
		this.amountOfBuildingsCurrent.put(unitType, amount);
	}
	public Map<UnitType, Integer> getAmountOfBuildingsToBe() {
		return amountOfBuildingsToBe;
	}
}
