package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.PollenTracker;

@Disabled
@TeleOp
public class TestLimelight extends OpMode    {
    PollenTracker pollenTracker = new PollenTracker();

    @Override
    public void init() {
        pollenTracker.init(hardwareMap, this);
    }

    @Override
    public void start() {
        pollenTracker.start();
    }

    @Override
    public void loop() {
        pollenTracker.loop();
    }
}
