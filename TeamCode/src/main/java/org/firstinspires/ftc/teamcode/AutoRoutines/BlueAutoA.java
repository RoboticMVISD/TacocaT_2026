package org.firstinspires.ftc.teamcode.AutoRoutines;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.mechanisms.FlowerIntake;
import org.firstinspires.ftc.teamcode.mechanisms.FlowerPlower;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class BlueAutoA extends OpMode {
    private Commands c = new Commands();
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();
    private final Pose start = poseFactory.of(85, 133.5, 270);
    private final Pose Flower1 = poseFactory.of(125, 95, 0);
    private final Pose F1Clear = poseFactory.of(130, 95, 0);
    private final Pose Flower2 = poseFactory.of(95, 15, 270);
    private final Pose F2Clear = poseFactory.of(95, 10, 270);
    private final Pose park = poseFactory.of(130, 20, 180);
    public Path startToFlower() {return line(start, Flower1).linear(start, Flower1);}
    public Path clearFlower1() {return line(Flower1, F1Clear).linear(Flower1, F1Clear);}
    public Path disengageFlower1() {return line(F1Clear, Flower1).linear(F1Clear, Flower1);}
    public Path flower1To2() {return line(Flower1, Flower2).linear(Flower1, Flower2);}
    public Path clearFlower2() {return line(Flower2, F2Clear).linear(Flower2, F2Clear);}
    public Path disengageFlower2() {return line(F2Clear, Flower2).linear(F2Clear, Flower2);}
    public Path park() {return line(Flower2, park).linear(Flower2, park);}
    private Command autoRoutine =
            c.autoAim(hardwareMap, this, false, follower, 29)
                    .with(
                            c.shoot(hardwareMap, this, false, follower, getRuntime() + 3)
                            .then(
                                    follow(follower, startToFlower())
                                    .with(c.flowerPlowerDown(hardwareMap))
                            )
                            .then(
                                    follow(follower, clearFlower1())
                                    .with(c.runIntake(hardwareMap, this, getRuntime() + 3))
                            )
                            .then(follow(follower, disengageFlower1()))
                            .then(
                                    c.flowerPlowerUp(hardwareMap)
                                    .with(follow(follower, flower1To2()))
                                    .with(
                                            c.waitUntilOnOtherSide(follower, 50)
                                            .then(c.shoot(hardwareMap, this, false, follower, getRuntime() + 3)))
                            )
                            .then(c.flowerPlowerDown(hardwareMap))
                            .then(
                                    follow(follower, clearFlower2())
                                    .with(c.runIntake(hardwareMap, this, getRuntime() + 3))
                                    .with(c.shoot(hardwareMap, this, true, follower, getRuntime() + 5))
                            )
                            .then(follow(follower, disengageFlower2()))
                            .then(
                                    c.flowerPlowerUp(hardwareMap)
                                    .with(follow(follower, park()))
                            )
                    ).then();

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(start);
    }

    @Override
    public void start() {
        schedule(autoRoutine);
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
    }
}
