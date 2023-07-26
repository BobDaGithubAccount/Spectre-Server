package api.command;

import gamelogic.Spectre;

import java.util.Arrays;

public class LoadLevelCommand implements ICommand {
    @Override
    public String getName() {
        return "load";
    }

    @Override
    public String getDescription() {
        return "Loads a level of specified name from the /saves folder, if it exists. Usage: load <name-of-save>";
    }

    @Override
    public CommandResponse run(String[] args) {
        if(args.length==0) {
            return new CommandResponse(false);
        }
        System.out.println(Arrays.toString(args));
        Spectre.loadLevel(args[0]);
        return new CommandResponse(true);
    }
}
