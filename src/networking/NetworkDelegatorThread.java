package networking;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import config.Settings;
import lib.json.JSONObject;

public class NetworkDelegatorThread extends Thread {

	public boolean canRun = true;
	
	public HashMap<UUID, NetworkWorkerThread> connections = new HashMap<UUID, NetworkWorkerThread>();
	
	ServerSocket ss;
	
	@Override
	public void run() {
		try {
			ss = new ServerSocket(Settings.portNumber);
			
			while(ss.isBound() && !ss.isClosed() && canRun) {
				Socket s = ss.accept();
				UUID uuid = UUID.randomUUID();
				NetworkWorkerThread nwt = new NetworkWorkerThread(s, uuid);
				connections.put(uuid, nwt);
				nwt.start();
			}
			
			ss.close();
		} catch(Exception e) {
			if(canRun) {
				e.printStackTrace();
			}
			System.exit(-1);
		}
	}

	public void shutdown() {
		try {
			canRun = false;
			ss.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
