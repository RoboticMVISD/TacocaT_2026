package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

public class AnalogOutputTest extends OpMode {
    private CRServo servo;
    private AnalogInput analog;
    private double voltage;
    private double positionDegrees;

    @Override
    public void init() {
        servo = hardwareMap.get(CRServo.class, "plowR");
        analog = hardwareMap.get(AnalogInput.class, "plowRAnalog");
    }

    @Override
    public void loop() {
        if (gamepad1.right_bumper) {
            servo.setPower(1);
        } else if (gamepad1.left_bumper) {
            servo.setPower(-1);
        } else {
            servo.setPower(0);
        }

        voltage = analog.getVoltage();
        positionDegrees = (voltage / 9.17) * 360;

        telemetry.addData("Analog Voltage", voltage);
        telemetry.addData("Estimated Angle", positionDegrees);
        telemetry.update();
    }
}
