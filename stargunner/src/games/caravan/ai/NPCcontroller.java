package games.caravan.ai;

import sage.ai.behaviortrees.BTCompositeType;
import sage.ai.behaviortrees.BehaviorTree;

public class NPCcontroller
{
	protected BehaviorTree bt = new BehaviorTree(BTCompositeType.SELECTOR);
	protected long startTime, lastUpdateTime;
	protected boolean playerNear = false;
	
	public void startNPControl()
	{ 
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		setupNPC();
		setupBehaviorTree();
		npcLoop();
	}
	
	private void setupBehaviorTree() {
		// TODO Auto-generated method stub
		
	}

	public void setupNPC()
	{ 
		
	}
	
	public void npcLoop()
	{ 
		while (true)
		{ 
			long frameStartTime = System.nanoTime();
			float elapsedMilliSecs = (frameStartTime-lastUpdateTime)/(1000000.0f);
			if (elapsedMilliSecs >= 50.0f)
			{ 
				lastUpdateTime = frameStartTime;
				//npc.updateLocation();
				bt.update(elapsedMilliSecs);
			}	
			Thread.yield();
		}
	}

	public void setNearFlag(boolean b) {
		playerNear = b;
	}

	public boolean getNearFlag() {
		return playerNear;
	} 
	
}