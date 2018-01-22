package com.ipsl.server;

import java.io.IOException;
import java.net.ServerSocket;

public class MultiServer {
	public static void main(String[] args) throws IOException {

		int portNumber = 8888;//Ãµ¾È¿ª
		boolean listening = true;

		try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
			while (listening) {
				new MultiServerThread(serverSocket.accept()).start();
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port " + portNumber);
			System.exit(-1);
		}
	}
}
