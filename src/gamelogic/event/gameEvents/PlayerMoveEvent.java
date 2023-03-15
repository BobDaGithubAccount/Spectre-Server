package gamelogic.event.gameEvents;

import gamelogic.event.IEvent;
import gamelogic.Spectre;
import gamelogic.entity.Player;
import lib.json.JSONObject;
import networking.NetworkWorkerThread;
import networking.Packet;

import java.util.UUID;

public class PlayerMoveEvent implements IEvent {
    @Override
    public String name() {
        return Packet.CMovePacket;
    }

    @Override
    public boolean run(JSONObject json, NetworkWorkerThread nwt) {
        float x = json.getFloat("x");
        float y = json.getFloat("y");
        float z = json.getFloat("z");
        float pitch = json.getFloat("pitch");
        float yaw = json.getFloat("yaw");
        float roll = json.getFloat("roll");
        UUID connectionUUID = nwt.connectionUUID;
        Player player = Spectre.players.get(connectionUUID);
        player.x = x;
        player.y = y;
        player.z = z;
        player.pitch = pitch;
        player.yaw = yaw;
        player.roll = roll;
        Spectre.players.put(connectionUUID, player);
        nwt.broadcastJSON(Packet.SMovePacket(x,y,z,pitch,yaw,roll,player.name,connectionUUID));
        return false;
    }
}
