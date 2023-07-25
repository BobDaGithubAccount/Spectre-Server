package api;

import api.events.Listener;
import command.CommandResponse;
import command.ICommand;
import logging.Logger;
import main.Main;

import java.io.File;


//name: testPlugin
//main_class: testFolder.Main
//author: Jephacake
//version: 1.0
//spectre_version: 1.0
//description: yes

public abstract class JavaPlugin {

    public final String name;
    public final String main_class;
    public final String author;
    public final float version;
    public final float spectre_version;
    public final String description;

    public JavaPlugin(String name, String main_class, String author, float version, float spectre_version, String description) {
        this.name = name;
        this.main_class = main_class;
        this.author = author;
        this.version = version;
        this.spectre_version = spectre_version;
        this.description = description;
    }

    protected File getPluginFolder() {
        File folder = new File(Main.jarFile.getParent() + "/plugins/" + this.name);
        folder.mkdirs();
        return folder;
    }

    private void logError(Exception e) {
        Logger.log("There was an error loading plugin " + name + " with the following exception:");
        Logger.log(e.getMessage());
    }

    protected void log(String message) {
        Logger.log(message);
    }

    protected boolean addEventListener(Listener listener) {
        return false;
    }

    protected boolean removeEventListener(Listener listener) {
        return false;
    }

    protected boolean addCommand(ICommand command) {
        return false;
    }

    protected boolean removeCommand(ICommand command) {
        return false;
    }

    protected CommandResponse executeCommand(String toRun) {
        return new CommandResponse("test", false);
    }

    public abstract void start();
    public abstract void stop();

}
