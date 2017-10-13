import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class WuuInstance {
	ServerSocket socket;
	Integer port;
	ArrayList<Socket> clients; //sends
	ArrayList<Socket> hosts; //receives
	
	public WuuInstance(Integer portNumber) {
		port = portNumber;		
		clients = new ArrayList<Socket>();
		hosts = new ArrayList<Socket>();
	}
	
	public String getHostName() {
		return socket.getInetAddress().getHostName();
	}
	
	public void listen() {
		System.out.println("Listening on port " + port);
		try (
			ServerSocket serverSocket = new ServerSocket(port);
			) {					
				socket = serverSocket;
				Socket clientSocket = socket.accept();
				clientSocket.setKeepAlive(true);
				
				receiveMessages(clientSocket);
			} catch (IOException e) {
				System.out.println("Exception caught listening for a connection to server on port " + port);
				System.out.println(e.getMessage());
			}
	}
	
	public void receiveMessages(Socket clientSocket) {
		while (true) {
			try {
				if (clientSocket != null) {
					PrintWriter sendToClient = new PrintWriter(clientSocket.getOutputStream(), true);                   
					BufferedReader receiveFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));



					clients.add(clientSocket);
					System.out.println("Received communication from client. Port " + clientSocket.getLocalPort());
					sendToClient.println("Host received communication from client.");

					String line = receiveFromClient.readLine();
					if (line != null) {
						System.out.println(line);
					}
				}
			}
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void connectTo(String host, Integer hostPort) {
		System.out.println("Connecting to host " + host);
		try (
			Socket hostSocket = new Socket(host, hostPort);
		) {
			if (hostSocket != null) {
				PrintWriter sendToHost = new PrintWriter(hostSocket.getOutputStream(), true);
				BufferedReader receiveFromHost = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
				sendToHost.println("Client port " + port + " connected to host " + hostPort + ".");
				hosts.add(hostSocket);
				System.out.println("Connected self to host " + host);
			}
			else {
				System.out.println("Message fell on deaf ears....");
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + host);
		} catch (IOException e) {
			System.out.println("Message fell on deaf ears....");
		}
	}
	
}