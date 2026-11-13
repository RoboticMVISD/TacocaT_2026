package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.AutoRoutines.StaticBlueAuto;
import org.firstinspires.ftc.teamcode.AutoRoutines.StaticRedAuto;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class PositionalAim {
    static OpMode op;
    static Turret turret;
    public static Follower follower;
    public Pose robotPose;
    private double botX;
    private double botY;
    private double botAngle;
    private double turretAngle;
    private double botBearingToGoal;
    private double bearingToGoal;
    private double elevationToGoal;
    public double rangeToGoal;
    private double lateralRange;
    private double xToGoal;
    private double yToGoal;
    private double heightToGoal;
    private double rotateSpeed;
    public double launchVelocity;
    private double currentGoalX;
    private double currentGoalY;
    private static final PoseFactory poseFactory = PoseFactory.degrees();
    private static Pose start;
    private static boolean red;
    private static final Pose redStart = poseFactory.of(14.5, 122, 0);
    private static final Pose blueStart = poseFactory.of(130, 20, 180);
    private final double goalHeight = 60;
    private final double blueAudienceX = 84.5, blueAudienceY = 55;
    private final double blueGoalX = 84.5, blueGoalY = 85;
    private final double redAudienceX = 57.5, redAudienceY = 55;
    private final double redGoalX = 57.5, redGoalY = 85;
    private final double turretHeight = 8.9;
    public static boolean aimEnabled = false;
    public boolean launchEnabled = false;
    private static boolean inTeleop;
    double kP = 0.00001;
    double error;
    double lastError;
    double kD = 0.00001;
    double curTime = 0;
    double lastTime = 0;
    final double aimBoundary = 1;
    final double launchMultiplier = 1.00538;
    final double launchOffset = 948.03381;
    double[] stepSizes = {0.1, 0.01, 0.001, 0.0001, 0.00001};
    int stepIndex = 0;

    public static void init(HardwareMap hardwareMap, OpMode OP, boolean InTeleop, boolean Red, Follower Follower, Turret Turret) {
        op = OP;
        inTeleop = InTeleop;
        turret = Turret;
        if (Red) {
            start = redStart;
            red = true;
        } else {
            start = blueStart;
            red = false;
        }

        if (inTeleop) {
            follower = Constants.create(hardwareMap);
            if (StaticBlueAuto.lastPosition != null || StaticRedAuto.lastPosition != null) {
                if (red) {
                    follower.setPose(StaticRedAuto.lastPosition);
                } else {
                    follower.setPose(StaticBlueAuto.lastPosition);
                }
            } else {
                follower.setPose(start);
            }
            follower.update();
        } else {
            follower = Follower;
            follower.setPose(Follower.pose());
            follower.update();
        }
    }

    public void loop() {
        follower.update();
        robotPose = follower.pose();

        if (robotPose.y() < 70) {
            if (red) {
                currentGoalX = redAudienceX;
                currentGoalY = redAudienceY;
            } else {
                currentGoalX = blueAudienceX;
                currentGoalY = blueAudienceY;
            }
        } else {
            if (red) {
                currentGoalX = redGoalX;
                currentGoalY = redGoalY;
            } else {
                currentGoalX = blueGoalX;
                currentGoalY = blueGoalY;
            }
        }

        botX = robotPose.x();
        botY = robotPose.y();
        botAngle = -robotPose.heading() + 90;
        turretAngle = turret.totalTurretDegrees;

        xToGoal = Math.abs(currentGoalX - botX);
        yToGoal = Math.abs(currentGoalY - botY);
        heightToGoal = Math.abs(goalHeight - turretHeight);

        lateralRange = Math.sqrt(Math.pow(yToGoal, 2) + Math.pow(xToGoal, 2));
        rangeToGoal = Math.sqrt(Math.pow(lateralRange, 2) + Math.pow(heightToGoal, 2));

        botBearingToGoal = Math.asin(xToGoal / lateralRange);
        if (botX < currentGoalX) {
            if (!red) {
                botBearingToGoal = -botBearingToGoal;
            }
        } else {
            if (red) {
                botBearingToGoal = -botBearingToGoal;
            }
        }
        bearingToGoal = botBearingToGoal + turretAngle + botAngle;
        elevationToGoal = Math.asin(heightToGoal / rangeToGoal);

        if (aimEnabled) {
            if (bearingToGoal < aimBoundary) {
                rotateSpeed = 0;
                turret.setTurretRotate(rotateSpeed);

                lastError = 0;
                lastTime = op.getRuntime();
            } else {
                autoRotate();
            }
        } else {
            if (!inTeleop) {
                turret.stopTurretRotate();
            }

            lastError = 0;
            lastTime = op.getRuntime();
        }
        if (launchEnabled) {
            autoVelocity();
        } else if (!inTeleop) {
            turret.stopShooter();
        }
    }

    public void autoRotate() {
        double pTerm = bearingToGoal * kP;

        curTime = op.getRuntime();
        double dT = curTime - lastTime;
        double dTerm = ((error - lastError) / dT) * kD;

        rotateSpeed = Range.clip(pTerm + dTerm, -1, 1);

        lastError = error;
        lastTime = curTime;

        turret.setTurretRotate(rotateSpeed);
    }

    public void autoVelocity() {
        launchVelocity = launchOffset * Math.pow(launchMultiplier, rangeToGoal);

        turret.setLauncherVelocity(launchVelocity);
    }

    public void tuner() {
        if (op.gamepad1.bWasPressed()) {
            kP += stepSizes[stepIndex];
        } else if (op.gamepad1.aWasPressed()) {
            kP -= stepSizes[stepIndex];
        } else if (op.gamepad1.yWasPressed()) {
            kD += stepSizes[stepIndex];
        } else if (op.gamepad1.xWasPressed()) {
            kD -= stepSizes[stepIndex];
        } else if (op.gamepad1.dpadRightWasPressed()) {
            if (stepIndex < stepSizes.length - 1){
                stepIndex = stepIndex + 1;
            }
        } else if (op.gamepad1.dpadLeftWasPressed()) {
            if (stepIndex > 0) {
                stepIndex = stepIndex - 1;
            }
        }

        op.telemetry.addData("kD", kD);
        op.telemetry.addData("kP", kP);
        op.telemetry.addData("Step Size", stepSizes[stepIndex]);
    }

    public void resetTurret() {
        robotPose = follower.pose();
        botAngle = -robotPose.heading() + 90;
        turretAngle = turret.totalTurretDegrees;

        botBearingToGoal = 0;
        bearingToGoal = botBearingToGoal + turretAngle + botAngle;

        if (bearingToGoal < 0) {
            rotateSpeed = 0;
            turret.setTurretRotate(rotateSpeed);

            lastError = 0;
            lastTime = op.getRuntime();
        } else {
            autoRotate();
        }
    }
}
