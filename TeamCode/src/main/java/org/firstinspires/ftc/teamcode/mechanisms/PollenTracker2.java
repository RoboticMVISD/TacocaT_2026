package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous
public class PollenTracker2 extends OpMode {

    private Limelight3A limelight3A;
    MecanumDrive drive = new MecanumDrive();
    Intake intake = new Intake();

    @Override
    public void init() {
        // Initialize Limelight3A
        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(9);

        // Initialize Mecanum Drive
        drive.init(hardwareMap);
        intake.init(hardwareMap);
    }

    @Override
    // start Limelight when start button is pressed
    public void start() {
        limelight3A.start();
    }

    @Override
    public void loop() {

        // Capture field view from Limelight
        LLResult llResult = limelight3A.getLatestResult();

        if (llResult != null & llResult.isValid()) {

            double tx = llResult.getTx();
            double ty = llResult.getTy();
            double ta = llResult.getTa();

            telemetry.addData("Target X offset", tx);
            telemetry.addData("Target Y offset", ty);
            telemetry.addData("Target Area", ta);

            if (ta . 0.1){
                if (tx >= -23 && tx <= 23 && ty >= -9 && ty <= 28) {
                    // start intake and drive to pollen
                    intake.startIntake();
                    driveToPollen(tx, ty);
                }
            }
            else {
                // look for pollen
                telemetry.clearAll();
                telemetry.addData("looking for pollen", "");
                // Heading error of 80 turns clockwise(?); Distance error of -25 makes speed 0
                driveToPollen(80, -25);
            }

        }
    }

    public void driveToPollen(double headingError, double distanceError) {
        double KpSteer = 1;
        double KpSpeed = 1;

        double steeringAdjust = 0.0;
        double speedAdjust = 0.0;

        // Calculate rotation
        steeringAdjust = KpSteer * headingError / 360;

        // Calculate speed
        speedAdjust = KpSpeed * ((distanceError / 50) + 0.5);  // this formula needs to zero when
        // the pollen gets sucked in

        telemetry.addData("Heading error", headingError);
        telemetry.addData("Distance error", distanceError);

        telemetry.addData("Steering Adjust", steeringAdjust);
        telemetry.addData("Speed Adjust", speedAdjust);

        intake.startIntake();
        drive.drive(speedAdjust, 0.0, steeringAdjust);

    }
}