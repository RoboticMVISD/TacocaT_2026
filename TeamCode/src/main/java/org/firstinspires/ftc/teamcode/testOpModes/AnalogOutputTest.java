package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class AnalogOutputTest extends OpMode {
    private CRServo servo;
    private AnalogInput analog;
    private double voltage;
    private double positionDegrees = 0;

    @Override
    public void init() {
        servo = hardwareMap.get(CRServo.class, "servo");
        analog = hardwareMap.get(AnalogInput.class, "analog");

        positionDegrees = 0;
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
