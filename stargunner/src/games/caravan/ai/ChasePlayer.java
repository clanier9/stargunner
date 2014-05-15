package games.caravan.ai;

import games.caravan.character.Ship;
import games.caravan.character.UFO;
import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class ChasePlayer extends BTAction {
	
	Ship player;
	UFO npc;
	
	public ChasePlayer(Ship s, UFO u) {
		npc = u;
		player = s;
	}


	protected BTStatus update(float timeInMillisec) {
		npc.lookAt(player.getLocation());
		return BTStatus.BH_SUCCESS;
	}

}
