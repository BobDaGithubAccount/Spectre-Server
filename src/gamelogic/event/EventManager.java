package gamelogic.event;

import api.JavaPlugin;
import api.events.Event;
import gamelogic.event.gameEvents.PlayerConnectEvent;
import gamelogic.event.gameEvents.PlayerDisconnectEvent;
import gamelogic.event.gameEvents.PlayerMoveEvent;
import lib.json.JSONObject;
import logging.Logger;
import main.Main;
import networking.NetworkWorkerThread;
import networking.Packet;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class EventManager {
    public static HashMap<String, ArrayList<IEvent>> events = new HashMap<String, ArrayList<IEvent>>();

    public static void pollPacket(JSONObject json, NetworkWorkerThread nwt) {
        try {
            if(pollPacketPlugin(json, nwt)) {
                return;
            }
            String packet_type = json.getString(Packet.packet_type);
            if(!events.containsKey(packet_type)) {
                return;
            }
            ArrayList<IEvent> eventsToPoll = events.get(packet_type);
            if (eventsToPoll == null) {
                return;
            }
            boolean shouldCancel = false;
            for (IEvent eventHook : eventsToPoll) {
                shouldCancel = eventHook.run(json, nwt);
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
            pollStart();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log("There was an error loading plugins!");
        }
    }

    public static void pollStart() {
        for(JavaPlugin plugin : plugins) {
            plugin.start();
        }
    }

    public static void stopServer() {
        for(JavaPlugin plugin : plugins) {
            plugin.stop();
        }
        Main.scanner.close();
        Main.ndt.shutdown();
        Main.canRun = false;
        Logger.shutdown();
        System.exit(0);
    }

    public static ArrayList<Method> pluginEvents = new ArrayList<>();

    public static boolean pollPacketPlugin(JSONObject json, NetworkWorkerThread nwt) {
        try {
            boolean shouldCancel = false;
            Event event = new Event(nwt, json);
            for (Method method : pluginEvents) {
                System.out.println(method);
                shouldCancel = (boolean) method.invoke(event);
                if (shouldCancel) {
                    break;
                }
            }
            return shouldCancel;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
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
                JarFile file = new JarFile(f);
                JarEntry pluginYAML = file.getJarEntry("plugin.yml");
                if(pluginYAML == null) {
                    throw new Exception("No 'plugin.yml' was found at /plugin.yml");
                }
                Scanner pluginYmlScanner = new Scanner(file.getInputStream(pluginYAML));
                ArrayList<String> lines = new ArrayList<>();
                while (pluginYmlScanner.hasNextLine()) {
                    lines.add(pluginYmlScanner.nextLine());
                }
                pluginYmlScanner.close();
                String mainClass = null;
                for(String s : lines) {
                    if(s.split(" ")[0].equalsIgnoreCase("main_class:")) {
                        mainClass = s.split(" ")[1];
                        break;
                    }
                }
                if(mainClass == null) {
                    throw new Exception("There was an error loading the main_class info from the plugin.yml. Example: 'main_class: testFolder.Main'");
                }
                URL jarURL = f.toURI().toURL();
                URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
                Class<?> clazz = Class.forName(mainClass, true, classLoader);
                JavaPlugin plugin = (JavaPlugin) clazz.getDeclaredConstructor().newInstance();
                plugins.add(plugin);
            }
            catch(Exception e) {
                e.printStackTrace();
                Logger.log("There was an error while trying to load the file " + f.getPath() + " as a plugin");
                Logger.log(e.getMessage());
            }
        }
        Logger.log("Loaded plugins!");
    }

    //TODO Plugin support
}
