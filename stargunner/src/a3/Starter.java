package a3;

import java.io.IOException;
import java.util.Scanner;

import games.caravan.CaravanGame;
import games.caravan.CaravanNetworkingGame;
import games.caravan.networking.GameServerTCP;

public class Starter {
	private static final int localPort = 1550;
	private static int numPlayers;
		
	public static void main(String[] args) throws IOException 
	{
		Scanner s = new Scanner(System.in);
		System.out.print("How many players for this game?  ");
		numPlayers = s.nextInt();
		if (numPlayers == 1) {
			CaravanGame g = new CaravanGame();
			g.start();
			g.startScrolling();
		}
		else if (numPlayers == 2) {
			Scanner kb = new Scanner(System.in);
			System.out.print("Are you going to host this game?  ");
			String str = kb.nextLine();
			if (str.charAt(0) == 'y') {
				GameServerTCP server = new GameServerTCP(localPort);
				server.getLocalInetAddress();
				System.out.print("waiting for the client to connect to " + server.getLocalInetAddress() + " on port " + localPort + "...");

				String[] msgTokens = server.getLocalInetAddress().toString().split("/"); 				
				CaravanNetworkingGame serverClient = new CaravanNetworkingGame(msgTokens[1], localPort);		
				serverClient.start();
			}
			else if (str.charAt(0) == 'n') {
				System.out.print("What is the server IP address?  ");
				String serverIP = kb.nextLine();
				CaravanNetworkingGame client = new CaravanNetworkingGame(serverIP, localPort);		
				client.start();
			}
			else {
				System.out.println("Invalid input. Exiting...");
				System.exit(0);
			}
		}
		else {
			System.out.println("Invalid input. Exiting...");
			System.exit(0);
		}
	}
	
	public static int getNumPlayers() { 
		return numPlayers;
	}
}
