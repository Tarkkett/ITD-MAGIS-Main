package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class StateMachine implements Runnable {

    DriveManager driveManager;
    OuttakeManager liftManager;
    public volatile boolean operational = true;

    public StateMachine(OuttakeManager liftManager, DriveManager driveManager){
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
