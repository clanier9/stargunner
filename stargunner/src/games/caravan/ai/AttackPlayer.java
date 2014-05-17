package games.caravan.ai;

import games.caravan.character.NPC;
import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class AttackPlayer extends BTAction {
	private NPC npc;
	
	public AttackPlayer(NPC n) {
		npc = n;
	}
	
	@Override
	protected BTStatus update(float elapsedTime) {
		// TODO Auto-generated method stub
		return null;
	}

}
