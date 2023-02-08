package commands;

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
	public void run(String[] args) {
		Main.scanner.close();
		Main.ndt.shutdown();
		Main.canRun = false;
		System.exit(0);
	}

}
