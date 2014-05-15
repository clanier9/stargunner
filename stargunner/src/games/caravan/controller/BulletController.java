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
		for (SceneNode node : controlledNodes)
		{
			if(node instanceof Bullet)
			{
				Bullet b = (Bullet) node;
				double dist = b.getSpeed() * time;
				b.moveFoward(dist);
			}
		}
	}
}

