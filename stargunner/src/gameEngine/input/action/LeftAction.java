package gameEngine.input.action;

import gameEngine.character.BaseCharacter;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;

public class LeftAction extends AbstractInputAction {

	private BaseCharacter player;
	public LeftAction(BaseCharacter b) {
		player = b;
	}

	@Override
	public void performAction(float time, Event evt) 
	{
		float sensitivity = player.getSpeed();
		if (evt.getValue() > 0.2) 
		{ 
			float move = (evt.getValue() * time * sensitivity);
			player.strafeRight(-move);
		}

	}

}
