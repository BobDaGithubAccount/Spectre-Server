package command;

import logging.Logger;
import main.Main;

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
		Main.scanner.close();
		Main.ndt.shutdown();
		Main.canRun = false;
		Logger.shutdown();
		System.exit(0);
		return new CommandResponse(true);
	}

}
