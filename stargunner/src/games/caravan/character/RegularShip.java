package games.caravan.character;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

public class RegularShip extends Ship {

	public RegularShip() {
		super();
				setFireRate(1000);
	}

	public RegularShip(Point3D p) {
		super(p);
				setFireRate(1000);
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

}
