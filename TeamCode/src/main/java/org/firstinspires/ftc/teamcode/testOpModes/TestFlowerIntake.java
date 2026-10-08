package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.FlowerIntake;

@Disabled
@TeleOp
public class TestFlowerIntake extends OpMode {
    FlowerIntake flowerIntake = new FlowerIntake();

    @Override
    public void init() {
        flowerIntake.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.right_bumper) {
            flowerIntake.setFlowerIntake(1);
        } else if (gamepad1.left_bumper) {
            flowerIntake.setFlowerIntake(-1);
        } else {
            flowerIntake.setFlowerIntake(0);
        }
    }
}
