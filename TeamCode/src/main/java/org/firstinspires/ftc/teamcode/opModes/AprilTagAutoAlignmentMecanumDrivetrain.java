package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;

@TeleOp
public class AprilTagAutoAlignmentMecanumDrivetrain extends OpMode {

    private Limelight3A limelight;
    private MecanumDrive drive = new MecanumDrive();

    //------------------- PD controller --------------------------

    double kP = 0.002;
    double error = 0;
    double lastError = 0;
    double goalX = 0;  // offset here
    double angleTolerance = 0.4;
    double kD = 0.0001;
    double curTime = 0;
    double lastTime = 0;

    // ----------------------- driving setup ---------------------

    double forward, strafe, rotate;

    //----------------------- controller based PD tuning ----------

    double[] stepSizes = {0.1, 0.01, 0.001, 0.0001};
    int stepIndex = 1;



    @Override
    public void init() {

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(9);        // use pipeline for April Tag

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

        //------------------ set mecanum drive inputs -------------
        forward = -gamepad1.left_stick_x;
        strafe = gamepad1.left_stick_y;
        rotate = gamepad1.right_stick_x;

        //---------------------- get april tag info ----------------

        // get latest limelight result
        LLResult llResult = limelight.getLatestResult();

        //----------------- auto align rotation logic --------------

        if (gamepad1.left_trigger > 0.3) {
            if (llResult != null && llResult.isValid()) {

                error = goalX - llResult.getTx();

                if (Math.abs(error) < angleTolerance) {
                    rotate = 0;
                } else {
                    double pTerm = error * kP;

                    curTime = getRuntime();
                    double dT = curTime - lastTime;
                    double dTerm = ((error - lastError) / dT) * kD;

                    rotate = Range.clip(pTerm + dTerm, -0.4, 0.4);

                    lastError = error;
                    lastTime = curTime;
                }
            }
            else {
                lastTime = getRuntime();
                lastError = 0;
            }
        }
        else {
            lastError = 0;
            lastTime = getRuntime();
        }

        // drive our motors!
        drive.drive(forward, strafe, rotate);

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

        // -------------------------------- telemetry ---------------------------
        if (llResult != null && llResult.isValid()) {
            if (gamepad1.left_trigger > 0.3) {
                telemetry.addLine("AUTO ALIGN");
            }
            // display Detection telemetry
            telemetry.addData("Error", error);
        } else {
            telemetry.addLine("MANUAL ROTATE MODE");
        }
        telemetry.addLine("--------------------");
        telemetry.addData("Tuning P: ", "%.4f (Dpad L/R)", kP);
        telemetry.addData("Tuning D: ", "%.4f (Dpad U/D)", kD);
        telemetry.addData("Step Size: ", "%.4f (B button)", stepSizes[stepIndex]);
    }
}

