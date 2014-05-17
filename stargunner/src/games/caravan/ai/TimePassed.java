package games.caravan.ai;

import sage.ai.behaviortrees.BTCondition;

public class TimePassed extends BTCondition
{
	private NPCcontroller controller;
	private long lastUpdateTime;
	private long fireRate;

	public TimePassed(NPCcontroller c, long milliseconds, boolean toNegate)
	{ 
		super(toNegate);
		controller = c;
		fireRate = milliseconds;
		lastUpdateTime = System.nanoTime();
	}
	protected boolean check()
	{ 
		float elapsedMilliSecs = (System.nanoTime()-lastUpdateTime)/(1000000.0f);
		if (elapsedMilliSecs >= fireRate)
		{ 
			lastUpdateTime = System.nanoTime();
			return true;
		}
		else return false;
	} 
}
