package games.caravan.ai;

import games.caravan.character.NPC;
import sage.ai.behaviortrees.BTCondition;

public class FiveSecPassed extends BTCondition {
	private NPCcontroller npcc;
	private NPC npc;
	
	private long lastUpdateTime;
	
	public FiveSecPassed(NPCcontroller c, NPC n, boolean toNegate) { 
		super(toNegate); 
		npcc = c; 
		npc = n; 
		lastUpdateTime = System.nanoTime(); 
	} 
	
	protected boolean check() { 
		float elapsedMilliSecs = (System.nanoTime()-lastUpdateTime)/(1000000.0f); 
		
		if (elapsedMilliSecs >= 5000.0f) { 
			lastUpdateTime = System.nanoTime(); 
			npcc.setNearFlag(false); 
			return true; 
		} 
		else return false; 
	}
}
