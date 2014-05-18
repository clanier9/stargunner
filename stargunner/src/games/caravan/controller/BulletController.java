package games.caravan.controller;

import games.caravan.character.Bullet;

import java.util.Vector;

import sage.scene.Controller;
import sage.scene.SceneNode;

public class BulletController extends BaseController {
	
	
	long lastTime;
	
	public BulletController() {
		// TODO Auto-generated constructor stub
		lastTime = System.currentTimeMillis();
	}
	
	public void update(double time) 
	{
		
		long current = System.currentTimeMillis();
		long t = current - lastTime;
		lastTime = current;
		
		double dist;
		for (SceneNode node : controlledNodes)
		{
			if(node instanceof Bullet)
			{
				Bullet b = (Bullet) node;
				dist = b.getSpeed() * t;
				b.moveFoward(dist);
			}
		}
	}
	
	public void removeNode(SceneNode s)
	{
		controlledNodes.remove(s);
	}
}

