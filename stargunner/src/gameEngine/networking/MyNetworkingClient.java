package gameEngine.networking;

import gameEngine.character.BaseCharacter;
import graphicslib3D.Vector3D;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import sage.app.BaseGame;
import sage.networking.IGameConnection.ProtocolType;

public class MyNetworkingClient extends BaseGame 
{ 
	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	private MyClient thisClient;

	// assumes main() gets address/port from command line 
	public MyNetworkingClient(String serverAddr, int sPort) 
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
			thisClient = new MyClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this); 
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
	} 
	
	protected void update(float time) 
	{ 
		// same as before, plus process any packets received from server 
	 	if (thisClient != null) thisClient.processPackets(); 
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
	
	public class GhostAvatar extends BaseCharacter
	{

	}

	public void setIsConnected(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public Vector3D getPlayerPosition() {
		// TODO Auto-generated method stub
		return null;
	}
}

