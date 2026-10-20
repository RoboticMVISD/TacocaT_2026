package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
//import com.pedropathing.follower.Follower;

//import org.firstinspires.ftc.teamcode.pedro.Constants;


public class Turret {
    DcMotorEx shooter1, shooter2;
    CRServo rotator;
    DcMotor turretTracker;

    // ---- PIDF Variables to optimize shooting and reduce oscillations, overshoot, undershoot, etc ---- //
    public static double proportional = 300;
    public static double integral = 0;
    public static double derivative = 0.001;
    public static double feedForward = 10;
    public static PIDFCoefficients pidfCoefficients = new PIDFCoefficients(proportional, integral, derivative, feedForward);

    public void init(HardwareMap hardwareMap) {
        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        rotator = hardwareMap.get(CRServo.class, "rotator");

        shooter1.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooter1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter1.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        shooter2.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        turretTracker = hardwareMap.dcMotor.get("backLeft");
        turretTracker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretTracker.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setShooterPower(float power) {
        shooter1.setPower(power);
        shooter2.setPower(power);
    }

    public void rotateTurretRight() {
        rotator.setPower(1);
    }
    public void rotateTurretLeft() {
        rotator.setPower(-1);
    }
    public void stopTurretRotate() {
        rotator.setPower(0);
    }
}