package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class PrepLiftForAscentCommand extends SequentialCommandGroup {
    public PrepLiftForAscentCommand(IntakeManager intakeManager, OuttakeManager outtakeManager) {
        addCommands(

                new SetIntakeTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.LOWERED),
                new WaitCommand(500),
                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.HIGH),
                new WaitCommand(500),
                new SetIntakeSlidePositionCommand(intakeManager, IntakeManager._SlideState.RETRACTED),
                new SetIntakeTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.PACKED)
        );
    }
}
