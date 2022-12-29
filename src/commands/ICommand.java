package commands;

public interface ICommand {

	public String getName();
	public String getDescription();
	public void run();
	
}
