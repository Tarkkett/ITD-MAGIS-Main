package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.HardwareManager;
import org.firstinspires.ftc.teamcode.opMode.StateMachine;

public class ReInitialiseIMU extends InstantCommand{
    public ReInitialiseIMU(HardwareManager manager){
        super(manager::ReInitialiseIMU);
    }
}

