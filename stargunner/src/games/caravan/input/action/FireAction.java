package games.caravan.input.action;

import net.java.games.input.Event;
import sage.event.IEventManager;
import sage.input.action.AbstractInputAction;
import games.caravan.CaravanGame;
import games.caravan.character.Bullet;
import games.caravan.character.Ship;
import games.caravan.event.ShotEvent;

public class FireAction extends AbstractInputAction {
	
	private Ship ship;
	private long timeLastFired;
	private CaravanGame gw;

	public FireAction(Ship s, CaravanGame g) {
		ship = s;
		timeLastFired = System.currentTimeMillis();
		gw = g;
	}

	@Override
	public void performAction(float arg0, Event arg1) {
		if(System.currentTimeMillis() - timeLastFired >= ship.getFireRate())
		{
			timeLastFired = System.currentTimeMillis();
			Bullet[] b = ship.fire();
			for(int i = 0; i < b.length; i++)
			{
				gw.addBullet(b[i]);
			}
			IEventManager m = gw.getEventMgr();
			ShotEvent e = new ShotEvent(ship,ship.getLocation());
			m.triggerEvent(e);
		}
		
	}

}
