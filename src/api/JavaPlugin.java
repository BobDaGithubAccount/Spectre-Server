package api;

import api.events.Event;
import api.events.EventHandler;
import api.events.Listener;
import api.command.CommandResponse;
import api.command.ICommand;
import gamelogic.event.EventManager;
import logging.Logger;
import main.Main;

import java.io.File;
import java.lang.reflect.Method;

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
        Logger.log("[" + this.name + "] " + message);
    }

    protected void addEventListener(Listener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHandler.class) && method.getReturnType().equals(boolean.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1 || parameterTypes[0] != Event.class) {
                    throw new IllegalArgumentException(
                            "Method " + method.getName() + " is annotated with @EventHandler but doesn't have correct arguments"
                    );
                }
            }
        }
        EventManager.pluginEvents.add(listener);
    }

    protected void removeEventListener(Listener listener) {
        EventManager.pluginEvents.remove(listener);
    }

    protected void addCommand(ICommand command) {
        Main.addCommand(command);
    }

    protected void removeCommand(ICommand command) {
        Main.removeCommand(command);
    }

    protected CommandResponse executeCommand(String toRun) {
        Main.runCommand(toRun);
        return new CommandResponse( false);
    }

    public abstract void start();
    public abstract void stop();

}
