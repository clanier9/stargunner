package games.caravan.character;

import gameEngine.character.BaseCharacter;
import graphicslib3D.Point3D;

public class NPC extends BaseCharacter {
	public NPC() {
		super();
	}
	
	public NPC(Point3D p) {
		super(p);
	}
	
	public double getX() { return getLocation().getX(); } 
	public double getY() { return getLocation().getY(); } 
	public double getZ() { return getLocation().getZ(); } 
	 
	public void updateLocation() { 
		//TODO 
	}	
}
