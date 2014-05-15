package games.caravan.controller;

import games.caravan.character.Ship;
import games.caravan.character.UFO;
import graphicslib3D.Point3D;

import java.util.ArrayList;

import sage.scene.Controller;
import sage.scene.SceneNode;

public class ChaseController extends Controller {

	ArrayList<Ship> players = new ArrayList<Ship>();
	
	public ChaseController() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(double time) {
		for (SceneNode node : controlledNodes)
		{
			if(node instanceof UFO)
			{
				Ship player = players.get(0);
				UFO b = (UFO) node;
				
				Point3D pLoc = player.getLocation();
				Point3D bLoc = b.getLocation();
				
				if(pLoc.getZ() < bLoc.getZ()) //If player is still below you
				{
					
					if(pLoc.getX() - bLoc.getX() < 0.5)
					{
						b.setRotation(150);
					}
					else if(pLoc.getX() - bLoc.getX() > 0.5)
					{
						b.setRotation(210);
					}
					else b.setRotation(180);
				}
				
				double dist = b.getSpeed() * time;
				b.moveFoward(dist);
			}
		}
	}
	
	public void addPlayer(Ship s)
	{
		players.add(s);
	}

}
