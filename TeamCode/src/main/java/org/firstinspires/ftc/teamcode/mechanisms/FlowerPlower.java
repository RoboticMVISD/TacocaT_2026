package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class FlowerPlower {
    public static Servo FlowerPlower;

    public static void init(HardwareMap hwMap) {
        FlowerPlower = hwMap.get(Servo.class, "flowerIntake");
        plowUp();
    }
    public static void plowDown() {
        FlowerPlower.setPosition(1);
    }
    public static void plowUp() {
        FlowerPlower.setPosition(-1);
    }
}
