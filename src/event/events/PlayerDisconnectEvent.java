package event.events;

import event.IEvent;
import gamelogic.Spectre;
import gamelogic.entity.Player;
import lib.json.JSONObject;
import logging.Logger;
import networking.NetworkWorkerThread;
import networking.Packet;

import java.util.Map;
import java.util.UUID;

public class PlayerDisconnectEvent implements IEvent {
    @Override
    public String name() {
        return Packet.CDisconnectPacket;
    }

    @Override
    public boolean run(JSONObject json, NetworkWorkerThread nwt) {

        if(nwt.isDisconnected) {
            return false;
        }
        nwt.isDisconnected = true;

        Player player = Spectre.players.get(nwt.connectionUUID);
        Spectre.players.remove(nwt.connectionUUID);
        for(Map.Entry<UUID, Player> p : Spectre.players.entrySet()) {
            if(!p.getKey().equals(nwt.connectionUUID) && !p.getValue().name.equals(player.name)) {
                nwt.sendJSON(Packet.SDisconnectPacket(player.name, nwt.connectionUUID));
            }
        }
        nwt.shutdown();

        Logger.log(player.name + " left the server with UUID " + nwt.connectionUUID);

        return false;
    }
}
