package games.caravan.character;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.audio.Sound;
import sage.model.loader.ogreXML.OgreXMLParser;
import sage.scene.Group;
import sage.scene.Model3DTriMesh;
import sage.scene.SceneNode;
import sage.terrain.TerrainBlock;

public class TRex extends NPC {
	private Group model; // the Ogre model 
	private Model3DTriMesh myObject;
	private boolean moving;
	private Sound growl, roar;
	private TerrainBlock theTerrain;
	private float offset, xTrans, zTrans;
	
	public TRex(Point3D p, TerrainBlock t) {
		super(p);
		theTerrain = t;
		Point3D tp = new Point3D(theTerrain.getLocalTranslation().getCol(3));
		xTrans = (float)tp.getX();
		zTrans = (float)tp.getZ();
		offset = (float)theTerrain.getHeight(-xTrans, -zTrans);
		
		
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
		
		setFowardVector(new Vector3D(0,0,-1));
		setSideVector(getFowardVector().cross(getUpVector()).normalize());
		setSpeed(0.002f);
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
	
	public void moveForward(double amt)
	{
		super.moveFoward(amt);
		
		Point3D myLoc = getLocation();
		if(myLoc.getX() > 20 || myLoc.getX() < -20 || myLoc.getZ() > 30 || myLoc.getZ() < -20)
		{
			rotate(180);
		}
		
		updateVerticalPosition();
	}
	
	private void updateVerticalPosition() 
	{ 
		Point3D avLoc = new Point3D(getLocalTranslation().getCol(3)); 
		float x = (float) avLoc.getX(); 
		float z = (float) avLoc.getZ(); 
		float terHeight = theTerrain.getHeight(x-xTrans,z-zTrans); 
		float desiredHeight = terHeight + offset + 3.5f; 
		if (!Float.isNaN(terHeight))
				getLocalTranslation().setElementAt(1, 3, desiredHeight); 
	}
	
	public void update(double eTime) {
		if (moving) {
			moveForward(getSpeed() * eTime);
		}
	}
}
