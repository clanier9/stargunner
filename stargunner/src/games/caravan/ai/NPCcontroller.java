package games.caravan.ai;

import sage.ai.behaviortrees.BTCompositeType;
import sage.ai.behaviortrees.BehaviorTree;

public class NPCcontroller
{
	private BehaviorTree bt = new BehaviorTree(BTCompositeType.SELECTOR);
	private long startTime, lastUpdateTime;
	public void startNPControl()
	{ 
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		setupNPC();
		//setupBehaviorTree();
		npcLoop();
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
	
}