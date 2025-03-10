package api.command;

import gamelogic.event.EventManager;

public class StopCommand implements ICommand {

	@Override
	public String getName() {
		return "stop";
	}

	@Override
	public String getDescription() {
		return "Halts the server";
	}

	@Override
	public CommandResponse run(String[] args) {
		EventManager.stopServer(true);
		return new CommandResponse(true);
	}

}
