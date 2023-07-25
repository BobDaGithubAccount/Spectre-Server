package testFolder;

import api.JavaPlugin;
import api.command.ICommand;
import api.events.Event;
import api.events.EventHandler;
import api.events.Listener;
import api.networking.Packet;
import command.CommandResponse;

public class Main extends JavaPlugin implements Listener, ICommand {

    public Main() {
        super("testPlugin", "testFolder.Main", "Jephacake", 1.0f, 1.0f, "yes");
    }

    @Override
    public void start() {
        this.log("Hello from plugin start()!");
        this.addEventListener(this);
    }

    @Override
    public void stop() {
        this.log("So long and thanks for all the stop()!");
        this.removeEventListener(this);
    }

    @EventHandler
    public boolean onPlayerConnectEvent(Event event) {
        if(!event.json.getString(Packet.packet_type).equals(Packet.CConnectPacket)) {
            return false;
        }
        this.log(event.json.getString(Packet.packet_type));
        return false;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public CommandResponse run(String[] strings) {
        return new CommandResponse(false);
    }
}
