package games.caravan.controller;

import games.caravan.CaravanGame;
import games.caravan.character.Bullet;
import games.caravan.character.Ship;
import games.caravan.character.UFO;
import graphicslib3D.Point3D;

import java.util.ArrayList;

import sage.scene.Controller;
import sage.scene.SceneNode;

public class ChaseController extends BaseController {

	Ship player;
	CaravanGame gw;
	
	
	long lastTime;
	long elapsedTime;
	
	public ChaseController(CaravanGame g) {
		// TODO Auto-generated constructor stub
		lastTime = System.currentTimeMillis();
		gw = g;
	}

	@Override
	public void update(double time) {
		long current = System.currentTimeMillis();
		long t = current - lastTime;
		elapsedTime += t;
		lastTime = current;
		
		for (SceneNode node : controlledNodes)
		{
			if(node instanceof UFO)
			{
				UFO b = (UFO) node;
				
				Point3D pLoc = player.getLocation();
				Point3D bLoc = b.getLocation();
				
				if(pLoc.getZ() < bLoc.getZ()) //If player is still below you
				{
					
					if(pLoc.getX() - bLoc.getX() > 0.5)
					{
						b.setRotation(150);
					}
					else if(pLoc.getX() - bLoc.getX() < 0.5)
					{
						b.setRotation(210);
					}
					else b.setRotation(180);
					
					if(elapsedTime >= 2000 && bLoc.getZ() < 20)
					{
						Bullet[] bul = b.fire();
						for(int i = 0; i < bul.length; i++)
						{
							gw.addBullet(bul[i]);
						}
					}
				}
				
				double dist = b.getSpeed() * (t);
				b.moveFoward(dist);
				
				
			}
		}
		if(elapsedTime >= 2000)
		{
			elapsedTime = 0;
		}
	}
	
	public void addPlayer(Ship s)
	{
		player = s;
	}
	
	public void removeNode(SceneNode s)
	{
		controlledNodes.remove(s);
	}

}
