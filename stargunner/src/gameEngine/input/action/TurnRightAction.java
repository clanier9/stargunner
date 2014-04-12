package gameEngine.input.action;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;

public class TurnRightAction extends AbstractInputAction  {

	private ICamera camera;
	private Vector3D gup = new Vector3D(0,1,0);
	
	public TurnRightAction(ICamera c) {
		camera = c;
	}

	public void performAction(float arg0, Event arg1) {
		float rotAmount = 0.5f;
		
		Matrix3D m = new Matrix3D(-rotAmount, gup);
		Vector3D right = camera.getRightAxis();
		Vector3D up = camera.getUpAxis();
		Vector3D foward = camera.getViewDirection();
		
		camera.setAxes(right.mult(m),
		   	   	   up.mult(m),
		   	       foward.mult(m));
	}

}
