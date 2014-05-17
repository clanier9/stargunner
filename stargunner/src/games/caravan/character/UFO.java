package games.caravan.character;

import java.io.File;

import sage.model.loader.OBJLoader;
import sage.scene.TriMesh;
import graphicslib3D.Point3D;

public class UFO extends Ship {

	public UFO() {
		super();
		OBJLoader loader = new OBJLoader();  
		TriMesh ufo = loader.loadModel("models" + File.separator + "ufo.obj"); 
		addModel(ufo);
		setSpeed(0.0001f);
	}

	public UFO(Point3D p) {
		super(p);
		OBJLoader loader = new OBJLoader();  
		TriMesh ufo = loader.loadModel("models" + File.separator + "ufo.obj"); 
		addModel(ufo);
		setSpeed(0.0001f);
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

}
