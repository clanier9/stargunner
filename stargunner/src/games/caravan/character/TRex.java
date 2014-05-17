package games.caravan.character;

import gameEngine.character.BaseCharacter;
import games.caravan.input.action.WalkAction;
import graphicslib3D.Point3D;
import sage.audio.Sound;
import sage.model.loader.ogreXML.OgreXMLParser;
import sage.scene.Group;
import sage.scene.Model3DTriMesh;
import sage.scene.SceneNode;

public class TRex extends NPC {
	private Group model; // the Ogre model 
	private Model3DTriMesh myObject;
	private WalkAction walk;
	private double currDir;
	
	public TRex(Point3D p, Sound s) {
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
			
		walk = new WalkAction(myObject);
		
		addModel(myObject);
	}
	
	public void move(int elapsedTime) {				
		float angle = (float)Math.toRadians(90 + currDir);		
		float deltaX = (float)Math.cos(angle);
		float deltaY = (float)Math.sin(angle);
		translate(deltaX, 0, deltaY);		
	}
	
	public void setDirection(double dir) {
		dir = dir%360;
		rotate(dir);
	}
}
