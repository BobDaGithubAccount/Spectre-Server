package networking;

import config.Settings;
import lib.json.JSONObject;

import java.util.Date;
import java.util.UUID;

public class Packet {

	public static final int protocolVersion = Settings.protocolVersion;
	public static final String packet_type = "packet_type";
	public static final String protocol_version = "protocol_version";

	//
	//	SERVER
	//

	public static String SPingPacket = "S-PING";
	public static JSONObject SPingPacket() {
		JSONObject packet = new JSONObject();
		packet.put(packet_type, SPingPacket);
		packet.put(protocol_version, protocolVersion);
		packet.put("time", new Date().getTime());
		return packet;
	}

	public static String SStatusPacket = "S-STATUS";
	public static JSONObject SStatusPacket() {
		JSONObject packet = new JSONObject();
		packet.put(packet_type, SStatusPacket);
		packet.put(protocol_version, Settings.protocolVersion);
		packet.put("server_name", Settings.serverName);
		packet.put("server_description", Settings.serverDescription);
		return packet;
	}

	public static String SMovePacket = "S-MOVE";
	public static JSONObject SMovePacket(float x, float y, float z, float pitch, float yaw, float roll, String name, UUID connectionUUID) {
		JSONObject packet = new JSONObject();
		packet.put(packet_type, SMovePacket);
		packet.put(protocol_version, protocolVersion);
		packet.put("x", x);
		packet.put("y", y);
		packet.put("z", z);
		packet.put("pitch", pitch);
		packet.put("yaw", yaw);
		packet.put("roll", roll);
		packet.put("name", name);
		packet.put("connectionUUID", connectionUUID.toString());
		return packet;
	}



	//
	//	CLIENT
	//
	public static String CPingPacket = "C-PING";
	public static JSONObject CPingPacket() {
		JSONObject packet = new JSONObject();
		packet.put(packet_type, CPingPacket);
		packet.put(protocol_version, protocolVersion);
		packet.put("time", new Date().getTime());
		return packet;
	}

	public static String CConnectPacket = "C-CONNECT";
	public static JSONObject CConnectPacket() {
		JSONObject packet = new JSONObject();
		packet.put(packet_type, CConnectPacket);
		packet.put(protocol_version, protocolVersion);
		packet.put("player_name", UUID.randomUUID().toString());
		return packet;
	}
	public static String CDisconnectPacket = "C-DISCONNECT";
	public static JSONObject CDisconnectPacket() {
		JSONObject packet = new JSONObject();
		packet.put(packet_type, CDisconnectPacket);
		packet.put(protocol_version, protocolVersion);
		return packet;
	}

	public static String CMovePacket = "C-MOVE";
	public static JSONObject CMovePacket(float x, float y, float z, float pitch, float yaw, float roll) {
		JSONObject packet = new JSONObject();
		packet.put(packet_type, CMovePacket);
		packet.put(protocol_version, protocolVersion);
		packet.put("x", x);
		packet.put("y", y);
		packet.put("z", z);
		packet.put("pitch", pitch);
		packet.put("yaw", yaw);
		packet.put("roll", roll);
		return packet;
	}

}
