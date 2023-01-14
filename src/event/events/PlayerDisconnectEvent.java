package event.events;

import event.IEvent;
import gamelogic.Spectre;
import lib.json.JSONObject;
import networking.NetworkWorkerThread;
import networking.Packet;

public class PlayerDisconnectEvent implements IEvent {
    @Override
    public String name() {
        return Packet.CDisconnectPacket;
    }

    @Override
    public boolean run(JSONObject json, NetworkWorkerThread nwt) {

        Spectre.players.remove(nwt.connectionUUID);
        nwt.shutdown();

        System.out.println(Spectre.getPlayers());

        return true;
    }
}
