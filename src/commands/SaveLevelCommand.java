package commands;

import gamelogic.Spectre;

import java.util.Arrays;

public class SaveLevelCommand implements ICommand {
    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getDescription() {
        return "Saves the current level to the /levels folder. Usage: save <name-of-save>";
    }

    @Override
    public void run(String[] args) {
        if(args.length==0) {return;}
        System.out.println(Arrays.toString(args));
        Spectre.dumpLevel(args[0]);
    }
}
