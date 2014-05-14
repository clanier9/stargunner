package games.caravan.character;

import gameEngine.character.BaseCharacter;
import graphicslib3D.Point3D;

import java.io.File;

import sage.model.loader.OBJLoader;
import sage.model.loader.ogreXML.OgreXMLParser;
import sage.scene.Group;
import sage.scene.Model3DTriMesh;
import sage.scene.SceneNode;
import sage.scene.TriMesh;
import sage.scene.state.TextureState;

public class TRex extends BaseCharacter {
	
	TextureState hobbitTextureState; // for texturing the model 
	Group model; // the Ogre model 
	Model3DTriMesh myObject;
	
	public TRex() {
		super();
		OBJLoader loader = new OBJLoader(); 
		TriMesh trex = loader.loadModel("models" + File.separator + "T-Rex.obj"); 
		addModel(trex);
	}
	
	public TRex(Point3D p) {
		super(p);
		OBJLoader loader = new OBJLoader(); 
		TriMesh trex = loader.loadModel("models" + File.separator + "T-Rex.obj"); 
		addModel(trex);
		
//		OgreXMLParser loader = new OgreXMLParser(); 
//		 try 
//		 { model = loader.loadModel("models/Trex.mesh.xml", 
//		 "materials/TRexSkin.material", 
//		 "models/Trex.skeleton.xml"); 
//		 model.updateGeometricState(0, true); 
//		 java.util.Iterator<SceneNode> modelIterator = model.iterator(); 
//		 myObject = (Model3DTriMesh) modelIterator.next(); 
//		 } 
//		 catch (Exception e) 
//		 { e.printStackTrace(); 
//		 System.exit(1); 
//		 } 

	}
	
}
