package org.DecisionMaker;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.Info.AmountOfBuildings;
import org.Info.CurrentGameInfo;
import org.Macro.BasicMacroManager;
import org.TRX.bwapi.Facade.GameListener;
import org.TRX.bwapi.Facade.Events.OnFrameEvent;

import bwapi.UnitType;

@ApplicationScoped
public class DecisionMaker {

	@Inject
	private GameListener gameListener;

	@Inject
	private CurrentGameInfo currentGameInfo;

	@Inject
	private AmountOfBuildings amountOfBuildings;

	@Inject
	BasicMacroManager basicMacroManager;
	private int decisionTimer;


	@PostConstruct
	private void init() {
		this.decisionTimer = 0;
		this.amountOfBuildings.setAmountOfBuildingToBe(UnitType.Terran_Barracks, 4);
	}

	public void run() {
		this.gameListener.run();
	}

	public void onFrame(@Observes OnFrameEvent onFrameEvent) {
		this.basicMacroManager.update();

		if (this.gameListener.getGame().getFrameCount() >= (this.decisionTimer + 120)) {
			this.doDecisions();
			this.decisionTimer = this.gameListener.getGame().getFrameCount();
		}
	}


	private void doDecisions() {
	}
}
