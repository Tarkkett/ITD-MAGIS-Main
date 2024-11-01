package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.LiftManager;

public class StateMachine implements Runnable {

    DriveManager driveManager;
    LiftManager liftManager;
    public volatile boolean operational = true;

    public StateMachine(LiftManager liftManager, DriveManager driveManager){
        this.liftManager = liftManager;
        this.driveManager = driveManager;
    }



    @Override
    public void run() {
        while(operational){
            liftManager.loop();

        }
    }

    public void stop(){ operational = false; }
}
