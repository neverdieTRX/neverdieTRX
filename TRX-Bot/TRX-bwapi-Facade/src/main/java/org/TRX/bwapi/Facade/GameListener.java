package org.TRX.bwapi.Facade;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.TRX.bwapi.Facade.Events.OnFrameEvent;
import org.TRX.bwapi.Facade.Events.OnUnitCompleteEvent;
import org.TRX.bwapi.Facade.Events.OnUnitCreateEvent;
import org.TRX.bwapi.Facade.Events.OnUnitDestroyEvent;

import bwapi.DefaultBWListener;
import bwapi.Game;
import bwapi.Mirror;
import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwta.BWTA;
import bwta.BaseLocation;

@Named
@ApplicationScoped
public class GameListener extends DefaultBWListener {

	@Inject
	Event<OnFrameEvent> onFrameEvent;

	@Inject
	Event<OnUnitCreateEvent> onUnitCreateEvent;

	@Inject
	Event<OnUnitCompleteEvent> onUnitCompletedEvent;

	@Inject
	Event<OnUnitDestroyEvent> onUnitDestroyEvent;

	private Mirror mirror = new Mirror();

	private Game game;

	private Player self;

	public Game getGame() {
		return this.game;
	}

	public Player getSelf() {
		return this.self;
	}

	@PostConstruct
	public void init() {
		System.out.println("init listener");
	}

	public void run() {
		mirror.getModule().setEventListener(this);
		mirror.startGame();
		game.setLocalSpeed(0);
	}

	@Override
	public void onUnitCreate(Unit unit) {
		System.out.println("New unit " + unit.getType());
		OnUnitCreateEvent ouce = new OnUnitCreateEvent(game, 0, game, unit);
		this.onUnitCreateEvent.fire(ouce);

	}

	@Override
	public void onUnitDestroy(Unit unit) {
		OnUnitDestroyEvent oude = new OnUnitDestroyEvent(game, 0, game, unit);
		this.onUnitDestroyEvent.fire(oude);
	}

	@Override
	public void onUnitComplete(Unit unit) {
		OnUnitCompleteEvent ouce = new OnUnitCompleteEvent(game, 0, game, unit);
		onUnitCompletedEvent.fire(ouce);
	}

	@Override
	public void onStart() {
		game = mirror.getGame();
		self = game.self();
		// Use BWTA to analyze map
		// This may take a few minutes if the map is processed first time!
		System.out.println("Analyzing map...");
		BWTA.readMap();
		BWTA.analyze();
		System.out.println("Map data ready");

		int i = 0;
		for (BaseLocation baseLocation : BWTA.getBaseLocations()) {
			System.out.println("Base location #" + (++i) + ". Printing location's region polygon:");
			for (Position position : baseLocation.getRegion().getPolygon().getPoints()) {
				System.out.print(position + ", ");
			}
			System.out.println();
		}
	}

	@Override
	public void onFrame() {
		// game.setTextSize(10);
		game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());

		// draw my units on screen
		this.onFrameEvent.fire(new OnFrameEvent(game, 0, game));
	}

}