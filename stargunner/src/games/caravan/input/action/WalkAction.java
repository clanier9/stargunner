package games.caravan.input.action;


import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import sage.scene.Model3DTriMesh;

public class WalkAction extends AbstractInputAction { 
	 private Model3DTriMesh avatar; 
	 
	 public WalkAction(Model3DTriMesh n) 
	 { avatar = n; } 
	 
	 public void performAction(float time, Event e) 
	 { 
		 avatar.stopAnimation(); 
		 avatar.startAnimation("Walk");
	 }
}
