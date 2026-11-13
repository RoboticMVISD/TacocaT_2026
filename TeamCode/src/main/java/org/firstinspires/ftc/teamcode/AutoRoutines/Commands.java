package org.firstinspires.ftc.teamcode.AutoRoutines;

import static com.pedropathing.ivy.commands.Commands.waitUntil;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.mechanisms.FlowerPlower;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.PositionalAim;
import org.firstinspires.ftc.teamcode.mechanisms.Turret;
import org.opencv.core.Mat;

public class Commands {
    public Command flowerPlowerDown(FlowerPlower plower) {
        return Command.build()
                .setStart(() -> {
                    plower.plowDown();
                })
                .setDone(() ->
                        plower.FlowerPlower.getPosition() >= 0
                )
                .requiring(plower.FlowerPlower);
    }
    public Command flowerPlowerUp(FlowerPlower plower) {
        return Command.build()
                .setStart(() -> {
                    plower.plowUp();
                })
                .setDone(() ->
                        plower.FlowerPlower.getPosition() <= 0
                )
                .requiring(plower.FlowerPlower);
    }
    public Command runIntake(HardwareMap hwMap, OpMode OP, double endTime, Intake intake) {
        return Command.build()
                .setStart(() -> {
                    intake.startBothIntake();
                })
                .setDone(() ->
                        OP.getRuntime() >= endTime
                )
                .setEnd(endCondition -> {
                    intake.stopIntake();
                })
                .requiring(intake.Intake, intake.Intake2);
    }
    public Command autoAim(OpMode OP, double endTime, PositionalAim autoAim, Turret turret) {
        return Command.build()
                .setStart(() -> {
                    autoAim.aimEnabled = true;
                })
                .setExecute(() -> {
                    if (OP.getRuntime() >= endTime - 2) {
                        autoAim.aimEnabled = false;

                        autoAim.resetTurret();
                    }
                })
                .setDone(() -> OP.getRuntime() >= endTime)
                .setEnd(endCondition -> {
                    autoAim.aimEnabled = false;
                })
                .requiring(turret.rotator1, turret.rotator2);
    }
    public Command shoot( OpMode OP, double endTime, Intake intake, Turret turret, PositionalAim autoAim) {
        return Command.build()
                .setStart(() -> {
                    autoAim.launchEnabled = true;
                    intake.gateClosed();
                })
                .setExecute(() -> {
                    autoAim.launchEnabled = true;
                    autoAim.loop();

                    double currentVelocity = turret.shooter1.getVelocity();
                    double targetVelocity = autoAim.launchVelocity;

                    if (OP.getRuntime() >= endTime - 2) {
                        intake.gateOpen();
                        intake.startBothIntake();
                    }
                })
                .setDone(() -> OP.getRuntime() >= endTime)
                .setEnd(endCondition -> {
                    autoAim.launchEnabled = false;
                    autoAim.loop();
                    intake.stopIntake();
                    intake.gateClosed();
                })
                .requiring(intake.Intake, intake.Intake2, turret.shooter1, turret.shooter2, intake.IntakeGate);
    }
    public Command waitUntilOnOtherSide(Follower follower, double thresholdY) {
        return waitUntil(() -> Math.abs(follower.pose().y() - thresholdY) <= 2);
    }
}
