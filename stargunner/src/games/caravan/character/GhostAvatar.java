package games.caravan.character;

import graphicslib3D.Point3D;

import java.util.UUID;

public class GhostAvatar extends FighterJet
{
//	Ship p1;
	
	public GhostAvatar(UUID ghostID, Point3D ghostPosition) {
		id=ghostID;
		setLocation(ghostPosition);
	}

	public void move(Point3D ghostPosition) {
		setLocation(ghostPosition);
	}
}

