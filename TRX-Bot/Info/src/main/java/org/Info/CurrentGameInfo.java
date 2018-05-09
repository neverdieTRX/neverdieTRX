package org.Info;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.TRX.bwapi.Facade.GameListener;

import bwta.BWTA;
import bwta.BaseLocation;

@ApplicationScoped
public class CurrentGameInfo {

	@Inject
	GameListener gameListener;

	private BaseLocation startBaseLocation;
	
	private BaseLocation productionPlace;
	
	private List<BaseLocation> expansions;

	@PostConstruct
	private void init() {
		this.startBaseLocation = BWTA.getNearestBaseLocation(this.gameListener.getSelf().getStartLocation());
		this.productionPlace = this.startBaseLocation;
		this.expansions = new ArrayList<BaseLocation>();
	}

	
	public List<BaseLocation> getExpansions() {
		return expansions;
	}

	public void setExpansions(List<BaseLocation> expansions) {
		this.expansions = expansions;
	}

	public BaseLocation getProductionPlace() {
		return productionPlace;
	}

	public void setProductionPlace(BaseLocation productionPlace) {
		this.productionPlace = productionPlace;
	}

	public BaseLocation getStartBaseLocation() {
		return startBaseLocation;
	}

}
