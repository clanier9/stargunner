package games.caravan.controller;

import sage.scene.Controller;
import sage.scene.SceneNode;

public class ScrollController extends BaseController {

	private double translationRate;
	
	public ScrollController(double rate) {
		translationRate = rate;
	}

	@Override
	public void update(double time) {
		double distTraveled = time * translationRate;
		for (SceneNode node : controlledNodes)
		{
			node.translate(0, 0, (float)-distTraveled);
		}

	}

}
