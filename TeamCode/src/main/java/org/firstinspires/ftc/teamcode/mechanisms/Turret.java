package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.AnalogInput;
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
    public static DcMotorEx shooter1;
    static DcMotorEx shooter2;
    static CRServo rotator1;
    static CRServo rotator2;
    private static AnalogInput turretTracker1;
    private static AnalogInput turretTracker2;

    private static double voltage1, voltage2;
    private static double currentReadingDegrees1 = 0, currentReadingDegrees2 = 0;
    private static double lastReadingDegrees1 = 0;
    private static double lastReadingDegrees2 = 0;
    private static double finalDegrees1 = 0;
    private static double finalDegrees2 = 0;
    private static double totalServoDegrees = 0;
    public static double totalTurretDegrees = 0;
    private static boolean firstRead = true;
    public static double velocity;
    public static double currentVelocity;
    public static double maxVelocity = 0;
    public static double predictedP, predictedI, predictedD, predictedF;

    // ---- PIDF Variables to optimize shooting and reduce oscillations, overshoot, undershoot, etc ---- //
    public static double proportional = 200;
    public static double integral = 0.1251065;
    public static double derivative = 0;
    public static double feedForward = 12.5065;
    public static PIDFCoefficients pidfCoefficients = new PIDFCoefficients(proportional, integral, derivative, feedForward);

    public void init(HardwareMap hardwareMap) {
        shooter1 = hardwareMap.get(DcMotorEx.class, "bottomShooter");
        shooter2 = hardwareMap.get(DcMotorEx.class, "topShooter");
        rotator1 = hardwareMap.get(CRServo.class, "rotatorL");
        rotator2 = hardwareMap.get(CRServo.class, "rotatorR");

        shooter1.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter2.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter1.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        shooter2.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        turretTracker1 = hardwareMap.get(AnalogInput.class, "turretTrackerL");
        turretTracker2 = hardwareMap.get(AnalogInput.class, "turretTrackerR");
    }

    public void loop() {
        trackTurretAngle();
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

    public static void stopShooter() {
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
    public static void stopTurretRotate() {
        rotator1.setPower(0);
        rotator2.setPower(0);
    }

    public static void setTurretRotate(double power) {
        rotator1.setPower(power);
        rotator2.setPower(power);
    }

    public static void setLauncherVelocity(double Velocity) {
        shooter1.setVelocity(Velocity);
        shooter2.setVelocity(Velocity);
    }

    public static void trackTurretAngle() {
        voltage1 = turretTracker1.getVoltage();
        voltage2 = turretTracker2.getVoltage();

        //💥convert voltage reading to degrees, max voltage for an analog input is 3.3V
        currentReadingDegrees1 = (voltage1 / 3.3) * 360;
        currentReadingDegrees2 = (voltage2 / 3.3) * 360;

        if (firstRead) {
            lastReadingDegrees1 = currentReadingDegrees1;
            finalDegrees1 = currentReadingDegrees1;
            lastReadingDegrees2 = currentReadingDegrees2;
            finalDegrees2 = currentReadingDegrees2;
            firstRead = false;
        }
        //🚀detect rollover if the change in servo position is greater than the 180 degree threshold
        double delta1 = currentReadingDegrees1 - lastReadingDegrees1;
        double delta2 = currentReadingDegrees2 - lastReadingDegrees2;
        if (delta1<-180) {
            delta1 += 360;
        }
        if (delta1>180) {
            delta1 -= 360;
        }
        if (delta2<-180) {
            delta2 += 360;
        }
        if (delta2>180) {
            delta2 -= 360;
        }
        finalDegrees1 += delta1;
        finalDegrees2 += delta2;

        totalServoDegrees = (finalDegrees1 + finalDegrees2) / 2;
        totalTurretDegrees = totalServoDegrees * 1.76;

        lastReadingDegrees1 = currentReadingDegrees1;
        lastReadingDegrees2 = currentReadingDegrees2;
    }
}