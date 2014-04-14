package a3;

import java.io.IOException;
import java.util.Scanner;

import gameEngine.networking.GameServerTCP;
import games.caravan.CaravanGame;
import games.caravan.MyNetworkingClient;

public class Starter {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException 
	{
		System.out.print("1 or 2 players for this game?  ");
		Scanner s = new Scanner(System.in);
		int numPlayers = s.nextInt();
		if (numPlayers == 1) {
			CaravanGame t = new CaravanGame();
			t.start();
		}
		else if (numPlayers == 2) {
			System.out.print("Are you going to host this game?  ");
			String str = s.nextLine();
			if (str.charAt(0) == 'y') {
				int localPort = 1550;
				GameServerTCP server = new GameServerTCP(localPort);
				server.getLocalInetAddress();
				System.out.print("waiting for the client to connect to " + server.getLocalInetAddress() + " on port " + localPort + "...");
				while (!server.hasConnection()) {
					//wait for client to connect
				}
				CaravanGame t = new CaravanGame();
				t.start();
			}
			else if (str.charAt(0) == 'n') {
				System.out.print("What is the server IP address?  ");
				String serverIP = s.nextLine();
				System.out.print("What is the server port number?  ");
				int port = s.nextInt();
				MyNetworkingClient client = new MyNetworkingClient(serverIP, port);
				while (!client.isConnected()) {
					//wait for the connection to finish
				}			
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
}
