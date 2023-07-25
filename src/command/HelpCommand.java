package command;

import logging.Logger;
import main.Main;

public class HelpCommand implements ICommand {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Returns a list of commands supported by the server";
	}

	@Override
	public CommandResponse run(String[] args) {
		for(ICommand command : Main.commands) {
			Logger.log(command.getName() + " | " + command.getDescription());
		}
		return new CommandResponse( true);
	}

}
