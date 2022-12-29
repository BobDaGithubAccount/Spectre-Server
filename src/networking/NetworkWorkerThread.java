package networking;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

import logging.Logger;

public class NetworkWorkerThread extends Thread {

	Socket socket;
	UUID uuid;
	public boolean isRunning = true;
	
	public NetworkWorkerThread(Socket s, UUID uuid) {
		this.socket = s;
		this.uuid = uuid;
	}
	
	@Override
	public void run() {
		try {
			Logger.log("Player with UUID " + uuid + " connected with an I.P address of " + socket.getInetAddress());
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			while(isRunning) {
				
			}
			is.close();
			os.close();
			socket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		isRunning = false;
	}
	
}
