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
    CRServo rotator1, rotator2;
    DcMotor turretTracker;

    public static double velocity;
    public static double currentVelocity;
    public static double maxVelocity = 0;
    public static double predictedP, predictedI, predictedD, predictedF;

    // ---- PIDF Variables to optimize shooting and reduce oscillations, overshoot, undershoot, etc ---- //
    public static double proportional = 1.25065;
    public static double integral = 0.1251065;
    public static double derivative = 0;
    public static double feedForward = 12.5065;
    public static PIDFCoefficients pidfCoefficients = new PIDFCoefficients(proportional, integral, derivative, feedForward);

    public void init(HardwareMap hardwareMap) {
        shooter1 = hardwareMap.get(DcMotorEx.class, "bottomShooter");
        shooter2 = hardwareMap.get(DcMotorEx.class, "topShooter");
        rotator1 = hardwareMap.get(CRServo.class, "rotatorL");
        rotator2 = hardwareMap.get(CRServo.class, "rotatorR");

        shooter1.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
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

    public void PIDFCalibration() {
        currentVelocity = shooter1.getVelocity();

        if (currentVelocity > maxVelocity) {
            maxVelocity = currentVelocity;
        }

        predictedF = 32767 / maxVelocity;
        predictedP = 0.1 * predictedF;
        predictedI = 0.1 * predictedP;
        predictedD = 0;
    }

    public void changeSetVelocity(double changeValue) {
        velocity += changeValue;
    }

    public void startShooter() {
        shooter1.setVelocity(velocity);
        shooter2.setVelocity(velocity);
    }

    public void stopShooter() {
        shooter1.setVelocity(0);
        shooter2.setVelocity(0);
    }

    public void rotateTurretRight() {
        rotator1.setPower(1);
        rotator2.setPower(1);
    }
    public void rotateTurretLeft() {
        rotator1.setPower(-1);
        rotator2.setPower(-1);
    }
    public void stopTurretRotate() {
        rotator1.setPower(0);
        rotator2.setPower(0);
    }
}