package games.caravan.controller;

import games.caravan.CaravanGame;
import games.caravan.character.Bullet;
import games.caravan.character.Ship;
import games.caravan.character.UFO;
import graphicslib3D.Point3D;
import sage.scene.SceneNode;

public class SnakeController extends BaseController {

	private double width;
	private Ship player;
	private CaravanGame gw;
	private long lastTime;
	private long elapsedTime;
	
	public SnakeController(double width, CaravanGame g) {
		this.width = width;
		gw = g;
		lastTime = System.currentTimeMillis();
		elapsedTime = 0;
	}
	

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
					if(elapsedTime >= 3000 && bLoc.getZ() < 20)
					{
						Bullet[] bul = b.fireAt(player.getLocation());
						for(int i = 0; i < bul.length; i++)
						{
							gw.addBullet(bul[i]);
						}
					}
				}
				double dist = b.getSpeed() * (t);
				b.moveFoward(dist);
				Point3D p = b.getLocation();
				p.setX(Math.sinh(p.getZ()) * width + b.getSpawn());
				b.setLocation(p);
				System.out.println(p);
			}
		}
		
		if(elapsedTime >= 4000)
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
