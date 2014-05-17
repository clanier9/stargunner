package games.caravan.input.action;


import net.java.games.input.Event;
import sage.audio.Sound;
import sage.input.action.AbstractInputAction;
import sage.scene.Model3DTriMesh;

public class RoarAction extends AbstractInputAction { 
	 private Model3DTriMesh avatar; 
	 private Sound roar;
	 
	 public RoarAction(Model3DTriMesh n, Sound s) { 
		 avatar = n; 
		 roar = s;
	 } 
	 
	 public void performAction(float time, Event e) 
	 { 
		 avatar.stopAnimation(); 
		 roar.stop();
		 avatar.startAnimation("Roar");
		 roar.play();
	 }
}
