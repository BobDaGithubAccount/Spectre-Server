package command;

public interface ICommand {

	public String getName();
	public String getDescription();
	public CommandResponse run(String[] args);
	
}
