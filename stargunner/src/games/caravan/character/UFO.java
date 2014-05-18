package games.caravan.character;

import java.io.File;

import sage.model.loader.OBJLoader;
import sage.scene.Controller;
import sage.scene.TriMesh;
import games.caravan.controller.BaseController;
import graphicslib3D.Point3D;

public class UFO extends Ship {

	private Ship target;
	private double xSpawn;
	private BaseController myControl;
	
	public UFO() {
		super();
		OBJLoader loader = new OBJLoader();  
		TriMesh ufo = loader.loadModel("models" + File.separator + "ufo.obj"); 
		addModel(ufo);
		setSpawn(getLocation().getX());
		setSpeed(0.007f);
	}

	public UFO(Point3D p) {
		super(p);
		OBJLoader loader = new OBJLoader();  
		TriMesh ufo = loader.loadModel("models" + File.separator + "ufo.obj"); 
		addModel(ufo);
		setSpawn(getLocation().getX());
		setSpeed(0.007f);
	}

	public Bullet[] fire() {
		Bullet[] b = new Bullet[1];
		b[0] = new EnemyBullet(getLocation());
		return b;
	}
	
	public Bullet[] fireAt(Point3D p)
	{
		Bullet[] b = fire();
		b[0].lookAt(p);
		return b;
	}

	public Ship getTarget() {
		return target;
	}

	public void setTarget(Ship target) {
		this.target = target;
	}

	public double getSpawn() {
		return xSpawn;
	}

	public void setSpawn(double xSpawn) {
		this.xSpawn = xSpawn;
	}
	
	public void addController(BaseController contr)
	{
		super.addController(contr);
		myControl = contr;
	}
	
	public void removeController()
	{
		super.removeController(myControl);
		myControl.removeNode(this);
	}

}
