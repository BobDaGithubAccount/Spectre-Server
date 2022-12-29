package event;

import event.events.PlayerConnectEvent;
import event.events.PlayerDisconnectEvent;
import event.events.PlayerMoveEvent;
import lib.json.JSONObject;
import logging.Logger;
import networking.NetworkWorkerThread;
import networking.Packet;

import java.util.ArrayList;
import java.util.HashMap;

public class EventHandler {
    public static HashMap<String, ArrayList<IEvent>> events = new HashMap<String, ArrayList<IEvent>>();

    public static void pollPacket(JSONObject json, NetworkWorkerThread nwt) {
        ArrayList<IEvent> eventsToPoll = events.get(json.get(Packet.packet_type));
        System.out.println(eventsToPoll);
        if(eventsToPoll==null) {
            return;
        }
        for(IEvent eventHook : eventsToPoll) {
            boolean shouldContinue = eventHook.run(json, nwt);
            if(!shouldContinue) {break;}
        }
    }

    public static void addEventListener(String packetType, IEvent event) {
        ArrayList<IEvent> eventsToPoll = events.get(packetType);
        if(eventsToPoll==null) {
            events.put(packetType, new ArrayList<IEvent>());
            eventsToPoll = new ArrayList<IEvent>();
        }
        eventsToPoll.add(event);
        events.put(packetType, eventsToPoll);
    }



    public static void init() {
        addEventListener(Packet.CConnectPacket, new PlayerConnectEvent());
        addEventListener(Packet.CDisconnectPacket, new PlayerDisconnectEvent());
        addEventListener(Packet.CMovePacket, new PlayerMoveEvent());
    }

}
