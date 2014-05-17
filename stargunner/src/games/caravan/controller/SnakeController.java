package games.caravan.controller;

import games.caravan.character.Bullet;
import games.caravan.character.UFO;
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
			if(node instanceof UFO)
			{
				UFO b = (UFO) node;
				double dist = b.getSpeed() * time;
				b.moveFoward(dist);
				Point3D p = b.getLocation();
				p.setX(Math.sinh(p.getZ()) * width + b.getSpawn());
				b.setLocation(p);
				System.out.println(p);
			}
		}

	}
	
	public void removeNode(SceneNode s)
	{
		controlledNodes.remove(s);
	}

}
