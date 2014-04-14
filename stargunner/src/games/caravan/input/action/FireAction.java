package games.caravan.input.action;

import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import games.caravan.character.Ship;

public class FireAction extends  AbstractInputAction {
	
	private Ship ship;

	public FireAction(Ship s) {
		ship = s;
	}

	@Override
	public void performAction(float arg0, Event arg1) {
		// TODO Auto-generated method stub
		
	}

}
