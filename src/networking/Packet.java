package networking;

import java.util.Date;

import config.Settings;
import lib.json.JSONObject;

public class Packet {
	
	public static JSONObject SPingPacket() {
		JSONObject packet = new JSONObject();
		packet.put("packet_type", "S-PING");
		packet.put("time", new Date().getTime());
		return packet;
	}
	
	public static JSONObject SStatusPacket() {
		JSONObject packet = new JSONObject();
		packet.put("packet_type", "S-STATUS");
		packet.put("protocol_version", Settings.protocolVersion);
		packet.put("server_name", Settings.serverName);
		packet.put("server_description", Settings.serverDescription);
		return packet;
	}
	
}
