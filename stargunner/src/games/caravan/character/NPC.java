package games.caravan.character;

import gameEngine.character.BaseCharacter;
import graphicslib3D.Point3D;

public class NPC extends BaseCharacter {
	Point3D location;
	
	public NPC() {
		super();
	}
	
	public NPC(Point3D p) {
		super(p);
	}
	
	public double getX() { return location.getX(); } 
	public double getY() { return location.getY(); } 
	public double getZ() { return location.getZ(); } 
	 
	public void updateLocation() { 
		//TODO 
	}	
}
