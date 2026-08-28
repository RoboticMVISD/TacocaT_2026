package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;

@Disabled
@TeleOp
public class TestMecanum extends OpMode {
    MecanumDrive drive = new MecanumDrive();

    @Override
    public void init() {
        drive.init(hardwareMap);
    }

    @Override
    public void loop() {

        // from either controller, drive each motor forward individually to verify direction is correct
        if (gamepad2.x || gamepad1.x) { // front_left
            drive.testWheelDirection(true, false, false, false);
            telemetry.addLine("Front Left Forward");

        } else if (gamepad2.a || gamepad1.a) { // front right
            drive.testWheelDirection(false, true, false, false);
            telemetry.addLine("Front Right Forward");

        } else if(gamepad2.y || gamepad1.y) { // back left
            drive.testWheelDirection(false, false, true, false);
            telemetry.addLine("Back Left Forward");

        } else if(gamepad2.b || gamepad1.b) { // back right
            drive.testWheelDirection(false, false, false, true);
            telemetry.addLine("Back Right Forward");
        }
    }
}
