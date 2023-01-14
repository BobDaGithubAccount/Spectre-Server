package gamelogic;

import gamelogic.entity.Player;
import main.Main;

import java.util.*;

public class Spectre {

    public static HashMap<UUID, Player> players = new HashMap<UUID, Player>();

    public static ArrayList<Player> getPlayers() {
        ArrayList<Player> players1 = new ArrayList<>();
        for(Map.Entry<UUID, Player> e : players.entrySet()) {
            players1.add(e.getValue());
        }
        return players1;
    }

}
