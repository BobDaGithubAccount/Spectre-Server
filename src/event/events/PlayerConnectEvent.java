package event.events;

import event.IEvent;
import gamelogic.Spectre;
import gamelogic.entity.Player;
import lib.json.JSONObject;
import logging.Logger;
import main.Main;
import networking.NetworkWorkerThread;
import networking.Packet;

import java.util.Map;
import java.util.UUID;

public class PlayerConnectEvent implements IEvent {


    @Override
    public String name() {
        return Packet.CConnectPacket;
    }

    @Override
    public boolean run(JSONObject json, NetworkWorkerThread nwt) {

        if(json.getInt(Packet.protocol_version)!=Packet.protocolVersion) {
            nwt.shutdown("Protocol version incompatible between server and client - S: " + Packet.protocolVersion + " C:" + (json.getInt(Packet.protocol_version)));
            return true;
        }
        for(Player p : Spectre.getPlayers()) {
            nwt.sendJSON(Packet.SConnectPacket(p.name, p.connectionUUID));
        }
        Player player = new Player(0, 10, 0, 0, 0, 0, json.getString("name"), nwt.connectionUUID);
        Spectre.players.put(nwt.connectionUUID, player);
        nwt.broadcastJSON(Packet.SConnectPacket(player.name, player.connectionUUID));
        Logger.log(player.name + " joined the server with UUID " + nwt.connectionUUID);

        return false;
    }
}
