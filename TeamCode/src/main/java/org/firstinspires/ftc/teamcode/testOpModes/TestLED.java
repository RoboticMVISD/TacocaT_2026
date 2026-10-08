package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.LED;

@Disabled
@TeleOp
public class TestLED extends OpMode {
    LED led = new LED();

    @Override
    public void init() {
        led.init(hardwareMap);
    }

    @Override
    public void loop() {
        led.loop();
    }
}