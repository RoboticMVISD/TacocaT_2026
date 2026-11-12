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
    Intake intake = new Intake();
    FlowerPlower plower = new FlowerPlower();
    PositionalAim autoAim = new PositionalAim();
    Turret turret = new Turret();
    public Command flowerPlowerDown(HardwareMap hwMap) {
        return Command.build()
                .setStart(() -> {
                    plower.init(hwMap);
                })
                .setExecute(() -> {
                    plower.plowDown();
                })
                .setDone(() ->
                        1 == plower.FlowerPlower.getPosition()
                )
                .requiring(plower.FlowerPlower);
    }
    public Command flowerPlowerUp(HardwareMap hwMap) {
        return Command.build()
                .setStart(() -> {
                    plower.init(hwMap);
                    plower.plowDown();
                })
                .setExecute(() -> {
                    plower.plowUp();
                })
                .setDone(() ->
                        plower.FlowerPlower.getPosition() == 0
                )
                .requiring(plower.FlowerPlower);
    }
    public Command runIntake(HardwareMap hwMap, OpMode OP, double endTime) {
        return Command.build()
                .setStart(() -> {
                    intake.init(hwMap);
                })
                .setExecute(() -> {
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
    public Command autoAim(HardwareMap hwMap, OpMode OP, boolean red, Follower Follower, double endTime) {
        return Command.build()
                .setStart(() -> {
                    turret.init(hwMap);
                    autoAim.init(hwMap, OP, false, red, Follower, turret);
                    autoAim.aimEnabled = true;
                })
                .setExecute(() -> {
                    turret.loop();
                    if (OP.getRuntime() >= endTime - 2) {
                        autoAim.aimEnabled = false;

                        autoAim.resetTurret();
                    } else {
                        autoAim.loop();
                    }
                })
                .setDone(() -> OP.getRuntime() >= endTime)
                .setEnd(endCondition -> {
                    autoAim.aimEnabled = false;
                    autoAim.loop();
                });
    }
    public Command shoot(HardwareMap hwMap, OpMode OP, boolean red, Follower Follower, double endTime) {
        return Command.build()
                .setStart(() -> {
                    intake.init(hwMap);
                    turret.init(hwMap);
                    autoAim.init(hwMap, OP, false, red, Follower, turret);
                    autoAim.launchEnabled = true;
                })
                .setExecute(() -> {
                    double currentVelocity = turret.shooter1.getVelocity();
                    double targetVelocity = autoAim.launchVelocity;

                    autoAim.loop();

                    if (Math.abs(targetVelocity - currentVelocity) < 10) {
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
                });
    }
    public Command waitUntilOnOtherSide(Follower follower, double thresholdY) {
        return waitUntil(() -> Math.abs(follower.pose().y() - thresholdY) <= 2);
    }
    public Command fixTurret(HardwareMap hwMap) {
        return Command.build()
                .setStart(() -> {
                    turret.init(hwMap);
                });
    }
}
