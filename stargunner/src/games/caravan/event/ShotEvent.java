package games.caravan.event;

import games.caravan.character.Ship;
import graphicslib3D.Point3D;
import sage.event.AbstractGameEvent;

public class ShotEvent extends AbstractGameEvent {

	private Point3D position; //Where the shot was fired
	private Ship player; //Who shot it
	
	public ShotEvent(Ship s, Point3D pos) {
		
		position = pos;
		player = s;
	}

	public ShotEvent(float timeStamp) {
		super(timeStamp);
		
	}

	public Ship getPlayer() {
		return player;
	}
	
	public Point3D getPosition()
	{
		return position;
	}


}
