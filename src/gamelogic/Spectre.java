package gamelogic;

import gamelogic.entity.Entity;
import gamelogic.entity.Player;
import lib.vector.Vector3f;
import logging.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Spectre {

    public static HashMap<UUID, Player> players = new HashMap<UUID, Player>();
    public static HashMap<String, HashMap<String, Entity>> scene = new HashMap<String, HashMap<String, Entity>>();

    //TEST
    public static void initScene() {
        scene = new HashMap<String, HashMap<String, Entity>>();
        initParent("dragon1");
        pushEntity(new Entity("dragon", "dragon1", new Vector3f(0,0,0), new Vector3f(0,0,0), 1));
    }

    public static void loadLevel(String name) {
        try {
            Logger.log("Loading level " + name);
            File folder = new File(new File(Spectre.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent()+"/levels/");
            folder.mkdirs();
            System.out.println(folder);
            File file = new File(folder.getPath() + "/" + name + ".slf");
            System.out.println(file);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            scene = (HashMap<String, HashMap<String, Entity>>) ois.readObject();
            ois.close();
            System.out.println(scene);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void dumpLevel(String name) {
        try {
            Logger.log("Saving level " + name);
            File folder = new File(new File(Spectre.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent()+"/levels/");
            folder.mkdirs();
            System.out.println(folder);
            File file = new File(folder.getPath() + "/" + name + ".slf");
            file.delete();
            file.createNewFile();
            System.out.println(file);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(scene);
            oos.close();
            System.out.println(scene);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void pushEntity(Entity instance) {
        HashMap<String, Entity> instances = scene.get(instance.getParent());
        instances.put(instance.getName(), instance);
        scene.put(instance.getParent(), instances);
    }

    public static void popEntity(String parent, String name) {
        HashMap<String, Entity> instances = scene.get(parent);
        instances.remove(name);
        scene.put(parent, instances);
    }

    public static void initParent(String parent) {
        scene.put(parent, new HashMap<String, Entity>());
    }

    public static void removeParent(String parent) {
        scene.remove(parent);
    }

    public static ArrayList<Player> getPlayers() {
        ArrayList<Player> players1 = new ArrayList<>();
        for(Map.Entry<UUID, Player> e : players.entrySet()) {
            players1.add(e.getValue());
        }
        return players1;
    }

}
