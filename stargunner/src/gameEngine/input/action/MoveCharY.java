package gameEngine.input.action;

import gameEngine.character.BaseCharacter;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;

public class MoveCharY extends AbstractInputAction {

	
	private BaseCharacter player;
	
	public MoveCharY(BaseCharacter p) {
		player = p;
	}

	public void performAction(float time, Event evt) {
		float sensitivity = player.getSpeed();
		if(evt.getValue() > 0.2 || evt.getValue() < -0.2)
		{
			float move = (-evt.getValue() * time * sensitivity);
			player.moveFoward(move);
		}

	}

}
