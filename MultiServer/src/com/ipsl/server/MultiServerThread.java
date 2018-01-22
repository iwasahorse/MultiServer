package com.ipsl.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiServerThread extends Thread {
	private Socket socket = null;
	private int status;
	private static String latitude;
	private static String longitude;
	private static String altitude ;
	private static String count;
	private PrintWriter out;
	private BufferedReader in;

	public MultiServerThread(Socket socket) {
		super("MultiServerThread");
		this.socket = socket;
	}

	public void run() {
		try {

			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String inputLine;
			System.out.println("Thread created");
			
			while ((inputLine = in.readLine()) != null) {
				readStatus(inputLine);
				
				switch (status) {
				case 0: // initial
					break;
				case 1: // driver
					System.out.println("Driver App Conneted");
					setLocation(inputLine);
					break;
				case 2: // passenger
					System.out.println("Passenger App Conneted");
					sendLocation();
					break;
				case 3: //nfc
					sendNfc();
					break;
				}

			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public void readStatus(String inputLine) {
		try {
			status = Integer.parseInt(inputLine);
		} catch (NumberFormatException e) {
			status = 0; // 0: initial, error, 1: driver, 2: passenger
		}
	}

	public void setLocation(String inputLine) throws IOException {
		latitude = in.readLine();
		longitude = in.readLine();
		altitude = in.readLine();
		count= in.readLine();

		System.out.println(inputLine);
		System.out.println(latitude + ", " + longitude + ", " + altitude + ","+count);
	}

	public synchronized void sendLocation() {
		out.println(latitude);
		out.println(longitude);
		out.println(altitude);
	}
	public synchronized void sendNfc() {
		out.println(count);

	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		in.close();
		out.close();
		System.out.println("Thread destroyed");
	}
}
