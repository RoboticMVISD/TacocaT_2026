package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.sun.tools.javac.comp.Todo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ShooterPIDFTesting {
    // --------- Making an OpMode object to use for hardware maps, gamepads, etc ------------ //
    static OpMode op;
    // -------- Objects for the hardware being used by turret & shooters //
    public static DcMotorEx bottomShooter, topShooter;
    public static CRServo turretRotatorCR, turretRotatorCR2;


    // ----- Variables for the controller two tester to find a optimal velocity for a certain range ------ //
    double TESTVELOCITY = 500;
    int SMALL_INCREMENT = 50;
    int LARGE_INCREMENT = 100;

    //Max Velocity range is -2500 to 2500
    //These are preset velocity values for certain ranges from the goal.
    //Found using the Controller two test method
    public static double SPIN_UP_VELOCITY_SHORTRANGE = 950;
    public static double SPIN_UP_VELOCITY_MEDIUMRANGE = 1050;
    public static double SPIN_UP_VELOCITY_LONGRANGE = 1225;
    public static double SPIN_UP_VELOCITY_XLRANGE = 1550;

    // ---- Variable Created to reverse the shooter if needed to put a ball back in the robot ----- /
    public static double BALL_REVERSE_SPEED = -500;


    // ---- PIDF Variables to optimize shooting and reduce oscillations, overshoot, undershoot, etc ---- //
    public static double proportional = 900;
    public static double integral = 0;
    public static double derivative = 0.001;
    public static double feedForward = 35;
    public static PIDFCoefficients pidfCoefficients = new PIDFCoefficients(proportional, integral, derivative, feedForward);

    // Boolean created to differentiate between whether we want to set all controls
    // To one controller to make testing easier, or have them separate for actual driving
    // practice or competition
    public static boolean testMode;
    private static double currentTurretPosition;
    private static Telemetry telemetry;


    // ---- Initializes Shooter Motors With PIDF Variables and Encoders, as well as the turret rotating CRServo ----- //
    public static void init(OpMode OP, boolean isTesting) {
        op = OP;

        testMode = isTesting;

        bottomShooter = op.hardwareMap.get(DcMotorEx.class, "bottomShooter");
        bottomShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bottomShooter.setDirection(DcMotorSimple.Direction.REVERSE);
        bottomShooter.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        bottomShooter.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        bottomShooter.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        topShooter = op.hardwareMap.get(DcMotorEx.class, "topShooter");
        topShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topShooter.setDirection(DcMotorSimple.Direction.FORWARD);
        topShooter.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        topShooter.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        topShooter.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        // ----- Commented out as there is no rotator yet, but I'm leaving it for when we have one -------//

        /*turretRotatorCR = op.hardwareMap.crservo.get("turretRotatorCR");
        turretRotatorCR2 = op.hardwareMap.crservo.get("turretRotatorCR2");
        turretRotatorCR2.setDirection(DcMotorSimple.Direction.REVERSE);*/

        telemetry = OP.telemetry;
    }


    // -- loop that activates testing for PIDF accuracy --------/
    //TODO: Create Methods for shooter controls and replace it in the loop
    public void loop() throws InterruptedException {
        testerConTwo();
    }


    // --- Two "OverLoaded" methods created to make changing velocities for both shooters simultaneously easier --- //
    // "OverLoaded" = Two Methods with the same name and identifiers, but different parameters
    public static void setShooterPower(double Vel) {
        bottomShooter.setVelocity(Vel); topShooter.setVelocity(Vel);
    }
    public static void setShooterPower(double Vel, double shooterTurretPos, Servo turretRotator){
        bottomShooter.setVelocity(Vel); topShooter.setVelocity(Vel);
        turretRotator.setPosition(shooterTurretPos);
    }

    // - Method utilizing the test variables to have an adjustable velocity to find optimal velocity to hit a certain target ----- //
    public void testerConTwo(){

        // A = Set to chosen velocity
        // Dpad L/R to change the Test Velocity by a Small increment
        // Dpad U/D to change the Test Velocity by a Large Increment
        // Increments can be changed above where the variables are created.
            if (op.gamepad2.a){
                setShooterPower(TESTVELOCITY);
            } else if (op.gamepad2.dpadLeftWasPressed()){
                TESTVELOCITY -= SMALL_INCREMENT;
            } else if (op.gamepad2.dpadRightWasPressed()){
                TESTVELOCITY += SMALL_INCREMENT;
            } else if (op.gamepad2.dpadDownWasPressed()){
                TESTVELOCITY -= LARGE_INCREMENT;
            } else if (op.gamepad2.dpadUpWasPressed()){
                TESTVELOCITY += LARGE_INCREMENT;
            } else if(op.gamepad2.b){
                TESTVELOCITY = TESTVELOCITY*-1;
            }else {
                setShooterPower(0);
            }

        // ----- Telemetry To report the current velocities on both sides, as well as what it SHOULD be ----- /
        op.telemetry.addLine("Spin Power Left: " + bottomShooter.getVelocity() + " \nSpin Power Right: " + bottomShooter.getVelocity() + "\nWhat Power Should be: " + TESTVELOCITY);

    }

    // Two misc. methods for moving and using a dual servo turret rotation system //
    public void turretTracker(){
        //currentTurretPosition = Movement.frontLeftDrive.getCurrentPosition();
        telemetry.addData("Current Turret Position: ", currentTurretPosition);
        telemetry.update();
    }

    public static void setRotatorPower(double power){
        turretRotatorCR2.setPower(power);
        turretRotatorCR.setPower(power);
    }
}