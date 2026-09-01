package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

//Control HUB
//Motor 0 = frontLeft
//Motor 1 = frontRight
//Motor 2 = backLeft
//Motor 3 = backRight

//Servo 1 =
//Servo 2 =

//Expansion HUB
//Motor 0 = intake
//Motor 1 =
//Motor 2 =
//Motor 3 =

public class MecanumDrive {

    // Declare OpMode members for each of the 4 motors.
    private DcMotor frontLeftDrive, backLeftDrive, frontRightDrive, backRightDrive;
    private IMU imu;
    // TODO: likely change this back to 1
    private final double SPIN_DAMPING = 1.0;   // higher means slower turning

    public void init(HardwareMap hwMap) {

        // Initialize hardware variables for drive motors. Note that strings used here must
        // correspond to names assigned during the robot configuration step on Driver Hub.
        frontLeftDrive = hwMap.get(DcMotor.class, "frontLeft");
        backLeftDrive = hwMap.get(DcMotor.class, "backLeft");
        frontRightDrive = hwMap.get(DcMotor.class, "frontRight");
        backRightDrive = hwMap.get(DcMotor.class, "backRight");

        // Set the left motors in reverse which is needed for drive trains where the left
        // motors are opposite to the right ones.
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        // This uses RUN_USING_ENCODER to enable field orientated driving
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // This sets the motor stop behavior
        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize hardware variable for IMU.
        imu = hwMap.get(IMU.class, "imu");

        // This needs to be changed to match orientation of Control Hub on robot
        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT);

        imu.initialize(new IMU.Parameters(RevOrientation));
    }

    public void drive(double forward, double strafe, double rotate) {

        // apply damping to spin
        rotate /= SPIN_DAMPING;

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        double frontLeftPower = forward + strafe + rotate;
        double frontRightPower = forward - strafe - rotate;
        double backLeftPower = forward - strafe + rotate;
        double backRightPower = forward + strafe - rotate;

        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        double maxPower = 1.0;
        double maxSpeed = 1.0;     // change for outreach events

        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));

        // Send calculated power to wheels
        frontLeftDrive.setPower(maxSpeed * (frontLeftPower / maxPower));
        frontRightDrive.setPower(maxSpeed * (frontRightPower / maxPower));
        backLeftDrive.setPower(maxSpeed * (backLeftPower / maxPower));
        backRightDrive.setPower(maxSpeed * (backRightPower / maxPower));

        // Show the elapsed game time and wheel power.
        //telemetry.addData("Status","Run Time: "+runtime.toString());
        //telemetry.addData("Front left/Right","%4.2f, %4.2f",frontLeftPower,frontRightPower);
        //telemetry.addData("Back  left/Right","%4.2f, %4.2f",backLeftPower,backRightPower);
        //telemetry.update();
    }

    public void driveFieldRelative (double forward, double strafe, double rotate) {

        // Convert Cartesian coordinates to Polar coordinates
        double theta = Math.atan2(forward, strafe);
        double r = Math.hypot(strafe, forward);

        // Rotate angle based on robot's current yaw angle read from the IMU
        theta = AngleUnit.normalizeRadians(theta -
                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        // Convert back to Cartesian coordinates
        double newForward = r * Math.sin(theta);
        double newStrafe = r * Math.cos(theta);

        // Call drive method with robot relative forward and strafe values
        this.drive(newForward, newStrafe, rotate);
    }


    public void testWheelDirection(boolean front_left, boolean front_right,
                                   boolean back_left, boolean back_right) {

        // This is test code:
        //
        // Each button should make the corresponding motor run FORWARD.
        //   1) First get all the motors to take to correct positions on the robot
        //      by adjusting your Robot Configuration if necessary.
        //   2) Then make sure they run in the correct direction by modifying the
        //      the setDirection() calls above.


        double frontLeftPower  = front_left ? 1.0 : 0.0; // X gamepad
        double frontRightPower = front_right ? 1.0 : 0.0; // A gamepad
        double backLeftPower   = back_left ? 1.0 : 0.0; // Y gamepad
        double backRightPower  = back_right ? 1.0 : 0.0; // B gamepad


        // Send calculated power to wheels
        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);
    }
}
