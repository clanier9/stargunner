package games.caravan.character;

import sage.model.loader.OBJLoader;
import sage.model.loader.ogreXML.OgreXMLParser;
import sage.scene.Group;
import sage.scene.Model3DTriMesh;
import sage.scene.SceneNode;
import sage.scene.TriMesh;
import sage.scene.state.RenderState;
import sage.texture.TextureManager;
import graphicslib3D.Point3D;

public class FighterJet extends RegularShip {
	public FighterJet() {
		super();
		OBJLoader loader = new OBJLoader(); 
		TriMesh jet = loader.loadModel("./models/fighter.obj"); 
		addModel(jet);	
	}
	
	public FighterJet(Point3D p) {
		super(p);
		OBJLoader loader = new OBJLoader();  
		TriMesh jet = loader.loadModel("./models/fighter.obj"); 
		addModel(jet);
	}
}
