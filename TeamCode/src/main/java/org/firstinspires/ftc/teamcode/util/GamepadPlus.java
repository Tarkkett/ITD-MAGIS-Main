package org.firstinspires.ftc.teamcode.util;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.sun.tools.javac.util.Pair;

public class GamepadPlus extends GamepadEx {
    public GamepadKeys.Button circle = GamepadKeys.Button.B;
    public GamepadKeys.Button cross = GamepadKeys.Button.A;
    public GamepadKeys.Button triangle = GamepadKeys.Button.Y;
    public GamepadKeys.Button square = GamepadKeys.Button.X;
    public GamepadKeys.Button share = GamepadKeys.Button.BACK;
    public GamepadKeys.Button options = GamepadKeys.Button.START;
    public GamepadKeys.Button dpad_Up = GamepadKeys.Button.DPAD_UP;
    public GamepadKeys.Button dpad_Down = GamepadKeys.Button.DPAD_DOWN;
    public GamepadKeys.Button dpad_Left = GamepadKeys.Button.DPAD_LEFT;
    public GamepadKeys.Button dpad_Right = GamepadKeys.Button.DPAD_RIGHT;
    public GamepadKeys.Button leftBumper = GamepadKeys.Button.LEFT_BUMPER;
    public GamepadKeys.Button rightBumper = GamepadKeys.Button.RIGHT_BUMPER;
    public GamepadKeys.Button leftStick = GamepadKeys.Button.LEFT_STICK_BUTTON;
    public GamepadKeys.Button rightStick = GamepadKeys.Button.RIGHT_STICK_BUTTON;


    public GamepadPlus(Gamepad gamepad) {
        super(gamepad);
    }

    //! Override values
    public void rumble (int durationMs){
        super.gamepad.rumble(durationMs);
    }

    public float leftTrigger(){
        return super.gamepad.left_trigger;
    }

    public float rightTrigger(){
        return super.gamepad.right_trigger;
    }

    public boolean driveInput() {
        return (getRightX() > 0.1 || getRightX() < -0.1 || getRightY() > 0.1 || getRightY() < -0.1);
    }

    public void setLedColor(double r, double g, double b, int durationMs) {
        super.gamepad.setLedColor(r, g, b, durationMs);
    }
}