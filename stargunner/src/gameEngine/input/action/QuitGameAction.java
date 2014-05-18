package gameEngine.input.action;

import games.caravan.CaravanGame;
import sage.input.action.AbstractInputAction;
import sage.app.AbstractGame;
import net.java.games.input.Event;

public class QuitGameAction extends AbstractInputAction
{
	private AbstractGame game;
	
	public QuitGameAction(AbstractGame g) 
	{ 
		this.game = g; 
	}

	public void performAction(float time, Event event)
	{ 
		((CaravanGame) game).setSoundsOff();
		game.setGameOver(true); 
	}
}
