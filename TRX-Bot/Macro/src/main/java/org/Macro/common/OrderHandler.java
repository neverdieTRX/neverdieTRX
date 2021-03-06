package org.Macro.common;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.TRX.bwapi.Facade.Utils.BuildOrder;

import bwapi.Order;
import bwapi.UnitType;

@ApplicationScoped
public class OrderHandler {

	private List<BuildOrder> currentOrders;

	@PostConstruct
	private void init() {
		this.currentOrders = new ArrayList<BuildOrder>();
	}

	public List<BuildOrder> getCurrentOrders() {
		return currentOrders;
	}

	public void addOrder(BuildOrder order) {
		this.currentOrders.add(order);
	}

	public void removeOrder(BuildOrder order) {
		this.currentOrders.remove(order);
	}

	/**
	 * Checks if the worker has still the order to build a building, if not the
	 * order will deleted
	 * 
	 */
	public void updateOrders() {
		System.out.println("update buildorders");
		for (BuildOrder order : getCurrentOrders()) {
			if (order.getWorker() == null) {
				System.out.println("something went wrong with the worker");
				removeOrder(order);
				break;
			}
			if (order.getWorker().getOrder().equals(Order.PlaceBuilding) == false
					&& order.getWorker().isConstructing() == false) {
				removeOrder(order);
			}

		}
	}

	public int getAmountOfOrders(UnitType unitType) {
		int result = 0;
		for (BuildOrder order : getCurrentOrders()) {
			if (order.getUnitType() == unitType) {
				result++;
			}
		}
		return result;
	}

}
