package games.caravan.networking;

import games.caravan.CaravanNetworkingGame;
import games.caravan.CaravanNetworkingGame.GhostAvatar;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import java.util.Vector;

import sage.networking.client.GameConnectionClient;

public class GameClientTCP extends GameConnectionClient 
{ 
	private CaravanNetworkingGame game; 
	private UUID id; 
	private Vector<GhostAvatar> ghostAvatars;
 
	public GameClientTCP(InetAddress remAddr, int remPort, ProtocolType pType, CaravanNetworkingGame game) throws IOException 
	{ 
		super(remAddr, remPort, pType); 
		this.game = game; 
		this.id = UUID.randomUUID(); 
		this.ghostAvatars = new Vector<GhostAvatar>(); 
	} 
	
	protected void processPacket(Object msg) // override 
	{ 
		// extract incoming message into substrings. Then process: 
		String message = (String) msg; 
		System.out.println(msg);
		String[] msgTokens = message.split(","); 
	 
		if(msgTokens.length > 0) 
		{ 		
			if(msgTokens[0].compareTo("join") == 0) 
			{
				System.out.println("a join message was received");
				// receive “join” 
				// format: join, success or join, failure 			
				if(msgTokens[1].compareTo("success") == 0) 
				{ 
					game.setConnected(true); 
					sendCreateMessage(game.getPlayerPosition()); 
					System.out.println("Connected successfully!"); 
				} 
				else if(msgTokens[1].compareTo("failure") == 0) 
					game.setConnected(false); 
			} 
			if(msgTokens[0].compareTo("bye") == 0) // receive “bye” 
			{ 
				// format: bye, remoteId 
				UUID ghostID = UUID.fromString(msgTokens[1]); 
				removeGhostAvatar(ghostID); 
			} 
			if(msgTokens[0].compareTo("create") == 0) // receive “create…” 
			{  
				System.out.println("create message received from the server!"); 
				// format: create, remoteId, x,y,z or dsfr, remoteId, x,y,z 
				UUID ghostID = UUID.fromString(msgTokens[1]); 
				// extract ghost x,y,z, position from message, then: 
				Point3D ghostPosition = new Point3D(Double.parseDouble(msgTokens[2]), Double.parseDouble(msgTokens[3]), Double.parseDouble(msgTokens[4])); 
				if (ghostID != id) {
					createGhostAvatar(ghostID, ghostPosition);
				}
				 
			} 
			if(msgTokens[0].compareTo("move") == 0) // receive “move” 
			{ 
				UUID ghostID = UUID.fromString(msgTokens[1]); 
				// extract ghost x,y,z, position from message, then: 
				Point3D ghostPosition = new Point3D(Double.parseDouble(msgTokens[2]), Double.parseDouble(msgTokens[3]), Double.parseDouble(msgTokens[4]));
				moveGhostAvatar(ghostID, ghostPosition);
			} 
			if (msgTokens[0].compareTo("dsfr") == 0) // receive “details for” 
			{
				boolean exists = false;
				// format: dsfr, remoteId, x,y,z 
				UUID ghostID = UUID.fromString(msgTokens[1]);
				Point3D location = new Point3D(Double.parseDouble(msgTokens[2]), Double.parseDouble(msgTokens[3]), Double.parseDouble(msgTokens[4]));
				for (int i=0; i<ghostAvatars.size(); i++) {
					if (ghostID == ghostAvatars.get(i).getId()) {
						ghostAvatars.get(i).setLocation(location);
						exists = true;
						break;
					}
				}
				if (!exists && ghostID != id) {
					createGhostAvatar(ghostID, location);
				}
			} 
			if(msgTokens[0].compareTo("wsds") == 0) // receive “wants details” 
			{ 
				Point3D pos = game.getPlayerPosition();
				UUID remID = UUID.fromString(msgTokens[1]);
				sendDetailsForMessage(remID, pos);
			} 
		}
	} 

	private void createGhostAvatar(UUID ghostID, Point3D ghostPosition) {
		GhostAvatar p2 = game.new GhostAvatar(ghostID, ghostPosition);
		ghostAvatars.add(p2);
		game.addGameWorldObject(p2);
	}

	private void removeGhostAvatar(UUID ghostID) {
		for (int i=0; i<ghostAvatars.size(); i++) {
			if (ghostID == ghostAvatars.get(i).getId()) {
				ghostAvatars.remove(i);
				break;
			}
		}		
	}
	
	private void moveGhostAvatar(UUID ghostID, Point3D ghostPosition) {
		for (int i=0; i<ghostAvatars.size(); i++) {
			if (ghostID == ghostAvatars.get(i).getId()) {
				ghostAvatars.get(i).move(ghostPosition);
				break;
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
			System.out.println("a create message was sent to server");
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
	{  
		try 
		{ 
			String message = new String("bye," + id.toString()); 
			sendPacket(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	} 
	
	public void sendDetailsForMessage(UUID remId, Point3D pos) 
	{
		try 
		{ 
			String message = new String("dsfr," + id + "," + remId);
			message += "," + pos.getX(); 
			message += "," + pos.getY(); 
			message += "," + pos.getZ(); 
			sendPacket(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
	
	public void sendMoveMessage(Vector3D pos) 
	{
		
	}
}
