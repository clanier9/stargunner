package games.caravan.character;

import gameEngine.character.BaseCharacter;
import graphicslib3D.Point3D;

public abstract class Ship extends BaseCharacter {

	public enum state {LIVE, DEAD, INVINICIBLE};
	
	private state myState;
	private long fireRateInMillis;
	
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
	
	public boolean isDead()
	{
		if(myState == state.DEAD) return true;
		else return false;
	}

	public long getFireRate() {
		return fireRateInMillis;
	}

	public void setFireRate(long fireRateInMillis) {
		this.fireRateInMillis = fireRateInMillis;
	}
	
	
	
	
	

}
