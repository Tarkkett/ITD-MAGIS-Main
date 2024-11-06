package org.firstinspires.ftc.teamcode.opMode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.PinpointDrive;
import org.firstinspires.ftc.teamcode.commands.auto.DriveToPositionCommand;

@Autonomous(name = "Main Auto", group = "OpMode", preselectTeleOp = "Main TeleOp")
@SuppressWarnings("unused")
public class MainAuto extends OpModeTemplate {

    Pose2d beginPose = new Pose2d(0, 0, 0);

    @Override
    public void init() {
        initSystems(true);
    }
    @Override
    public void start(){
        Actions.runBlocking(
                drive.actionBuilder(beginPose)
                        .strafeToConstantHeading(new Vector2d(5, 0))
                        .strafeToConstantHeading(new Vector2d(5, -5))
                        .strafeToConstantHeading(new Vector2d(0, -5))
                        .strafeToConstantHeading(new Vector2d(0, 0))
                        .turn(Math.PI)
                        .build());
    }

    @Override
    public void loop() {

    }
}
