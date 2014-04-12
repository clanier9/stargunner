package gameEngine.character;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.scene.TriMesh;

public class BaseCharacter extends TriMesh {

	private Point3D location;
	private Vector3D foward;
	private Vector3D side;
	private Vector3D up;
	
	public BaseCharacter() {
		location = new Point3D(0,0,0);
		foward = new Vector3D(0,0,1);
		side = new Vector3D(1,0,0);
		up  = new Vector3D(0,1,0);
		//updateTranslation();
	}
	
	public BaseCharacter(Point3D p) {
		location = p;
		foward = new Vector3D(0,0,1);
		side = new Vector3D(1,0,0);
		up  = new Vector3D(0,1,0);
		updateTranslation();
		
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
		foward = foward.add(dir);
		side = foward.cross(up);
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

}
