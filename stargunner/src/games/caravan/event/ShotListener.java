package games.caravan.event;

import games.caravan.CaravanGame;
import graphicslib3D.Point3D;
import sage.event.IEventListener;
import sage.event.IGameEvent;

public class ShotListener implements IEventListener {

	CaravanGame gw;
	
	public ShotListener(CaravanGame g) {
		gw = g;
	}

	@Override
	public boolean handleEvent(IGameEvent event) {
		ShotEvent shot = (ShotEvent) event;
		Point3D pLoc = shot.getPosition();
		gw.playShot(pLoc);
		
		return true;
	}

}
