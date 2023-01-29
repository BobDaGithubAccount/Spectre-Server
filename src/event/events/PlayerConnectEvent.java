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
        nwt.sendJSON(Packet.SInitPacket());
        Player player = new Player(0, 10, 0, 0, 0, 0, json.getString("name"), nwt.connectionUUID);
        Spectre.players.put(nwt.connectionUUID, player);
        for(Map.Entry<UUID, Player> p : Spectre.players.entrySet()) {
            if(!p.getKey().equals(nwt.connectionUUID) && !p.getValue().name.equals(player.name)) {
                nwt.sendJSON(Packet.SConnectPacket(json.getString("name"), nwt.connectionUUID));
            }
        }

        Logger.log(player.name + " joined the server with UUID " + nwt.connectionUUID);

        return false;
    }
}
