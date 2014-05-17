package games.caravan.ai;

import games.caravan.character.Ship;
import games.caravan.character.UFO;
import sage.ai.behaviortrees.BTCondition;

public class PlayerBelow extends BTCondition {
	
	UFO npc;
	Ship player;
	
	public PlayerBelow(boolean toNegate, Ship s, UFO u) {
		super(toNegate);
		npc = u;
		player = s;
	}

	@Override
	protected boolean check() {
		if(player.getLocation().getZ() < npc.getLocation().getZ()) return true;
		else return false;
	}
}
