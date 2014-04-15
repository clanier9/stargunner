package games.caravan.networking;

import graphicslib3D.Point3D;

import java.io.IOException; 
import java.net.InetAddress; 
import java.util.UUID; 
 

import sage.networking.server.GameConnectionServer; 
import sage.networking.server.IClientInfo; 

public class GameServerTCP extends GameConnectionServer<UUID> 
{ 
	public GameServerTCP(int localPort) throws IOException 
	{ 
		super(localPort, ProtocolType.TCP);
	} 
	 
	public void acceptClient(IClientInfo ci, Object o) 
	{ 
		if (getClients().size()==2) { return; } //only 2 players allowed
		
		String message = (String)o; 
		String[] messageTokens = message.split(","); 
	 
		if(messageTokens.length > 0) 
		{ 
			if(messageTokens[0].compareTo("join") == 0) // received �join� 
			{ 
				System.out.println("join received");
				// format: join,localid 
				UUID clientID = UUID.fromString(messageTokens[1]); 
				sendJoinedMessage(clientID, true);
				addClient(ci, clientID);  
			} 
		} 
	} 
	
	public boolean isConnected() {
		return (!getClients().isEmpty());
	}

	public void processPacket(Object o, InetAddress senderIP, int sndPort) 
	{ 
		String message = (String) o; 
		String[] msgTokens = message.split(","); 
	 
		if(msgTokens.length > 0) 
		{ 
			if(msgTokens[0].compareTo("bye") == 0) // receive �bye� 
			{ 	
				// format: bye,localid 
				UUID clientID = UUID.fromString(msgTokens[1]); 
				sendByeMessages(clientID); 
				removeClient(clientID); 
			} 
	 
			if(msgTokens[0].compareTo("create") == 0) // receive �create� 
			{ // format: create,localid,x,y,z 
				UUID clientID = UUID.fromString(msgTokens[1]); 
				Point3D pos = new Point3D(Integer.parseInt(msgTokens[2]), Integer.parseInt(msgTokens[3]), Integer.parseInt(msgTokens[4])); 
				sendCreateMessages(clientID, pos); 
//				sendWantsDetailsMessages(clientID); 
			} 
		
			if(msgTokens[0].compareTo("dsfr") == 0) // receive �details for� 
			{ 
				 
			}
		
			if(msgTokens[0].compareTo("move") == 0) // receive �move� 
			{ 
				 
			}
		}
	}
		 
	public void sendJoinedMessage(UUID clientID, boolean success) 
	{ 
		// format: join, success or join, failure 
		try 
		{ 
			String message = new String("join,"); 
			if (success) message += "success"; 
			else message += "failure"; 
			sendPacket(message, clientID); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		}
	} 
	 
	public void sendCreateMessages(UUID clientID, Point3D position) 
	{ 
		// format: create, remoteId, x, y, z 
		try 
		{ 
			String message = new String("create," + clientID.toString()); 
			message += "," + position.getX(); 
			message += "," + position.getY(); 
			message += "," + position.getZ(); 
			forwardPacketToAll(message, clientID); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 
	} 
	 
	public void sndDetailsMsg(UUID clientID, UUID remoteId, String[] position) 
	{  } 
	 
	public void sendWantsDetailsMessages(UUID clientID) 
	{  
		
	} 
	 
	public void sendMoveMessages(UUID clientID, String[] position) 
	{  } 
	  
	public void sendByeMessages(UUID clientID) 
	{  
		try 
		{ 
			String message = new String("bye," + clientID); 
			forwardPacketToAll(message, clientID); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
}

