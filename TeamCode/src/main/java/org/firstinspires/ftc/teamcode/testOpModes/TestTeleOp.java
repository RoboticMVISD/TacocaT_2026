package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.FlowerIntake;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Turret;

@TeleOp
//@Disabled
public class TestTeleOp extends OpMode {

    MecanumDrive drive = new MecanumDrive();
    Intake intake = new Intake();
    FlowerIntake flowerIntake = new FlowerIntake();
    Turret turret = new Turret();
    @Override
    public void init() {
        drive.init(hardwareMap);
        intake.init(hardwareMap);
    }

    @Override
    public void loop() {

        // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        // Note: pushing left stick forward gives negative value
        drive.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        //For Intake (test if trigger buttons work)
        if (gamepad1.right_trigger !=0 ) {
            intake.startBothIntake();
        } else if (gamepad1.left_trigger !=0) {
            intake.reverseBothIntake();
        } else {
            intake.stopIntake();
        }

        if (gamepad1.dpad_up) {
            intake.gateOpen();
        } else if (gamepad1.dpad_down) {
            intake.gateClosed();
        }

        if (gamepad2.a) {
            turret.setShooterPower(1);
        } else if (gamepad2.b) {
            turret.setShooterPower(-1);
        } else {
            turret.setShooterPower(0);
        }

        if (gamepad2.right_bumper) {
            turret.rotateTurretRight();
        } else if (gamepad2.left_bumper) {
            turret.rotateTurretLeft();
        } else {
            turret.stopTurretRotate();
        }
    }
}
