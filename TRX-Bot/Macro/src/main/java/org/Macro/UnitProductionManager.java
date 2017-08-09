package org.Macro;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.TRX.bwapi.Facade.GameListener;
import org.TRX.bwapi.Facade.Events.OnUnitCompleteEvent;
import org.TRX.bwapi.Facade.Events.OnUnitDestroyEvent;

import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;

@ApplicationScoped
public class UnitProductionManager {

	@Inject
	GameListener gameListener;

	private List<Unit> commandCenters;
	private List<Unit> barracks;
	private List<Unit> factorys;
	private List<Unit> starports;

	public void update() {
		// this.createMariens();
	}

	private void createMariens() {
		System.out.println("trying to train mariens");
		for (Unit barrack : barracks) {
			System.out.println(barracks.size());
			if (this.gameListener.getSelf().minerals() >= 50 && barrack.isIdle()) {
				barrack.train(UnitType.Terran_Marine);
			}
		}
	}

	@PostConstruct
	private void init() {
		this.commandCenters = new ArrayList<Unit>();
		this.barracks = new ArrayList<Unit>();
		this.factorys = new ArrayList<Unit>();
		this.starports = new ArrayList<Unit>();
	}

	public void onUnitComplete(@Observes OnUnitCompleteEvent onUnitCompleteEvent) {
		Unit createdUnit = onUnitCompleteEvent.getCompletedUnit();
		this.doProduktion();
		if (createdUnit.getType().isBuilding() == false) {
			return;
		}
		if (createdUnit.getType().getRace().equals(Race.Terran) == false) {
			return;
		}
		if (!createdUnit.getPlayer().equals(this.gameListener.getSelf())) {
			return;
		}
		System.out.println("test " + createdUnit.getType());
		if (createdUnit.getType().equals(UnitType.Terran_Command_Center)) {
			this.commandCenters.add(createdUnit);
		} else if (createdUnit.getType().equals(UnitType.Terran_Barracks)) {
			System.out.println("adding barracks");
			this.barracks.add(createdUnit);
			System.out.println(barracks.size());
		} else if (createdUnit.getType().equals(UnitType.Terran_Factory)) {
			this.factorys.add(createdUnit);
		} else if (createdUnit.getType().equals(UnitType.Terran_Starport)) {
			this.starports.add(createdUnit);
		}
	}

	public void onUnitDestroy(@Observes OnUnitDestroyEvent onUnitDestoryEvent) {
		Unit destroyedUnit = onUnitDestoryEvent.getDestroyedUnit();
		if (destroyedUnit.getType().isBuilding() == false) {
			return;
		}
		if (destroyedUnit.getType().getRace().equals(Race.Terran) == false) {
			return;
		}
		if (!destroyedUnit.getPlayer().equals(this.gameListener.getSelf())) {
			return;
		}

		if (destroyedUnit.getType() == UnitType.Terran_Command_Center) {
			this.commandCenters.remove(destroyedUnit);
		} else if (destroyedUnit.getType() == UnitType.Terran_Barracks) {
			this.barracks.remove(destroyedUnit);
		} else if (destroyedUnit.getType() == UnitType.Terran_Factory) {
			this.factorys.remove(destroyedUnit);
		} else if (destroyedUnit.getType() == UnitType.Terran_Starport) {
			this.starports.remove(destroyedUnit);
		}
	}

	private void doProduktion() {
		this.createMariens();
	}
}
