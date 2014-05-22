package games.caravan;

import games.caravan.character.Bullet;
import games.caravan.character.RegularShip;
import games.caravan.character.Ship;
import games.caravan.event.NetworkShotListener;
import games.caravan.event.ShotEvent;
import games.caravan.networking.GameClientTCP;
import graphicslib3D.Point3D;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import sage.event.EventManager;
import sage.event.IEventManager;
import sage.networking.IGameConnection.ProtocolType;
import sage.scene.SceneNode;

public class CaravanNetworkingGame extends CaravanGame 
{ 
	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	private GameClientTCP thisClient;
	private boolean connected;
	private Point3D lastPosition;
	private NetworkShotListener ns;

	// assumes main() gets address/port from command line 
	public CaravanNetworkingGame(String serverAddr, int sPort) 
	{ 
		super(); 
		this.serverAddress = serverAddr; 
		this.serverPort = sPort; 
		this.serverProtocol = ProtocolType.TCP; 
		ns = new NetworkShotListener(this);
		IEventManager eventMgr = EventManager.getInstance();
		eventMgr.addListener(ns, ShotEvent.class);
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
		lastPosition = getPlayerPosition();
	} 
	
	protected void update(float time) 
	{ 
		// same as before, plus process any packets received from server 
	 	if (thisClient != null) {
	 		thisClient.processPackets(); 
	 		if (lastPosition != getPlayerPosition()) {
	 			thisClient.sendMoveMessage(getPlayerPosition());
	 			lastPosition = getPlayerPosition();
	 		}
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
	
	public void startScrolling() {
		super.startScrolling();
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
	
	public boolean removeGameWorldObject(SceneNode s) {
		return super.removeGameWorldObject(s);
	}
	
	public void fireMessage()
	{
		thisClient.sendFireMessage();
	}
}

