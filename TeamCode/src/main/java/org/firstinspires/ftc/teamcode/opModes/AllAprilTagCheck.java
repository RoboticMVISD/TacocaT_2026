package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;

@TeleOp
public class AllAprilTagCheck extends OpMode {

    private Limelight3A limelight;
    private MecanumDrive drive = new MecanumDrive();

    //------------------- PD controller --------------------------

    double kP = 0.02;
    double error = 0;
    double lastError = 0;
    double goalX = 0;  // offset here
    double angleTolerance = 0.4;
    double kD = 0.0001;
    double curTime = 0;
    double lastTime = 0;
    String pipelineLabel = "Pipeline 9 April Tag Blue Audience";
    String AprilTagLabel = "42";

    // ----------------------- driving setup ---------------------

    double forward, strafe, rotate;

    //----------------------- controller based PD tuning ----------

    // double[] stepSizes = {0.1, 0.01, 0.001, 0.0001};
    // int stepIndex = 1;

    @Override
    public void init() {

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(9);        // start by using pipeline 9 for April Tag

        drive.init(hardwareMap);

        telemetry.addLine("Initialized");

    }

    @Override
    public void start() {

        limelight.start();

        resetRuntime();
        curTime = getRuntime();
    }


    @Override
    public void loop() {

        // NOTE: I cannot completely cut power to the camera, but I could make it basically turn off
        // Using VisionPortal: Call myVisionPortal.close(); at the end of the autonomous mode.
        // According to FTC docs:  this stops all background vision processing and closes USB communication with the camera
        // (which typically turns off the camera's active indicator LED and drops its power draw to near-idle).
        
        //------------------ set mecanum drive inputs -------------
        forward = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        //---------------------- get april tag info ----------------

        // get latest limelight result

        LLResult llResult = limelight.getLatestResult();

        // D-pad selections Limelight pipelines 6 - 9

        if (gamepad1.dpadUpWasPressed()) {
            limelight.pipelineSwitch(9);        // pipeline 9 for Blue Audience April Tag 42
            pipelineLabel = "Pipeline 9 April Tag Blue Audience";
            AprilTagLabel = "42";
        }
        if (gamepad1.dpadLeftWasPressed()) {
            limelight.pipelineSwitch(8);        // pipeline 8 for Red Audience April Tag 33
            pipelineLabel = "Pipeline 8 April Tag Red Score";
            AprilTagLabel = "33";
        }
        if (gamepad1.dpadRightWasPressed()) {
            limelight.pipelineSwitch(7);        // pipeline 7 for Blue Score April Tag 38
            pipelineLabel = "Pipeline 7 April Tag Blue Score";
            AprilTagLabel = "38";
        }
        if (gamepad1.dpadDownWasPressed()) {
            limelight.pipelineSwitch(6);        // pipeline 6 for Red Score April Tag 37
            pipelineLabel = "Pipeline 6 April Tag Red Score";
            AprilTagLabel = "37";
        }

        telemetry.update();
        telemetry.addLine(pipelineLabel);
        telemetry.addLine("-------------------");

        if (llResult != null && llResult.isValid()) {
            telemetry.addLine("April tag seen: Yes");
            telemetry.addData("April tag: ", AprilTagLabel);
        } else {
            telemetry.addLine("April tag seen: No");
        }

        // drive our motors!
        drive.drive(forward, strafe, rotate);


        /*
        // update P and D on the fly
        // 'B' button cycles through the different step sizes for tuning precision
        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;  // Modulo wraps
        }
        // D-pad left/right to adjust+ P gain
        if (gamepad1.dpadLeftWasPressed()) {
            kP -= stepSizes[stepIndex];
        }
        if (gamepad1.dpadRightWasPressed()) {
            kP += stepSizes[stepIndex];
        }

        // D-pad up/down adjust D gain
        if (gamepad1.dpadUpWasPressed()) {
            kD += stepSizes[stepIndex];
        }
        if (gamepad1.dpadDownWasPressed()) {
            kD -= stepSizes[stepIndex];
        }
*/
        // telemetry.addLine("--------------------");

        // -------------------------------- telemetry ---------------------------

        // get latest limelight result

        /*
        LLResult llResult = limelight.getLatestResult();

        if (llResult != null && llResult.isValid()) {
            telemetry.addData("Target X", llResult.getTx());
            telemetry.addData("Target Y", llResult.getTy());
            telemetry.addData("Target Area", llResult.getTa());
            // display Detection telemetry
            telemetry.addData("Error", error);
        }
         */
        /*
        telemetry.addData("Tuning P: ", "%.4f (Dpad L/R)", kP);
        telemetry.addData("Tuning D: ", "%.4f (Dpad U/D)", kD);
        telemetry.addData("Step Size: ", "%.4f (B button)", stepSizes[stepIndex]);
*/

    }
}
