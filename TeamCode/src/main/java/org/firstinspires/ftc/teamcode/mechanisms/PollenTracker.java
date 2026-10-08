package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.List;

public class PollenTracker {
    OpMode op;
    private Limelight3A limelight3A;

    private static double turnLastError;
    private static double forwardLastError;
    private static double currentTargetBearing;
    private static double currentTargetDistance;
    private static double currentTargetYOffset;
    private static double turnPower;
    private static double forwardPower;
    private static double TkP = 0.058;
    private static double TkD = 0.001;
    private static double FkP = 0.010;
    private static double FkD = 0.001;
    private static boolean noTargetDetected;
    public static boolean trackingEnabled;
    private static boolean isChangingTurn;
    private static final double maxTurnPower = 0.5,  aimBoundary = 3, maxForwardPower = 0.75, distanceBoundary = 20;
    private static final double[]stepSizes= {0.1, 0.01, 0.001, 0.0001, 0.00001};
    private static int stepIndex;

    private static final ElapsedTime turnTimer = new ElapsedTime();
    private static final ElapsedTime forwardTimer = new ElapsedTime();
    private static final ElapsedTime staleTimer = new ElapsedTime();

    public void init(HardwareMap hwMap, OpMode OP) {
        op = OP;

        limelight3A = hwMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(0);
    }

    public void start() {
        limelight3A.start();
    }

    public void loop() {
        //op.telemetry.addData("Stale Timer", staleTimer.milliseconds());

        LLResult llResult = limelight3A.getLatestResult();
        if (llResult != null & llResult.isValid()) {
            noTargetDetected = false;
            staleTimer.reset();

            op.telemetry.addData("Target X offset", llResult.getTx());
            op.telemetry.addData("Target Y offset", llResult.getTy());
            op.telemetry.addData("Target Area offset", llResult.getTa());

            List<LLResultTypes.ColorResult> colors = llResult.getColorResults();
            for (LLResultTypes.ColorResult color : colors) {
                currentTargetBearing = color.getTargetXDegrees();

                //currentTargetDistance = 119.56843 * (Math.pow(0.695262, llResult.getTa()));

                currentTargetYOffset = color.getTargetYDegrees();

                //op.telemetry.addData("Target Distance", currentTargetDistance);
                op.telemetry.addData("Target Color Area", color.getTargetArea());
            }
        } else {
            noTargetDetected = true;
        }
    }
    /*
    public static void update() {
        if (trackingEnabled) {
            turnTracking();
            //forwardTracking();

            if (noTargetDetected) {

            } else {

            }
        } else {
            turnLastError = 0;
            forwardLastError = 0;
        }
    }

    public static void turnTracking() {
        double deltaTime = turnTimer.seconds();
        turnTimer.reset();

        if (!trackingEnabled) {
            turnLastError = 0;

            return;
        }

        if (noTargetDetected) {
            turnLastError = 0;

            return;
        }

        double error = currentTargetBearing;
        double pTerm = error * TkP;

        double dTerm = 0;
        if (deltaTime > 0) {
            dTerm = ((error - turnLastError) / deltaTime) * TkD;
        }

        if (Math.abs(error) < aimBoundary) {
            turnPower = 0;
        } else {
            turnPower = Range.clip(pTerm + dTerm, -maxTurnPower, maxTurnPower);
        }

        turnLastError = error;
    }
    */
}
