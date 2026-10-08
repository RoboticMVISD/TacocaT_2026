package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class FlowerIntake {
    private CRServo FlowerIntake;

    public void init(HardwareMap hwMap) {
        FlowerIntake = hwMap.get(CRServo.class, "flowerIntake");
    }

    public void setFlowerIntake(double speed) {
        FlowerIntake.setPower(speed);
    }
}
