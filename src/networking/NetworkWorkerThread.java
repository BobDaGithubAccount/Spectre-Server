package networking;

import event.EventHandler;
import lib.json.JSONObject;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class NetworkWorkerThread extends Thread {

	public Socket socket;
	public UUID connectionUUID;
	public InputStream is;
	public OutputStream os;
	public Timer timer = new Timer();

	public NetworkWorkerThread(Socket socket, UUID connectionUUID) {
		this.socket = socket;
		this.connectionUUID = connectionUUID;
	}

	class TickTask extends TimerTask {
		@Override
		public void run() {
			sendJSON(Packet.SPingPacket());
			timer.schedule(new TickTask(), 1000);
		}
	}

	public long lastPingReceived = new Date().getTime();
	boolean improperDisconnect = false;
	class TimeoutTask extends TimerTask {
		@Override
		public void run() {
			if((new Date().getTime() - lastPingReceived) > 10000) {
				shutdown("Connection has timed out!");
				return;
			}
			timer.schedule(new TimeoutTask(), 1000);
		}
	}

	@Override
	public void run() {
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
			timer.schedule(new TickTask(), 1000);
			timer.schedule(new TimeoutTask(), 1000);
			while (socket.isBound() && socket.isConnected() && !socket.isClosed()) {
				JSONObject json = receiveJSON();
				if(json == null) continue;
				System.out.println(json);
				if(json.get(Packet.packet_type).equals(Packet.CPingPacket)) {
					lastPingReceived = new Date().getTime();
				}
				EventHandler.pollPacket(json, this);
			}
			if(!improperDisconnect) {
				improperDisconnect = true;
				EventHandler.pollPacket(Packet.CDisconnectPacket(), this);
			}
			is.close();
			os.close();
			socket.close();
		} catch(Exception e) {
			System.out.println("There was a fatal error on network worker thread " + connectionUUID.toString() + " due to the following exception:");
			e.printStackTrace();
			if(!improperDisconnect) {
				improperDisconnect = true;
				EventHandler.pollPacket(Packet.CDisconnectPacket(), this);
			}
			System.out.println("The player has been cleared from the game server but the thread may or may not shutdown correctly.");
		}
	}

	public void shutdown() {
		try {
			is.close();
			os.close();
			socket.close();
		} catch (Exception e) {e.printStackTrace();}
		if(!improperDisconnect) {
			improperDisconnect = true;
			EventHandler.pollPacket(Packet.CDisconnectPacket(), this);
		}
	}

	public void shutdown(String message) {
		System.out.println("ON THREAD SHUTDOWN MESSAGE: " + message);
		try {
			is.close();
			os.close();
			socket.close();
		} catch (Exception e) {e.printStackTrace();}
		if(!improperDisconnect) {
			improperDisconnect = true;
			EventHandler.pollPacket(Packet.CDisconnectPacket(), this);
		}
	}

	public void sendJSON(JSONObject json) {
		if(!(socket.isConnected() && socket.isBound() && !socket.isClosed())) {
			return;
		}
		try {
			ObjectOutputStream o = new ObjectOutputStream(os);
			o.writeUTF(json.toString());
			o.flush();
			os.flush();
		} catch(Exception ignored) {}
	}

	public JSONObject receiveJSON() {
		if(!(socket.isConnected() && socket.isBound() && !socket.isClosed())) {
			return null;
		}
		try {
			ObjectInputStream i = new ObjectInputStream(is);
			String line;
			line = i.readUTF();
			return new JSONObject(line);
		} catch (Exception ignored) {
			return null;
		}
	}

}