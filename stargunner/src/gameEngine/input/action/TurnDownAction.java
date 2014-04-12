package gameEngine.input.action;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;

public class TurnDownAction extends AbstractInputAction  {

	private ICamera camera;
	
	public TurnDownAction(ICamera c) {
		camera = c;
	}

	public void performAction(float arg0, Event arg1) 
	{
		float rotAmount = 0.5f;
		
		Vector3D right = camera.getRightAxis();
		Matrix3D m = new Matrix3D(-rotAmount, right);
		Vector3D up = camera.getUpAxis();
		Vector3D foward = camera.getViewDirection();
		
		camera.setAxes(right.mult(m),
			   	   	   up.mult(m),
			   	       foward.mult(m));
		
	}

}
