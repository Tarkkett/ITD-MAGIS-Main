package org.firstinspires.ftc.teamcode.commands.low_level.intake

import com.arcrobotics.ftclib.command.InstantCommand
import org.firstinspires.ftc.teamcode.managers.IntakeManager

class AdjustYawServoCommand(
    manager: IntakeManager,
    state: IntakeManager._YawServoState,
    i: Double = 0.0
) : InstantCommand({ manager.update(state, i) }) {

    /**
     * Intake Yaw Servo
     * @param manager IntakeManager
     * @param state _YawServoState
     * @constructor
     */
    constructor(manager: IntakeManager, state: IntakeManager._YawServoState) : this(manager, state, 0.0)

}
