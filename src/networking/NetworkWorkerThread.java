package networking;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import lib.json.JSONObject;
import logging.Logger;

public class NetworkWorkerThread extends Thread {

	Socket socket;
	InputStream is;
	OutputStream os;
	UUID uuid;
	public boolean isRunning = true;
	
	public NetworkWorkerThread(Socket s, UUID uuid) {
		this.socket = s;
		this.uuid = uuid;
	}

	Timer timer = new Timer();
	public long a = 0;
	class TimeoutTask extends TimerTask {
		long b;
		TimeoutTask(long a) {
			this.b = a;
		}
		@Override
		public void run() {
			if(a == b && isRunning) {
				try {
					isRunning = false;
					is.close();
					os.close();
					socket.close();
					Logger.log("Player with UUID " + uuid + " and I.P address of " + socket.getInetAddress() + " has timed out!");
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			Logger.log("Player with UUID " + uuid + " has attempted to connect with an I.P address of " + socket.getInetAddress());
			is = socket.getInputStream();
			os = socket.getOutputStream();
			sendJSON(Packet.SStatusPacket());
			while(socket.isConnected() && socket.isBound() && !socket.isClosed()) {
				a++;
				timer.schedule(new TimeoutTask(a), 5000);
				JSONObject json = receiveJSON();
				if(json==null) {continue;}
				System.out.println(json.toString());
			}
			if(!isRunning) {
				return;
			}
			is.close();
			os.close();
			socket.close();
			Logger.log("Player with UUID " + uuid + " and I.P address of " + socket.getInetAddress() + " has disconnected!");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendJSON(JSONObject jsonObject) {
		try {
			ObjectOutputStream o = new ObjectOutputStream(os);
	        o.writeObject(jsonObject.toString());
	        os.flush();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject receiveJSON() {
		try {
			InputStream in = socket.getInputStream();
			ObjectInputStream i = new ObjectInputStream(in);
			String line = null;
			line = (String) i.readObject();
			JSONObject jsonObject = new JSONObject(line);
			return jsonObject;
		} catch(Exception e) {
			return null;
		}
    }
	
	//TODO STOP WEB BROWSERS CREATING GHOST CLIENTS, SHUTDOWN THREAD CORRECTLY
	
}
