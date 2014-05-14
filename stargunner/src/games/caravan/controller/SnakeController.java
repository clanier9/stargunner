package games.caravan.controller;

import games.caravan.character.Bullet;
import graphicslib3D.Point3D;
import sage.scene.Controller;
import sage.scene.SceneNode;

public class SnakeController extends Controller {

	private double width;
	
	public SnakeController(double width) {
		this.width = width;
	}
	

	@Override
	public void update(double  time) {
		for (SceneNode node : controlledNodes)
		{
			Bullet b = (Bullet) node;
			double dist = b.getSpeed() * time;
			b.moveFoward(dist);
			Point3D p = b.getLocation();
			p.setX(Math.sinh(p.getZ()) * width);
			b.setLocation(p);
		}

	}

}
