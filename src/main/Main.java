package main;

import commands.*;
import config.Settings;
import gamelogic.event.EventHandler;
import logging.Logger;
import networking.NetworkDelegatorThread;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static NetworkDelegatorThread ndt = new NetworkDelegatorThread();
	
	public static ArrayList<ICommand> commands = new ArrayList<ICommand>();
	
	public static Scanner scanner = new Scanner(System.in);
	
	public static boolean canRun = true;
	
	public static void main(String[] args) {
		Logger.init();
		Logger.log(Settings.name + " " + Settings.version + " is starting");
//		Spectre.initScene();
		ndt.start();
		try {
			init();
			Logger.log("Do 'help' for a list of commands");
			
			while(canRun) {
				String line = scanner.nextLine();
				String[] words = line.split(" ");
				if(words.length == 0) {continue;}
				String label = words[0];
				words[0] = "";
				String args1 = "";
				for(String s : words) {
					args1 = args1 + s;
				}
				String[] arguments = args1.split(" ");
				boolean commandFound = false;
				
				for(ICommand command : commands) {
					if(command.getName().equalsIgnoreCase(label)) {
						commandFound = true;
						command.run(arguments);
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
		Logger.shutdown();
	}
	
	public static void init() {
		commands.add(new HelpCommand());
		commands.add(new StopCommand());
		commands.add(new PlayerListCommand());
		commands.add(new SaveLevelCommand());
		commands.add(new LoadLevelCommand());
		commands.add(new InitSceneCommand());
		EventHandler.init();
	}

}
