package games.caravan.ai;

import games.caravan.character.TRex;
import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class MoveAround extends BTAction {
	private TRex npc;
	
	public MoveAround(TRex n) {
		npc = n;
	}
	
	@Override
	protected BTStatus update(float elapsedTime) {
		npc.setMoving(!(npc.isMoving()));
		
		if (npc.isMoving()) {
			npc.stopAnimation(); 
		 	npc.startAnimation("Walk");
		}
		else {
			npc.getGrowl().stop();
			npc.stopAnimation();		 
			npc.startAnimation("Roar");
			npc.getRoar().play();	
			npc.getGrowl().play();
//			while(npc.isAnimating()) {
//				
//			}
//			npc.stopAnimation();
		}
		
		return BTStatus.BH_SUCCESS;
	}
}
