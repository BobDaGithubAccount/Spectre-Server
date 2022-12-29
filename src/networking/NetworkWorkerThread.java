package networking;

import java.io.*;
import java.net.Socket;
import java.util.*;

import config.Settings;
import event.EventHandler;
import lib.json.JSONObject;
import logging.Logger;

public class NetworkWorkerThread extends Thread {

	public Socket socket;
	public InputStream is;
	public OutputStream os;
	public UUID connectionUUID;
	public boolean isRunning = true;
	public boolean hasReceivedGenericDisconnectPacket = false;
	
	public NetworkWorkerThread(Socket s, UUID uuid) {
		this.socket = s;
		this.connectionUUID = uuid;
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
					if(!hasReceivedGenericDisconnectPacket) {
						EventHandler.pollPacket(Packet.CDisconnectPacket(), NetworkWorkerThread.this);
					}
					Logger.log("Player with Connection UUID " + connectionUUID + " and I.P address of " + socket.getInetAddress() + " has timed out!");
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}

	class TickTask extends TimerTask {
		@Override
		public void run() {
			sendJSON(Packet.SPingPacket());
			timer.schedule(new TickTask(), 1000);
		}
	}

	public void shutdown(String reason) {
		Logger.log("SHUTTING DOWN THREAD " + connectionUUID + " DUE TO REASON: " + reason);
		try {
			isRunning = false;
			is.close();
			os.close();
			socket.close();
			if(!hasReceivedGenericDisconnectPacket) {
				EventHandler.pollPacket(Packet.CDisconnectPacket(), this);
			}
		} catch (Exception e) {
			System.exit(-1);
		}
	}

	@Override
	public void run() {
		try {
			Logger.log("Player with Connection UUID " + connectionUUID + " has attempted to connect with an I.P address of " + socket.getInetAddress());
			is = socket.getInputStream();
			os = socket.getOutputStream();
			timer.schedule(new TickTask(), 1000);
			sendJSON(Packet.SStatusPacket());
			while(socket.isConnected() && socket.isBound() && !socket.isClosed()) {	//TODO FIX PROBLEM WHERE PACKETS ARE SKIPPED FROM CLIENT DUE TO NON 1:1 I/O
				a++;
				timer.schedule(new TimeoutTask(a), 5000);
				JSONObject json = receiveJSON();
				if(json==null) {continue;}
				System.out.println(json.toString(1));
				EventHandler.pollPacket(json, this);
			}
			if(!hasReceivedGenericDisconnectPacket) {
				EventHandler.pollPacket(Packet.CDisconnectPacket(), this);
			}
			if(!isRunning) {
				return;
			}
			is.close();
			os.close();
			socket.close();
			Logger.log("Player with Connection UUID " + connectionUUID + " and I.P address of " + socket.getInetAddress() + " has disconnected!");
		} catch(Exception e) {}
	}
	
	public void sendJSON(JSONObject jsonObject) {
		try {
			if(os==null) {
				return;
			}
			ObjectOutputStream o = new ObjectOutputStream(os);
	        o.writeObject(jsonObject.toString());
	        os.flush();
		} catch(Exception e) {}
	}
	
	public JSONObject receiveJSON() {
		try {
			if(is==null) {
				return null;
			}
			ObjectInputStream i = new ObjectInputStream(is);
			String line = null;
			line = (String) i.readObject();
			JSONObject jsonObject = new JSONObject(line);
			return jsonObject;
		} catch (Exception e) {
			return null;
		}
	}
	
}
