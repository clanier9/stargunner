package games.caravan.ai;

import gameEngine.character.BaseCharacter;
import games.caravan.CaravanGame;
import games.caravan.character.NPC;
import sage.ai.behaviortrees.BTCompositeType;
import sage.ai.behaviortrees.BTCondition;
import sage.ai.behaviortrees.BTSequence;
import sage.ai.behaviortrees.BehaviorTree;

public class BossController extends NPCcontroller {
	private CaravanGame game;
	private NPC boss;
	
	public BossController(CaravanGame g, NPC b) {
		game = g;
		boss = b;
	}
	
	public void startBossControl() { 
		super.startNPControl();
	}

	private void setupBehaviorTree() {
		bt.insertAtRoot(new BTSequence(10)); 
		bt.insertAtRoot(new BTSequence(20)); 
		bt.insert(10, new FiveSecPassed(this, boss, false)); 
		bt.insert(10, new MoveAround(boss)); 
		bt.insert(20, new PlayerNear(game, this, boss, false)); 
		bt.insert(20, new AttackPlayer(boss)); 
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
				boss.updateLocation();
				bt.update(elapsedMilliSecs);
			}	
			Thread.yield();
		}
	}
}
