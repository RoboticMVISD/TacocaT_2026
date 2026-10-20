package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.FlowerIntake;
import org.firstinspires.ftc.teamcode.mechanisms.FlowerPlower;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.ShooterPIDFTesting;
import org.firstinspires.ftc.teamcode.mechanisms.Turret;

@TeleOp
//@Disabled
public class TestTeleOp extends OpMode {

    MecanumDrive drive = new MecanumDrive();
    Intake intake = new Intake();
    FlowerPlower plow = new FlowerPlower();
    FlowerIntake flowerIntake = new FlowerIntake();
    Turret turret = new Turret();
    org.firstinspires.ftc.teamcode.mechanisms.ShooterPIDFTesting shooter = new org.firstinspires.ftc.teamcode.mechanisms.ShooterPIDFTesting();
    @Override
    public void init() {
        drive.init(hardwareMap);
        intake.init(hardwareMap);
        plow.init(hardwareMap);
        ShooterPIDFTesting.init(this, true);
        //turret.init(hardwareMap);
    }

    @Override
    public void loop() {

        //Shooter Implemented by Justin (Me) to tune PIDF and make shooters work.
        //Test Classes work, just need to implement controls for the driver who uses shooter
        //Don't delete test classes if you think of it. Comment them out for later

        try {
            shooter.loop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



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

        if (gamepad1.y) {
            intake.gateOpen();
        } else if (gamepad1.b) {
            intake.gateClosed();
        }
        if (gamepad1.x) {
            plow.plowUp();
        } else if (gamepad1.a) {
            plow.plowDown();
        }
    }
}




        /*if (gamepad2.a) {
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

         */
