package games.caravan.character;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

public class RegularShip extends Ship {
	
	//private static float[] vrts = new float[] {0,1,0,-1,-1,1,1,-1,1,1,-1,-1,-1,-1,-1};
	//private static float[] cl = new float[] {1,0,0,1,0,1,0,1,0,0,1,1,1,1,0,1,1,0,1,1};
	//private static int[] triangles = new int[] {0,1,2,0,2,3,0,3,4,0,4,1,1,4,2,4,3,2};

	public RegularShip() {
		super();
				this.rotate(90, new Vector3D(1,0,0));
				setFireRate(1000);
	}

	public RegularShip(Point3D p) {
		super(p);
				this.rotate(90, new Vector3D(1,0,0));
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
