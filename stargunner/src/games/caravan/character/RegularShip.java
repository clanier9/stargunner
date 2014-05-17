package games.caravan.character;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

public class RegularShip extends Ship {

	public static final double MAX_X = 10;
	public static final double MAX_Z = 10;
	public static final double MIN_Z = -20;
	
	public RegularShip() {
		super();
				setFireRate(1000);
				setSpeed(0.01f);
	}

	public RegularShip(Point3D p) {
		super(p);
				setFireRate(1000);
				setSpeed(0.01f);
	}

	@Override
	public Bullet[] fire() {
		Point3D loc = getLocation();
		RegularBullet b1 = new RegularBullet(new Point3D(loc.getX() - 0.3, loc.getY(), loc.getZ() + 0.2));
		RegularBullet b2 = new RegularBullet(new Point3D(loc.getX() + 0.3, loc.getY(), loc.getZ() + 0.2));
		Bullet bul[] = new Bullet[2];
		bul[0] = b1;
		bul[1] = b2;
		return bul;

	}
	
	public void moveFoward(double amt)
	{
		super.moveFoward(amt);
		Point3D myLoc = getLocation();
		if(myLoc.getZ() > MAX_Z)
		{
			myLoc.setZ(MAX_Z);
			setLocation(myLoc);
		}
		else if(myLoc.getZ() < MIN_Z)
		{
			myLoc.setZ(MIN_Z);
			setLocation(myLoc);
		}
	}
	
	public void strafeRight(double amt)
	{
		super.strafeRight(amt);
		Point3D myLoc = getLocation();
		if(myLoc.getX() > MAX_X)
		{
			myLoc.setX(MAX_X);
			setLocation(myLoc);
		}
		else if(myLoc.getX() < -MAX_X)
		{
			myLoc.setX(-MAX_X);
			setLocation(myLoc);
		}
	}

}
