package commands;

import gamelogic.Spectre;
import logging.Logger;

public class InitSceneCommand implements ICommand {
    @Override
    public String getName() {
        return "initScene";
    }

    @Override
    public String getDescription() {
        return "Initialises the default testing scene";
    }

    @Override
    public void run(String[] args) {
        Logger.log("Creating default developer scene...");
        Spectre.initScene();
        System.out.println(Spectre.scene);
    }
}
