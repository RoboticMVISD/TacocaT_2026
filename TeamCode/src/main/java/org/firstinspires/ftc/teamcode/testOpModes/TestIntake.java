package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;

@TeleOp
public class TestIntake extends OpMode {
    Intake intake = new Intake();

    @Override
    public void init() {
        intake.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.right_trigger > 0) {
            intake.startBothIntake();
        } else if (gamepad1.left_trigger > 0) {
            intake.reverseBothIntake();
        } else if (gamepad1.a) {
            intake.startFrontIntake();
        } else if (gamepad1.y) {
            intake.startBackIntake();
        } else {
            intake.stopIntake();
        }

        if (gamepad1.dpad_up) {
            intake.gateOpen();
        } else if (gamepad1.dpad_down) {
            intake.gateClosed();
        }
    }
}
