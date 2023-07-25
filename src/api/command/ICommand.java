package api.command;

import command.CommandResponse;

public interface ICommand {

	public String getName();
	public String getDescription();
	public CommandResponse run(String[] args);
	
}
