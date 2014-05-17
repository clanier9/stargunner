package games.caravan.controller;

import games.caravan.character.Bullet;

import java.util.Vector;

import sage.scene.Controller;
import sage.scene.SceneNode;

public class BulletController extends Controller {
	
	public BulletController() {
		// TODO Auto-generated constructor stub
	}
	
	public void update(double time) 
	{
		double dist;
		for (SceneNode node : controlledNodes)
		{
			if(node instanceof Bullet)
			{
				Bullet b = (Bullet) node;
				dist = b.getSpeed() * time;
				b.moveFoward(dist);
			}
		}
	}
}

