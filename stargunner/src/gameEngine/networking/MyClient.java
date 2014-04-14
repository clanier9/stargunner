package gameEngine.networking;

import gameEngine.networking.MyNetworkingClient.GhostAvatar;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import java.util.Vector;

import sage.networking.client.GameConnectionClient;

public class MyClient extends GameConnectionClient 
{ 
	private MyNetworkingClient game; 
	private UUID id; 
	private Vector<GhostAvatar> ghostAvatars;
 
	public MyClient(InetAddress remAddr, int remPort, ProtocolType pType, MyNetworkingClient game) throws IOException 
	{ 
		super(remAddr, remPort, pType); 
		this.game = game; 
		this.id = UUID.randomUUID(); 
		this.ghostAvatars = new Vector<GhostAvatar>(); 
	} 
	
	protected void processPacket (Object msg) // override 
	{ 
		// extract incoming message into substrings. Then process: 
		String message = (String) msg; 
		String[] msgTokens = message.split(","); 
	 
		if(msgTokens.length > 0) 
		{ 		
			if(msgTokens[0].compareTo("join") == 0) 
			{
				// receive “join” 
				// format: join, success or join, failure 			
				if(msgTokens[1].compareTo("success") == 0) 
				{ 
					//game.setConnected(true); 
					//sendCreateMessage(game.getPlayerPosition()); 
					game.setConnected(true); 
					System.out.println("Connected successfully!");
//					sendCreateMessage(game.getPlayerPosition()); 
				} 
				if(msgTokens[1].compareTo("failure") == 0) 
					game.setConnected(false); 
			} 
			if(msgTokens[0].compareTo("bye") == 0) // receive “bye” 
			{ 
				// format: bye, remoteId 
				UUID ghostID = UUID.fromString(msgTokens[1]); 
				removeGhostAvatar(ghostID); 
			} 
			if (msgTokens[0].compareTo("dsfr") == 0 ) 
			{
				// receive “details for” 
				// format: create, remoteId, x,y,z or dsfr, remoteId, x,y,z 
				UUID ghostID = UUID.fromString(msgTokens[1]); 
				// extract ghost x,y,z, position from message, then: 
				UUID ghostPosition = UUID.fromString(msgTokens[1]); 
				createGhostAvatar(ghostID, ghostPosition); 
			} 
			if(msgTokens[0].compareTo("wsds") == 0) // receive “create…” 
			{  } 
			if(msgTokens[0].compareTo("wsds") == 0) // receive “wants…” 
			{  } 
			if(msgTokens[0].compareTo("move") == 0) // receive “move” 
			{  } 
		}
	} 
		
	private void createGhostAvatar(UUID ghostID, UUID ghostPosition) {
		// TODO Auto-generated method stub
		
	}

	private void removeGhostAvatar(UUID ghostID) {
		for (GhostAvatar gA : ghostAvatars) {
			if (gA.getId()==ghostID) {
				gA = null;
			}
		}
	}

	public void sendCreateMessage(Point3D pos) 
	{	
		// format: (create, localId, x,y,z) 
		try 
		{ 
			String message = new String("create," + id.toString()); 
			message += "," + pos.getX()+"," + pos.getY() + "," + pos.getZ(); 
			sendPacket(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
	 
	public void sendJoinMessage() 
	{
		// format: join, localId 
		try 
		{ 
			sendPacket(new String("join," + id.toString()));
		}
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
	 
	
	public void sendByeMessage() 
	{  } 
	
	public void sendDetailsForMessage(UUID remId, Vector3D pos) 
	{
		
	}
	
	public void sendMoveMessage(Vector3D pos) 
	{
		
	}
}
