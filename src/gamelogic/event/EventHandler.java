package gamelogic.event;

import gamelogic.event.gameEvents.PlayerConnectEvent;
import gamelogic.event.gameEvents.PlayerDisconnectEvent;
import gamelogic.event.gameEvents.PlayerMoveEvent;
import lib.json.JSONObject;
import logging.Logger;
import networking.NetworkWorkerThread;
import networking.Packet;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.jar.JarFile;

public class EventHandler {
    public static HashMap<String, ArrayList<IEvent>> events = new HashMap<String, ArrayList<IEvent>>();

    public static void pollPacket(JSONObject json, NetworkWorkerThread nwt) {
        Logger.log("Received packet: " + json.toString());
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

    private static String name = "";
    private static String author = "";
    private static String description = "";
    private static String version = "";
    private static String spectre_version = "";
    private static String main = "";
    public static void initPlugins() throws Exception {
        Logger.log("Loading plugins...");
        File jarFile = new File(Logger.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        File pluginsFolder = new File(jarFile.getParent() + "/plugins");
        pluginsFolder.mkdirs();
        for(File f : Objects.requireNonNull(pluginsFolder.listFiles())) {
            Logger.log("Attempting to load the file " + f);
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
                JarFile pluginJar = new JarFile(f);
                if(pluginJar.getJarEntry("plugin.yml")==null) {
                    throw new Exception("There isn't a plugin.yml that can be read! It needs to be at /plugin.yml");
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(pluginJar.getInputStream(pluginJar.getJarEntry("plugin.yml"))));
                String yml = "";
                String line;
                while((line = br.readLine())!=null) {
                    yml = yml + line + " ";
                }
                yml = yml.substring(0, yml.length() - 1);
                br.close();
                Plugin p;
                name = "";
                author = "";
                description = "";
                version = "";
                spectre_version = "";
                main = "";
                String[] words = yml.split(" ");
                for(int a = 0; a < words.length - 1; a = a + 2) {
                    if(words[a].equals("name:")) {
                        name = words[a+1];
                        continue;
                    }
                    if(words[a].equals("author:")) {
                        author = words[a+1];
                        continue;
                    }
                    if(words[a].equals("description:")) {
                        description = words[a+1];
                        continue;
                    }
                    if(words[a].equals("version:")) {
                        version = words[a+1];
                        continue;
                    }
                    if(words[a].equals("spectre_version:")) {
                        spectre_version = words[a+1];
                        continue;
                    }
                    if(words[a].equals("main_class:")) {
                        main = words[a+1];
                    }
                }
                if(name.equals("") || author.equals("") || description.equals("") || version.equals("") || spectre_version.equals("") || main.equals("")) {
                    throw new Exception("Not all required tokens were within the plugin YAML.");
                }
                //TODO
                Logger.log(name + " SC-" + spectre_version + "|V-" + version + " loaded successfully!");
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
