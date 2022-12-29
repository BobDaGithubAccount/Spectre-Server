package event.events;

import event.IEvent;
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
        float x = (float) json.get("x");
        float y = (float) json.get("y");
        float z = (float) json.get("z");
        float pitch = (float) json.get("pitch");
        float yaw = (float) json.get("yaw");
        float roll = (float) json.get("roll");
        UUID connectionUUID = nwt.connectionUUID;
        Player player = Spectre.getPlayer(connectionUUID);
        player.x = x;
        player.y = y;
        player.z = z;
        player.pitch = pitch;
        player.yaw = yaw;
        player.roll = roll;
        return true;
    }
}
