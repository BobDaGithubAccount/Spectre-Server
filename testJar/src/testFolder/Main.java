package testFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;

import api.JavaPlugin;
import api.command.ICommand;
import api.events.Event;
import api.events.EventHandler;
import api.events.Listener;
import api.networking.Packet;
import api.command.CommandResponse;

public class Main extends JavaPlugin implements Listener, ICommand {

    public Main() {
        super("testPlugin", "testFolder.Main", "Jephacake", 1.0f, 1.0f, "yes");
    }

    File configFile;
    
    private String hello;
    private final String helloIdentifier = "helloMessage:";
    private String bye;
    private final String byeIdentifier = "byeMessage:";
    
    private final String defaultConfiguration = 
    helloIdentifier + " Hello from plugin start()!" + System.lineSeparator()
    + byeIdentifier + " So long and thanks for all the stop()!" + System.lineSeparator();
    
    @Override
    public void start() {
    	try {
	    	File folder = this.getPluginFolder();
	    	configFile = new File(folder.getPath() + "/settings.txt");
	    	if(!configFile.exists()) {
	    		configFile.createNewFile();
	    		BufferedWriter bw = new BufferedWriter(new FileWriter(configFile));
	    		bw.write(defaultConfiguration);
	    		bw.close();
	    	}
	    	List<String> lines = Files.readAllLines(configFile.toPath());
	    	for(String line : lines) {
	    		String[] words = line.split(" ");
	    		String message = "";
	    		for(int i = 1; i < words.length; i++) {
	    			message += words[i] + " ";
	    		}
	            message = message.substring(0, message.length() - 1);
	    		switch(words[0]) {
	    			case helloIdentifier:
	    				this.hello = message;
	    			case byeIdentifier:
	    				this.bye = message;
	    		}
	    	}
	        this.log(hello);
	        this.addEventListener(this);
	        this.addCommand(this);
//	        this.executeCommand("stop");
    	} catch(Exception e) {
    		this.log("There was an error loading the plugin, just as a heads up!");
    		this.log(e.getMessage());
    		e.printStackTrace();
    	}
    }

    @Override
    public void stop() {
        this.log(bye);
        this.removeEventListener(this);
        this.removeCommand(this);
    }

    @EventHandler
    public boolean onPlayerConnectEvent(Event event) {
        if(event.json.getString(Packet.packet_type).equals(Packet.CConnectPacket) || event.json.getString(Packet.packet_type).equals(Packet.CDisconnectPacket)) {
        	this.log(event.json.toString(1));
            return false;
        }
        return false;
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "getDescription";
    }

    @Override
    public CommandResponse run(String[] strings) {
    	this.log("test");
        return new CommandResponse(false);
    }
}
