package event.events;

import event.IEvent;
import gamelogic.Spectre;
import gamelogic.entity.Player;
import lib.json.JSONObject;
import networking.NetworkWorkerThread;
import networking.Packet;

public class PlayerConnectEvent implements IEvent {


    @Override
    public String name() {
        return Packet.CConnectPacket;
    }

    @Override
    public boolean run(JSONObject json, NetworkWorkerThread nwt) {

        if((int)json.get(Packet.protocol_version)!=Packet.protocolVersion) {
            nwt.shutdown("Protocol version incompatible between server and client - S: " + Packet.protocolVersion + " C:" + ((int)json.get(Packet.protocol_version)));
            return false;
        }

        Spectre.players.put(nwt.connectionUUID, new Player(0, 10, 0, 0, 0, 0, json.getString("player_name"), nwt.connectionUUID));

        System.out.println(Spectre.getPlayers());

        return true;
    }
}
