package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.TurretAutoAlignAprilTag;

@TeleOp
public class TurretAutoAlignOpMode extends OpMode {

    private Limelight3A limelight;
    private TurretAutoAlignAprilTag turret = new TurretAutoAlignAprilTag();

    //----------------------- controller based PD tuning ----------

    double[] stepSizes = {0.1, 0.01, 0.001, 0.0001, 0.00001};

    // Index to select the current step size from the array
    int stepIndex = 1;


    @Override
    public void init() {

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(9);        // use pipeline for April Tag

        turret.init(hardwareMap);

        telemetry.addLine("Initialized all mechanisms");
    }

    @Override
    public void start() {
        limelight.start();
        turret.resetTimer();
    }

    @Override
    public void loop() {
        // vision logic
        // aprilTagWebcam.update();
        // AprilTagDetection id20 = aprilTagWebcam.getTagBySpecificationId(20);

        // get latest limelight result
        LLResult llResult = limelight.getLatestResult();

        // can april tag number be passed into limelight

        turret.update(??);

        // ------------ update P and D on the fly --------------
        // 'B' button cycles through the different step sizes for tuning precision
        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;  // Modulo wraps
        }
        // D-pad left/right to adjust+ P gain
        if (gamepad1.dpadLeftWasPressed()) {
            turret.setkP(turret.getkP() - stepSizes[stepIndex]);
        }
        if (gamepad1.dpadRightWasPressed()) {
            turret.setkP(turret.getkP() + stepSizes[stepIndex]);
        }

        // D-pad up/down adjust D gain
        if (gamepad1.dpadUpWasPressed()) {
            turret.setkD(turret.getkD() + stepSizes[stepIndex]);
        }
        if (gamepad1.dpadDownWasPressed()) {
            turret.setkD(turret.getkD() - stepSizes[stepIndex]);
        }


        if (id20 != null) {
            // telemetry.addData("cur ID", aprilTagWebcam);

        }
        else {
            telemetry.addLine("No Tag Detected. Stopping Turret Motor");
        }
        telemetry.addLine("--------------------");
        telemetry.addData("Tuning P: ", "%.4f (Dpad L/R)", turret.getkP());
        telemetry.addData("Tuning D: ", "%.4f (Dpad U/D)", turret.getkD());
        telemetry.addData("Step Size: ", "%.4f (B button)", stepSizes[stepIndex]);



    }


}
