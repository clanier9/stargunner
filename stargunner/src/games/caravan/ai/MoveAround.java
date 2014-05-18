package games.caravan.ai;

import java.util.Random;

import games.caravan.character.TRex;
import graphicslib3D.Matrix3D;
import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class MoveAround extends BTAction {
	private TRex npc;
	private Random rand;
	
	public MoveAround(TRex n) {
		npc = n;
		rand = new Random();
	}
	
	@Override
	protected BTStatus update(float elapsedTime) {
		npc.setMoving(!(npc.isMoving()));
		
		if (npc.isMoving()) {
			npc.getRoar().stop();
			npc.stopAnimation(); 
			npc.rotate((double)rand.nextInt(360));
		 	npc.startAnimation("Walk");
		 	npc.getGrowl().play();
		}
		else {
			npc.getGrowl().stop();
			npc.stopAnimation();		 
			npc.startAnimation("Roar");
			npc.getRoar().play();
		}
		
		return BTStatus.BH_SUCCESS;
	}
}
