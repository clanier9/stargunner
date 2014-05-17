package games.caravan.ai;

import games.caravan.character.NPC;
import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class MoveAround extends BTAction {
	private NPC npc;
	
	public MoveAround(NPC n) {
		npc = n;
	}
	
	@Override
	protected BTStatus update(float elapsedTime) {
		// TODO Auto-generated method stub
		return null;
	}
}
