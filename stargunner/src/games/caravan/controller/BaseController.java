package games.caravan.controller;

import sage.scene.Controller;
import sage.scene.SceneNode;

public abstract class BaseController extends Controller {

	public BaseController() {
		
	}

	public void update(double arg0) {

	}
	
	public void removeNode(SceneNode s)
	{
		controlledNodes.remove(s);
	}

}
