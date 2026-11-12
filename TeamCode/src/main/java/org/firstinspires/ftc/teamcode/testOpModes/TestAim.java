package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.PositionalAim;
import org.firstinspires.ftc.teamcode.mechanisms.Turret;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class TestAim extends OpMode {
    PositionalAim aim = new PositionalAim();
    Turret turret = new Turret();
    Intake intake = new Intake();

    @Override
    public void init() {
        intake.init(hardwareMap);
        turret.init(hardwareMap);
        aim.init(hardwareMap, this, true, true, null, turret);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        aim.loop();

        if (gamepad1.dpadUpWasPressed()) {
            aim.aimEnabled = !aim.aimEnabled;
        } else if (gamepad1.dpadDownWasPressed()) {
            aim.launchEnabled = !aim.launchEnabled;
        }

        if (!aim.aimEnabled) {
            if (gamepad1.right_bumper) {
                turret.rotateTurretRight();
            } else if (gamepad1.left_bumper) {
                turret.rotateTurretLeft();
            } else {
                turret.stopTurretRotate();
            }
        }
        if (!aim.launchEnabled) {
            turret.stopShooter();
        }

        if (gamepad1.right_trigger_pressed) {
            intake.startBothIntake();
        } else if (gamepad1.left_trigger_pressed) {
            intake.reverseBothIntake();
        } else {
            intake.stopIntake();
        }
    }
}
