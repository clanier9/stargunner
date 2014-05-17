package games.caravan.character;

import graphicslib3D.Point3D;
import sage.audio.Sound;
import sage.model.loader.ogreXML.OgreXMLParser;
import sage.scene.Group;
import sage.scene.Model3DTriMesh;
import sage.scene.SceneNode;

public class TRex extends NPC {
	private Group model; // the Ogre model 
	private Model3DTriMesh myObject;
	private double currDir;
	private boolean moving;
	private Sound growl, roar;
	
	public TRex(Point3D p) {
		super(p);
		
		OgreXMLParser loader = new OgreXMLParser(); 
		try 
		{ 
			model = loader.loadModel("models/Trex.mesh.xml", "materials/skin.material", "models/Trex.skeleton.xml"); 
		 	model.updateGeometricState(0, true); 
			java.util.Iterator<SceneNode> modelIterator = model.iterator(); 
			myObject = (Model3DTriMesh) modelIterator.next(); 
		} 
		catch (Exception e) 
		{ 
			e.printStackTrace(); 
			System.exit(1); 
		} 
		
		addModel(myObject);
	}
	
	public void setDirection(double dir) {
		dir = dir%360;
		rotate(dir);
		currDir = dir;
	}
	
	public void setMoving(boolean b) {
		moving = b;
	}

	public boolean isMoving() {
		return moving;
	}

	public Sound getGrowl() {
		return growl;
	}

	public void setGrowl(Sound growl) {
		this.growl = growl;
	}

	public Sound getRoar() {
		return roar;
	}

	public void setRoar(Sound roar) {
		this.roar = roar;
	}
}
