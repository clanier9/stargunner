package games.caravan.character;

import graphicslib3D.Point3D;

public class EnemyBullet extends Bullet {

	public EnemyBullet() {
		this.rotate(180);
	}

	public EnemyBullet(Point3D p) {
		super(p);
		this.rotate(180);
	}
	
	public EnemyBullet(Point3D p, Point3D target)
	{
		super(p);
		
	}

}
