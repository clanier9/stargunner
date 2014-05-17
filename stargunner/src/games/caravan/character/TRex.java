package games.caravan.character;

import gameEngine.character.BaseCharacter;
import graphicslib3D.Point3D;

import sage.audio.Sound;
import sage.model.loader.ogreXML.OgreXMLParser;
import sage.scene.Group;
import sage.scene.Model3DTriMesh;
import sage.scene.SceneNode;

public class TRex extends BaseCharacter {
	Group model; // the Ogre model 
	Model3DTriMesh myObject;
	
	public TRex() {
		super();
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
		addModel(myObject);
	}
}
