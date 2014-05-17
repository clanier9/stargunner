package games.caravan.ai;

import games.caravan.CaravanGame;
import games.caravan.character.TRex;
import sage.ai.behaviortrees.BTSequence;

public class BossController extends NPCcontroller {
	private CaravanGame game;
	private TRex boss;
	
	public BossController(CaravanGame g, TRex b) {
		game = g;
		boss = b;
		startTime = System.nanoTime(); 
		lastUpdateTime = startTime; 
		setupBehaviorTree(); 
	}

	private void setupBehaviorTree() {
		bt.insertAtRoot(new BTSequence(10)); 
		bt.insertAtRoot(new BTSequence(20)); 
		bt.insert(10, new FiveSecPassed(this, boss, false)); 
		bt.insert(10, new MoveAround(boss)); 
		bt.insert(20, new PlayerNear(game, this, boss, false)); 
		bt.insert(20, new AttackPlayer(boss)); 
	}
	
	public void update(float eTime)
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
