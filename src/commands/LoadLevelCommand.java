package commands;

import gamelogic.Spectre;

import java.util.Arrays;

public class LoadLevelCommand implements ICommand {
    @Override
    public String getName() {
        return "load";
    }

    @Override
    public String getDescription() {
        return "Loads a level of specified name from the /levels folder, if it exits. Usage: load <name-of-save>";
    }

    @Override
    public void run(String[] args) {
        if(args.length==0) {return;}
        System.out.println(Arrays.toString(args));
        Spectre.loadLevel(args[0]);
    }
}
