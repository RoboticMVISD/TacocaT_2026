package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Autonomous
public class PollenTracker extends OpMode {

    private Limelight3A limelight3A;
    private MecanumDrive drive;
    private IMU imu;

    public void init(HardwareMap hwMap) {
        // Initialize Limelight3A
        limelight3A = hwMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(0);

        // Initialize Mecanum Drive
        drive.init(hwMap);

        // Initialize hardware variable for IMU.
        imu = hwMap.get(IMU.class, "imu");

        // This needs to match orientation of Control Hub on robot
        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT);

        imu.initialize(new IMU.Parameters(RevOrientation));
    }

    // start Limelight when start button is pressed
    public void start() {
        limelight3A.start();
    }

    public void loop() {

        // find robot's current heading
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        limelight3A.updateRobotOrientation(orientation.getYaw(AngleUnit.RADIANS));

        // Capture field view from Limelight
        LLResult llResult = limelight3A.getLatestResult();

        if (llResult != null & llResult.isValid()) {

            Pose3D botpose =llResult.getBotpose(); // don't know if this is needed
            double tx = llResult.getTx();
            double ty = llResult.getTy();

            telemetry.addData("Target X offset", tx);
            telemetry.addData("Target Y offset", ty);
            telemetry.addData("Target Area", llResult.getTa());
            telemetry.addData("Robot Yaw",botpose.getOrientation().getYaw());

            driveToPollen(tx,ty);
        }
    }

    public void driveToPollen (double heading_error, double distance_error) {
        double Kp = -0.1;
        double min_command = 0.05;

        double steering_adjust = 0.0;
        double speed_adjust = 0.0;

        // Calculate rotation
        /*if (Math.abs(heading_error) > 1.0) {
            heading_error /= 160;      // resolution is 320 in x

            if (heading_error < 0) {
                steering_adjust = Kp*heading_error + min_command;
            }
            else {
                steering_adjust = Kp*heading_error - min_command;
            }
        }
        */
        // Calculate speed
        /*if (Math.abs(distance_error) > 1.0) {
            distance_error /= 120;       // resolution is 240 in y

            if (distance_error < 0) {
                speed_adjust = Kp*distance_error + min_command;
            }
            else {
                speed_adjust = Kp*heading_error - min_command;
            }
        }

         */
        telemetry.addData("Heading error", heading_error);
        telemetry.addData("Distance error", distance_error);

        /*
        telemetry.addData("Steering Adjust", steering_adjust);
        telemetry.addData("Speed Adjust", speed_adjust);
        */

        /*
        drive(speed_adjust, 0.0, steering_adjust);
        */

    }
    }
}
