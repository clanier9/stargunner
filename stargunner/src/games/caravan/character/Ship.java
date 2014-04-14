package games.caravan.character;

import java.awt.Color;

import gameEngine.character.BaseCharacter;
import graphicslib3D.Point3D;

public abstract class Ship extends BaseCharacter {

	public enum state {LIVE, DEAD, INVINICIBLE};
	
	private state myState;
	private Color myColor;
	
	public Ship() {
		
	}

	public Ship(Point3D p) {
		super(p);
		
	}
	
	public abstract Bullet[] fire();
	
	public void die() //SINISTER
	{
		setMyState(state.DEAD);
	}
	public void respawn()
	{
		setMyState(state.LIVE);
	}

	public state getMyState() {
		return myState;
	}

	public void setMyState(state myState) {
		this.myState = myState;
	}
	
	public void setColor(Color c) {
		myColor = c;
	}
	

}
