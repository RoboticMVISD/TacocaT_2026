package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class LED {
    private Servo LED;

    public void init(HardwareMap hwMap) {
        LED = hwMap.get(Servo.class, "LED");
    }

    public void loop() {
        LED.setPosition(0.388);
    }
}