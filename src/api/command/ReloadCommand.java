package api.command;

import gamelogic.event.EventManager;

public class ReloadCommand implements ICommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads all plugins on the server";
    }

    @Override
    public CommandResponse run(String[] args) {
        boolean response = EventManager.reloadServer();
        return new CommandResponse(response);
    }
}
