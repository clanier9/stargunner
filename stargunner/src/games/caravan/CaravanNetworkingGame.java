package games.caravan;

import games.caravan.character.Bullet;
import games.caravan.character.RegularShip;
import games.caravan.character.Ship;
import games.caravan.networking.GameClientTCP;
import graphicslib3D.Point3D;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import sage.networking.IGameConnection.ProtocolType;
import sage.scene.SceneNode;

public class CaravanNetworkingGame extends CaravanGame 
{ 
	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	private GameClientTCP thisClient;
	private boolean connected;

	// assumes main() gets address/port from command line 
	public CaravanNetworkingGame(String serverAddr, int sPort) 
	{ 
		super(); 
		this.serverAddress = serverAddr; 
		this.serverPort = sPort; 
		this.serverProtocol = ProtocolType.TCP; 
	} 
	
	protected void initGame() 
	{ 
		// items as before, plus initializing network: 
		try 
		{ 
			thisClient = new GameClientTCP(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this); 
		} 
		catch (UnknownHostException e) 
		{ 
			e.printStackTrace(); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 
		if (thisClient != null) { thisClient.sendJoinMessage(); } 
		super.initGame();
	} 
	
	protected void update(float time) 
	{ 
		// same as before, plus process any packets received from server 
	 	if (thisClient != null) {
	 		thisClient.processPackets(); 
	 		thisClient.sendMoveMessage(getPlayerPosition());
	 	}
	 	
	 	super.update(time);
	} 
	
	protected void shutdown() 
	{ 
		super.shutdown(); 
		if(thisClient != null) 
		{ 
			thisClient.sendByeMessage(); 
			try 
			{ 	// shutdown() is inherited 
				thisClient.shutdown(); 
			} 			
			catch (IOException e) 
			{ 
				e.printStackTrace(); 
			} 
		} 
	} 	
	
	public void setConnected(boolean b) {
		connected = b;
	}
	
	public boolean isConnected() {
		return connected;
	}

	public Point3D getPlayerPosition() {
		return getPlayer().getLocation();
	}
	
	public void addGameWorldObject(SceneNode s) {
		super.addGameWorldObject(s);
	}
}
	
//	public class GhostAvatar extends RegularShip
//	{
////		Ship p1;
//		
//		public GhostAvatar(UUID ghostID, Point3D ghostPosition) {
//			id=ghostID;
//			setLocation(ghostPosition);
//		}
//		
//		@Override
//		public Bullet[] fire() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		public void move(Point3D ghostPosition) {
//			setLocation(ghostPosition);
//		}
//	}
//}

