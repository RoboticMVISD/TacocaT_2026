package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp
public class AprilTagTracking extends OpMode {

    // Camera height in cm, camera angle in degrees
    private double CAMERA_HEIGHT_CM = 32.2;
    private double CAMERA_ANGLE = 44;
    private double GOAL_HEIGHT = 151.25;  // in cm
    private double distance = 0;

    private Limelight3A limelight;
    private IMU imu;

    @Override
    public void init() {
        // rename if using two Limelight cameras
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(9);    // april tag  ### pipleline

        // Upgrade this to pinpoint IMU !!!!
        imu = hardwareMap.get(IMU.class, "imu");

        // This matches orientation of Control Hub on robot
        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.UP);

        imu.initialize(new IMU.Parameters(RevOrientation));
    }

    @Override
    public void start() {

        limelight.start();
    }

    @Override
    public void loop() {
        // get yaw from control hub IMU
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        limelight.updateRobotOrientation((orientation.getYaw()));

        // get latest limelight result, pipeline ## for April Tag
        LLResult llResult = limelight.getLatestResult();
        if (llResult != null && llResult.isValid()) {
            Pose3D botPose = llResult.getBotpose_MT2();
            //distance = getDistanceFromTag(llResult.getTa());
            //telemetry.addData("Distance", distance);
            telemetry.addData("Tx", llResult.getTx());
            telemetry.addData("Ty", llResult.getTy());
            telemetry.addData("Ta", llResult.getTa());
            telemetry.addData("Botpose", botPose.toString());
            telemetry.addData("Yaw", botPose.getOrientation().getYaw());

            distance = getDistance(llResult.getTy());
            telemetry.addData("Distance", distance);
        } else {
            telemetry.addData("No Vaild Target", "found");
        }
    }

    /*
    public double getDistanceFromTag(double ta) {
        double scale 30665.95;
        double distance = (scale / ta);
        return distance;
    }
    */
    
    public double getDistance (double ty) {
        double angleToTarget = CAMERA_ANGLE + ty;
        double heightDifference = GOAL_HEIGHT - CAMERA_HEIGHT_CM;

        return heightDifference/Math.tan(Math.toRadians(angleToTarget));
    }
}
