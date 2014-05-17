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
		 //set walking boolean to true
		 //walk to desired location
		 //play roar sound and animation
		 //stop sound and animations
		 //set walking boolean to false
	 }
}
