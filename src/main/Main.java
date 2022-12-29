package main;

import java.util.ArrayList;
import java.util.Scanner;

import commands.HelpCommand;
import commands.ICommand;
import commands.StopCommand;
import config.Settings;
import logging.Logger;
import networking.NetworkDelegatorThread;

public class Main {

	public static NetworkDelegatorThread ndt = new NetworkDelegatorThread();
	
	public static ArrayList<ICommand> commands = new ArrayList<ICommand>();
	
	public static Scanner scanner = new Scanner(System.in);
	
	public static boolean canRun = true;
	
	public static void main(String[] args) {
		Logger.log(Settings.name + " " + Settings.version + " is starting");
		ndt.start();
		try {
			init();
			Logger.log("Do 'help' for a list of commands");
			
			while(canRun) {
				String line = scanner.nextLine();
				boolean commandFound = false;
				
				for(ICommand command : commands) {
					if(command.getName().equalsIgnoreCase(line)) {
						commandFound = true;
						command.run();
						break;
					}
				}
				
				if(!commandFound) {
					Logger.log("No command with that name found. Do 'help' for a list of commands!");
				}
			}
		} catch (Exception e) {
			if(canRun) {
				e.printStackTrace();
			}
		}
	}
	
	public static void init() {
		commands.add(new HelpCommand());
		commands.add(new StopCommand());
	}

}
