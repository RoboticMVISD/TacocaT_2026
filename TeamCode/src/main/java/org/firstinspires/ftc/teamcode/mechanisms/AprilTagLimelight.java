package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class AprilTagLimelight extends OpMode {

    private Limelight3A limelight;
    private IMU imu;

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "at_tracker");
        limelight.pipelineSwitch(0);   // April tag ??? pipeline

        // Upgrade this to the pinpoint IMU !!!!
        imu = hardwareMap.get(IMU.class, "imu");
        // This matches orientation of Control Hub on robot
        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT);

        imu.initialize(new IMU.Parameters(revOrientation));
    }

    @Override
    public void start() {
        limelight.start();

    }

    @Override
    public void loop() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        limelight.updateRobotOrientation(orientation.getYaw());
        LLResult llResult = limelight.getLatestResult();
        if (llResult != null && llResult.isValid()) {
            Pose3D botPose = llResult.getBotpose_MT2();
            //distance = getDistanceFromTag(llResult.getTa());
            //telemetry.addData("Distance", distance);
            telemetry.addData("Tx", llResult.getTx());
            telemetry.addData("Ty", llResult.getTy());
            telemetry.addData ("Ta", llResult.getTa());
            telemetry.addData("BotPose", botPose.toString());
            telemetry.addData("Yaw", botPose.getOrientation().getYaw());
        }
    }
/*
    public double getDistanceFromTag(double ta) {
        double scale 30665.95;
        double distance = (scale / ta);
        return distance;
    }

 */
}
