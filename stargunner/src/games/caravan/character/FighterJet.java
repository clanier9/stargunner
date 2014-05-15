package games.caravan.character;

import java.io.File;

import sage.model.loader.OBJLoader;
import sage.scene.TriMesh;
import graphicslib3D.Point3D;

public class FighterJet extends RegularShip {
	public FighterJet() {
		super();
		OBJLoader loader = new OBJLoader(); 
		TriMesh jet = loader.loadModel("models" + File.separator + "fighter.obj"); 
		addModel(jet);	
	}
	
	public FighterJet(Point3D p) {
		super(p);
		OBJLoader loader = new OBJLoader();  
		TriMesh jet = loader.loadModel("models" + File.separator + "fighter.obj"); 
		addModel(jet);
	}
}
