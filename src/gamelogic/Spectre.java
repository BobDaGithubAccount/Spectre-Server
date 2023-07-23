package gamelogic;

import gamelogic.entity.Entity;
import gamelogic.entity.Player;
import lib.utils.FileUtils;
import logging.Logger;
import networking.NetworkDelegatorThread;
import res.ResourceClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Spectre {

    public static HashMap<UUID, Player> players = new HashMap<UUID, Player>();
    public static HashMap<String, HashMap<String, Entity>> scene = new HashMap<String, HashMap<String, Entity>>();

    public static String level;

    public static void loadLevel(String name) {
        try {
            File jarFile = new File(NetworkDelegatorThread.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File folder = jarFile.getParentFile();
            File savesFolder = new File(folder.getPath() + "/saves");
            if(!savesFolder.exists()) {
                sterilizeFolder();
                Logger.log("Saves folder didn't exist & default configuration used! Designated folder: " + savesFolder.getPath());
            }
            File levelFile = new File(savesFolder.getPath() + "/" + name + ".level");
            if(!levelFile.exists()) {
                sterilizeFolder();
                throw new Exception("Specified level file '" + name + "' doesn't exist!");
            }
            level = new String(FileUtils.readBytesFromFile(levelFile.getPath()), StandardCharsets.UTF_8);
        }
        catch(Exception e) {
            Logger.log("There was an exception loading the server level: ");
            Logger.log(e.getMessage());
            Logger.log("Shutting down!");
            System.exit(-1);
        }
    }

    private static void sterilizeFolder() {
        try {
            File jarFile = new File(NetworkDelegatorThread.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File folder = jarFile.getParentFile();
            File savesFolder = new File(folder.getPath() + "/saves");
            String fileOutsideJar = savesFolder.getPath() + "/level.level";
            String fileInsideJar = "level.level";
            savesFolder.mkdirs();
            InputStream inputStream = ResourceClass.class.getResourceAsStream(fileInsideJar);
            File outputFile = new File(fileOutsideJar);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                outputStream.flush();
            }
            inputStream.close();
            outputStream.close();
        } catch(Exception ignored){}
    }

    public static ArrayList<Player> getPlayers() {
        ArrayList<Player> players1 = new ArrayList<>();
        for(Map.Entry<UUID, Player> e : players.entrySet()) {
            players1.add(e.getValue());
        }
        return players1;
    }

}