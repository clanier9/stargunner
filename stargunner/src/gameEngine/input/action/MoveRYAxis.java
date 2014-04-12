package gameEngine.input.action;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;

public class MoveRYAxis extends AbstractInputAction
{
	private ICamera camera;
	private float speed;
	public MoveRYAxis(ICamera c, float s)
	{ 
		camera = c;
		speed = s;
	}
	public void performAction(float time, net.java.games.input.Event e)
	{
		if(e.getValue() < -0.2 || e.getValue() > 0.2)
		{
			Vector3D sideDir = camera.getRightAxis().normalize();
		
			Matrix3D m = new Matrix3D((-e.getValue() * speed), sideDir);
			Vector3D up = camera.getUpAxis();
			Vector3D foward = camera.getViewDirection();
			camera.setAxes(sideDir.mult(m),
					   	   up.mult(m),
					   	   foward.mult(m));
		}
		
	} 
}
