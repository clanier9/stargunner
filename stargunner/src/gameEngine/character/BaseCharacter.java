package gameEngine.character;

import java.util.UUID;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.scene.Model3DTriMesh;
import sage.scene.TriMesh;

public class BaseCharacter extends Model3DTriMesh {

	private Point3D location;
	protected Vector3D foward;
	protected Vector3D side;
	protected Vector3D up;
	protected UUID id;
	private float speed;
	
	public BaseCharacter() {
		location = new Point3D(0,0,0);
		foward = new Vector3D(0,0,1);
		side = new Vector3D(1,0,0);
		up  = new Vector3D(0,1,0);
		id = UUID.randomUUID();
		setSpeed(0.05f);
		//updateTranslation();
	}
	
	public BaseCharacter(Point3D p) {
		location = p;
		foward = new Vector3D(0,0,1);
		side = new Vector3D(1,0,0);
		up  = new Vector3D(0,1,0);
		setSpeed(0.05f);
		updateTranslation();
		id = UUID.randomUUID();
	}
	
	public void addModel(Model3DTriMesh model)
	{
		setAnimatedNormals(model.getAnimatedNormals()); 
		setAnimatedVertices(model.getAnimatedVertices()); 
		setAnimations(model.getAnimations()); 
		setJoints(model.getJoints()); 
	    setShaderProgram(model.getShaderProgram()); 
		setTextureFilename(model.getTextureFileName()); 
		setVertexBoneIDs(model.getVertexBoneIDs()); 
		setVertexBoneWeights(model.getVertexBoneWeights());
		
		
		setColorBuffer(model.getColorBuffer());  
		setFaceMaterialIndices(model.getFaceMaterialIndices()); 
		setFaceMaterials(model.getFaceMaterials()); 
		setIndexBuffer(model.getIndexBuffer()); 
		setNormalBuffer(model.getNormalBuffer()); 
		setTextureBuffer(model.getTextureBuffer()); 
		setVertexBuffer(model.getVertexBuffer()); 
		
		updateLocalBound();
		

	}
	
	public void addModel(TriMesh m)
	{
		setColorBuffer(m.getColorBuffer());  
		setFaceMaterialIndices(m.getFaceMaterialIndices()); 
		setFaceMaterials(m.getFaceMaterials()); 
		setIndexBuffer(m.getIndexBuffer()); 
		setNormalBuffer(m.getNormalBuffer()); 
		setTextureBuffer(m.getTextureBuffer()); 
		setVertexBuffer(m.getVertexBuffer()); 
		
		updateLocalBound();
	}

	public UUID getId() {
		return id;
	}

	public Point3D getLocation() {
		return location;
	}

	public void setLocation(Point3D location) {
		this.location = location;
		Matrix3D m = new Matrix3D();
		m.translate(location.getX(), location.getY(), location.getZ());
		setLocalTranslation(m);
	}
	
	public void move(Vector3D d, double amt)
	{
		Vector3D loc = new Vector3D(location);
		Vector3D dir = d.normalize();
		dir = dir.mult(amt); //We now have a vector in the movement direction size amt
		loc = loc.add(dir);
		location = new Point3D(loc);
		updateTranslation();
		//this.updateWorldBound();
	}
	
	public void moveFoward(double amt)
	{
		Vector3D loc = new Vector3D(location);
		Vector3D dir = foward.normalize();
		dir = dir.mult(amt); //We now have a vector in the movement direction size amt
		loc = loc.add(dir);
		location = new Point3D(loc);
		updateTranslation();
	}
	
	public void strafeRight(double amt)
	{
		Vector3D loc = new Vector3D(location);
		Vector3D dir = side.normalize();
		dir = dir.mult(-amt); //We now have a vector in the movement direction size amt
		loc = loc.add(dir);
		location = new Point3D(loc);
		updateTranslation();
	}
	
	public void updateTranslation()
	{
		Matrix3D m = new Matrix3D();
		m.translate(location.getX(), location.getY(), location.getZ());
		setLocalTranslation(m);
		//this.updateLocalBound();
	}

	
	public void rotate(double degrees)
	{
		degrees = degrees % 360;
		Matrix3D rot = this.getLocalRotation();
		rot.rotate(degrees, up);
		this.foward = foward.normalize().mult(rot);
		this.side = side.normalize().mult(rot);
		this.setLocalRotation(rot);
	}
	
	public void setRotation(double degrees)
	{
		degrees = degrees % 360;
		Matrix3D rot = this.getLocalRotation();
		rot.setToIdentity();
		rot.rotate(degrees, up);
		foward = new Vector3D(0,0,1).mult(rot);
		side = new Vector3D(1,0,0).mult(rot);
		this.setLocalRotation(rot);
	}

	public Vector3D getFowardVector() {
		return foward;
	}

	public void setFowardVector(Vector3D foward) {
		this.foward = foward;
	}

	public Vector3D getSideVector() {
		return side;
	}

	public void setSideVector(Vector3D side) {
		this.side = side;
	}

	public Vector3D getUpVector() {
		return up;
	}

	public void setUpVector(Vector3D up) {
		this.up = up;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public void lookAt(Point3D p) //Note: Does not rotate the model
	{
		Vector3D target = new Vector3D(p);
		Vector3D pos = new Vector3D(getLocation());
		Vector3D viewDir = target.minus(pos).normalize();
		setFowardVector(viewDir);
		setSideVector(getFowardVector().cross(up).normalize());
		setUpVector(getFowardVector().cross(side).normalize());
		this.rotate((float) new Vector3D(0,0,1).dot(foward), up);
	}
	
	
	
}
