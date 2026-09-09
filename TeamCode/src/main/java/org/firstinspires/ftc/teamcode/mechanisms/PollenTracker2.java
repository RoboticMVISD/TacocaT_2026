package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Autonomous
public class PollenTracker2 extends OpMode {

    private Limelight3A limelight3A;
    MecanumDrive drive = new MecanumDrive();

    public void init() {
        // Initialize Limelight3A
        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(0);

        // Initialize Mecanum Drive
        drive.init(hardwareMap);
    }

    // start Limelight when start button is pressed
    public void start() {
        limelight3A.start();
    }

    public void loop() {

        // Capture field view from Limelight
        LLResult llResult = limelight3A.getLatestResult();

        if (llResult != null & llResult.isValid()) {

            double tx = llResult.getTx();
            double ty = llResult.getTy();

            telemetry.addData("Target X offset", tx);
            telemetry.addData("Target Y offset", ty);
            telemetry.addData("Target Area", llResult.getTa());

            driveToPollen(tx, ty);
        }
    }

    public void driveToPollen(double headingError, double distanceError) {
        double KpSteer = -1;
        double KpSpeed = 1;

        double steeringAdjust = 0.0;
        double speedAdjust = 0.0;

        // Calculate rotation
        steeringAdjust = KpSteer * headingError / 27.25;

        // Calculate speed
        speedAdjust = KpSpeed * (distanceError / 40) + 0.5;  // this formula needs to zero when
        // the pollen gets sucked in

        telemetry.addData("Heading error", headingError);
        telemetry.addData("Distance error", distanceError);

        telemetry.addData("Steering Adjust", steeringAdjust);
        telemetry.addData("Speed Adjust", speedAdjust);

        /*
        drive(speedAdjust, 0.0, steeringAdjust);
        */

    }
}

