package gameEngine.input.action;

import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;
import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;

public class MoveRXAxis extends AbstractInputAction {

	private ICamera camera;
	private float speed;
	private Vector3D gup = new Vector3D(0,1,0);
	
	
	public MoveRXAxis(ICamera c, float s)
	{ 
		camera = c;
		speed = s;
	}
	
	public void performAction(float time, net.java.games.input.Event e)
	{
		if(e.getValue() < -0.2 || e.getValue() > 0.2)
		{
			Matrix3D m = new Matrix3D((-e.getValue() * speed), gup);
			Vector3D sideDir = camera.getRightAxis();
			Vector3D up = camera.getUpAxis();
			Vector3D foward = camera.getViewDirection();
			camera.setAxes(sideDir.mult(m),
					   	   up.mult(m),
					   	   foward.mult(m));
		}
	}
}
