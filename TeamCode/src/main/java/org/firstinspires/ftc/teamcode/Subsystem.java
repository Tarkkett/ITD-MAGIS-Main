package org.firstinspires.ftc.teamcode;

public abstract class Subsystem implements State {
    abstract String getName();

    Command command;

    public Subsystem(){}

    public void setCommand(Command command){
        this.command = command;
    }

    abstract void setSubsytemState();

    public void ExecuteCommand(){
        command.execute();
    }


}
