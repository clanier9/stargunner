package games.caravan.ai;

import games.caravan.CaravanGame;
import games.caravan.character.NPC;
import games.caravan.networking.GameServerTCP;
import graphicslib3D.Point3D;
import sage.ai.behaviortrees.BTCondition;

public class PlayerNear extends BTCondition {
	private NPCcontroller npcc;
	private NPC npc;
	
	private CaravanGame game;
	
	public PlayerNear(CaravanGame g, NPCcontroller c, NPC n, boolean toNegate) {
		super(toNegate);
		game = g;
		npcc = c;
		npc = n;
	} 
	
	protected boolean check() {
		Point3D npcP = new Point3D(npc.getX(),npc.getY(),npc.getZ()); 
		npcc.setNearFlag(game.checkPlayerProximity()); 
		return npcc.getNearFlag(); 
	} 
} 

