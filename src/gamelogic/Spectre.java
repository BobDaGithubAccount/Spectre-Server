package gamelogic;

import gamelogic.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Spectre {

    private static ArrayList<Player> players = new ArrayList<Player>();

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static void removePlayer(Player player) {
        players.remove(player);
    }

    public static Player getPlayer(String name) {
        Player player = null;
        for(Player p : players) {
            if(p.name==name) {
                player = p;
                break;
            }
        }
        return player;
    }

    public static Player getPlayer(UUID connectionUUID) {
        Player player = null;
        for(Player p : players) {
            if(p.connectionUUID==connectionUUID) {
                player = p;
                break;
            }
        }
        return player;
    }

}
