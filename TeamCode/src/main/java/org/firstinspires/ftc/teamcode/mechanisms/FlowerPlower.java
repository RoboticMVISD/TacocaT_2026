package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class FlowerPlower {
    private Servo FlowerPlowerL;
    private Servo FlowerPlowerR;

    public void init(HardwareMap hwMap) {
        FlowerPlowerL = hwMap.get(Servo.class, "plowL");
        FlowerPlowerR = hwMap.get(Servo.class, "plowR");
        plowUp();
    }
    public void plowDown() {
        FlowerPlowerL.setPosition(0);
        FlowerPlowerR.setPosition(1);
    }
    public void plowUp() {
        FlowerPlowerL.setPosition(1);
        FlowerPlowerR.setPosition(0);
    }
}
