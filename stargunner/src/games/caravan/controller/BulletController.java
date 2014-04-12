package games.caravan.controller;

import games.caravan.character.Bullet;

import java.util.Vector;

import sage.scene.Controller;

public class BulletController extends Controller {
	
	private enum state {LIVE, DEAD};
	
	
	
	private Vector<Bullet> bullets = new Vector();

	public BulletController() {
		// TODO Auto-generated constructor stub
	}
	
	public void update(double time) 
	{
		for(Bullet b : bullets )
		{
			double dist = b.getSpeed() * time;
			b.moveFoward(dist);
		}
	}
	
	public void addBullet(Bullet b)
	{
		bullets.add(b);
	}

}

