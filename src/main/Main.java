package main;

import command.*;
import config.Settings;
import gamelogic.Spectre;
import gamelogic.event.EventManager;
import logging.Logger;
import networking.HTTPServer;
import networking.NetworkDelegatorThread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static NetworkDelegatorThread ndt = new NetworkDelegatorThread();
	public static ArrayList<ICommand> commands = new ArrayList<ICommand>();
	public static Scanner scanner = new Scanner(System.in);
	
	public static boolean canRun = true;
	public static File jarFile;

	public static void main(String[] args) {
		System.out.println("IGNORE THE WARNING AND DO NOT REPORT THE 'ISSUE' TO ME");
		System.out.println("THE PROJECT IS STAYING ON JAVA-17 INDEFINITELY AND THEREFORE THE SECURITY WARNING FOR THE SECURITY MANAGER \n(WHICH MANAGES SANDBOXING) PACKAGE BEING REMOVED IS NOTHING TO BE WORRIED ABOUT.");
		jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getFile());
		Logger.init();
		loadWhitelist();
		instantiateSecurityManager();
		Logger.log(Settings.name + " " + Settings.version + " is starting");
		Spectre.loadLevel(Settings.levelName);
		HTTPServer.init();
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
				StringBuilder args1 = new StringBuilder();
				for(String s : words) {
					args1.append(s);
				}
				String[] arguments = args1.toString().split(" ");
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
		shutdown();
	}

	public static void shutdown() {
		Logger.shutdown();
		scanner.close();
		HTTPServer.shutdown();
	}
	
	public static void init() {
		commands.add(new HelpCommand());
		commands.add(new StopCommand());
		commands.add(new PlayerListCommand());
		commands.add(new LoadLevelCommand());
		EventManager.init();
	}

	private static ArrayList<String> whitelistedDirectories = new ArrayList<String>();
	private static void loadWhitelist() {
		ArrayList<String> backup_whitelistedDirectories = new ArrayList<String>();
		backup_whitelistedDirectories.add(System.getProperty("java.home"));
		backup_whitelistedDirectories.add(jarFile.getParent());
		try {
			File whitelist = new File(jarFile.getParent() + "/whitelist.txt");
			if (!whitelist.exists()) {
				whitelist.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(whitelist));
				for(String s : backup_whitelistedDirectories) {
					bw.write(s);
					bw.newLine();
					bw.flush();
				}
				bw.close();
				whitelistedDirectories = backup_whitelistedDirectories;
				File fatalError = new File(jarFile.getParent() + "/FATAL_ERROR.TXT");
				fatalError.delete();
				fatalError.createNewFile();
				BufferedWriter bw2 = new BufferedWriter(new FileWriter(fatalError));
				for(int x = 0; x < 10; x++) {
					bw2.write("ENSURE YOUR JAVA DIRECTORY IS IN WHITELIST.TXT" + System.lineSeparator());
					bw2.flush();
				}
				bw2.close();
				return;
			}
			List<String> lines = Files.readAllLines(whitelist.toPath());
			for(String s : lines) {
				whitelistedDirectories.add(s);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		};
	}

	private static void instantiateSecurityManager() {
		File workingDirectory = new File(jarFile.getParent());
		SecurityManager customSecurityManager = new SecurityManager() {
			public boolean hasPermission(String file) {
				for(String s : whitelistedDirectories) {
					if(file.startsWith(s + File.separator)) {
						return true;
					}
				}
				return false;
			}
			@Override
			public void checkRead(String file) {
				if (!hasPermission(file)) {
					String toSay = "Access to " + file + " for read is not allowed!";
					System.out.println(toSay);
					throw new SecurityException(toSay);
				}
			}
			@Override
			public void checkWrite(String file) {
				if (!hasPermission(file)) {
					String toSay = "Access to " + file + " for write is not allowed!";
					System.out.println(toSay);
					throw new SecurityException(toSay);
				}
			}
			@Override
			public void checkDelete(String file) {
				if (!hasPermission(file)) {
					String toSay = "Access to " + file + " for delete is not allowed!";
					System.out.println(toSay);
					throw new SecurityException(toSay);
				}
			}
			@Override
			public void checkConnect(String host, int port) {
				// Allow all connections
			}

			@Override
			public void checkPermission(Permission perm) {
				// Allow other permissions
			}
			@Override
			public void checkPermission(Permission perm, Object context) {

			}
		};
		System.setSecurityManager(customSecurityManager);
	}

}
