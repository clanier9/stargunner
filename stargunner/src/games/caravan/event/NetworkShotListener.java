package games.caravan.event;

import games.caravan.CaravanNetworkingGame;
import graphicslib3D.Point3D;
import sage.event.IEventListener;
import sage.event.IGameEvent;

public class NetworkShotListener implements IEventListener {

CaravanNetworkingGame gw;
	
	public NetworkShotListener(CaravanNetworkingGame g) {
		gw = g;
	}
	
	@Override
	public boolean handleEvent(IGameEvent event) {
		
		ShotEvent shot = (ShotEvent) event;
		
		gw.fireMessage();
		
		return false;
	}

}
