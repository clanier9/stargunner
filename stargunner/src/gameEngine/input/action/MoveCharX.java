package gameEngine.input.action;

import gameEngine.character.BaseCharacter;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;

public class MoveCharX extends AbstractInputAction {

	BaseCharacter play;
	
	public MoveCharX(BaseCharacter c) {
		play = c;
	}

	public void performAction(float time, Event evt) {
		float sensitivity = play.getSpeed();
		if(evt.getValue() > 0.2 || evt.getValue() < -0.2)
		{
			float move = (evt.getValue() * time * sensitivity);
			play.strafeRight(move);
		}

	}

}
