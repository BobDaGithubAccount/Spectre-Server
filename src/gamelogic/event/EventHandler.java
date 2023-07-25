package gamelogic.event;

import api.JavaPlugin;
import gamelogic.event.gameEvents.PlayerConnectEvent;
import gamelogic.event.gameEvents.PlayerDisconnectEvent;
import gamelogic.event.gameEvents.PlayerMoveEvent;
import lib.json.JSONObject;
import logging.Logger;
import main.Main;
import networking.NetworkWorkerThread;
import networking.Packet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class EventHandler {
    public static HashMap<String, ArrayList<IEvent>> events = new HashMap<String, ArrayList<IEvent>>();

    public static void pollPacket(JSONObject json, NetworkWorkerThread nwt) {
        try {
            String packet_type = json.getString(Packet.packet_type);
            if(!events.containsKey(packet_type)) {
                return;
            }
            ArrayList<IEvent> eventsToPoll = events.get(packet_type);
            if (eventsToPoll == null) {
                return;
            }
            for (IEvent eventHook : eventsToPoll) {
                boolean shouldCancel = eventHook.run(json, nwt);
                if (shouldCancel) {
                    break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void addEventListener(String packetType, IEvent event) {
        registeredEvents.add(event);
        ArrayList<IEvent> eventsToPoll = events.get(packetType);
        if(eventsToPoll==null) {
            events.put(packetType, new ArrayList<IEvent>());
            eventsToPoll = new ArrayList<IEvent>();
        }
        eventsToPoll.add(event);
        events.put(packetType, eventsToPoll);
    }

    public static ArrayList<IEvent> registeredEvents = new ArrayList<IEvent>();

    public static void init() {
        addEventListener(Packet.CConnectPacket, new PlayerConnectEvent());
        addEventListener(Packet.CDisconnectPacket, new PlayerDisconnectEvent());
        addEventListener(Packet.CMovePacket, new PlayerMoveEvent());

        try {
            initPlugins();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log("There was an error loading plugins!");
        }
    }

    public static ArrayList<JavaPlugin> plugins = new ArrayList<JavaPlugin>();
    public static void initPlugins() throws Exception {
        Logger.log("Loading plugins...");
        File jarFile = Main.jarFile;
        File pluginsFolder = new File(jarFile.getParent() + "/plugins");
        pluginsFolder.mkdirs();
        for(File f : Objects.requireNonNull(pluginsFolder.listFiles())) {
            try {
                if (f.isDirectory()) {
                    continue;
                }
                String extension = "";
                int i = f.getName().lastIndexOf('.');
                if (i > 0) {
                    extension = f.getName().substring(i + 1);
                }
                if (!extension.equals("jar")) {
                    continue;
                }
                Logger.log("Attempting to load the file " + f);

            }
            catch(Exception e) {
                e.printStackTrace();
                Logger.log("There was an error while trying to load the plugin with file " + f);
                Logger.log(e.getMessage());
            }
        }
        Logger.log("Loaded plugins!");
    }

    //TODO Plugin support
}
