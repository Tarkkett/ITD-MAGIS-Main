package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomCommandScheduler implements Runnable {

    private List<Command> commandList = new ArrayList<>();

    private static CustomCommandScheduler instance;

    private CustomCommandScheduler() {}

    public static CustomCommandScheduler getInstance() {
        if (instance == null) {
            instance = new CustomCommandScheduler();
        }
        return instance;
    }

    public void schedule(Command command) {
        command.initialize();
        commandList.add(command);
    }

    @Override
    public void run() {
        Iterator<Command> iterator = commandList.iterator();

        while (iterator.hasNext()) {
            Command command = iterator.next();

            command.execute();

            if (command.isFinished()) {
                command.end(false);
                iterator.remove();
            }
        }
    }

    public void cancel(Command command) {
        if (commandList.contains(command)) {
            command.end(true);
            commandList.remove(command);
        }
    }

    public void cancelAll() {
        for (Command command : commandList) {
            command.end(true);
        }
        commandList.clear();
    }
}
