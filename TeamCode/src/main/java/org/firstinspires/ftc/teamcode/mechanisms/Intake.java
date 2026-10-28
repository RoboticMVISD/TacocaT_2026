package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    private final double STOP_SPEED = 0.0; //We send this power to the servos when we want them to stop.
    private final double FULL_SPEED = 1.0;
    private final double REVERSE_SPEED = -1.0;
    private DcMotor Intake;
    private DcMotor Intake2;
    private Servo IntakeGate;
    boolean gateOpen = false;

    public void init(HardwareMap hwMap) {
        Intake = hwMap.get(DcMotor.class,"intakeFront");  //previously intake
        Intake2 = hwMap.get(DcMotor.class, "intakeBack"); //previously intake2
        IntakeGate = hwMap.get(Servo.class, "intakeGate");

        // Set launcher motor to RUN_USING_ENCODER and BRAKE to slow down faster than coasting.
        Intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Intake.setDirection(DcMotor.Direction.REVERSE);

        Intake2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Intake2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Intake2.setDirection(DcMotor.Direction.FORWARD);

        stopIntake();
        gateOpen();
    }

    public void loop() {
        if (gateOpen) {
            gateOpen();
        } else {
            gateClosed();
        }
    }

    public void stopIntake() {
        Intake.setPower(STOP_SPEED);
        Intake2.setPower(STOP_SPEED);
    }

    public void startBothIntake() {
        Intake.setPower(FULL_SPEED);
        Intake2.setPower(FULL_SPEED);
    }
    public void startFrontIntake() {
        Intake.setPower(FULL_SPEED);
    }
    public void startBackIntake() {
        Intake2.setPower(FULL_SPEED);
    }

    public void reverseBothIntake() {
        Intake.setPower(REVERSE_SPEED);
        Intake2.setPower(REVERSE_SPEED);
    }

    public void moveGate() {
        gateOpen = !gateOpen;
    }

    public void gateOpen() {
        IntakeGate.setPosition(1);
    }

    public void gateClosed() {
        IntakeGate.setPosition(-1);
    }
}