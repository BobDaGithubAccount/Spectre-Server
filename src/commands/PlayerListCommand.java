package commands;

import gamelogic.Spectre;
import logging.Logger;

public class PlayerListCommand implements ICommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Lists all players connected to the server and their primitive data, like position or rotation";
    }

    @Override
    public void run() {
        Logger.log("Players:");
        System.out.println(Spectre.getPlayers());
    }
}
