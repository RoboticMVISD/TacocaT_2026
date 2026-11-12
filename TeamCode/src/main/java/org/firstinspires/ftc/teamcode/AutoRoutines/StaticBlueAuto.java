package org.firstinspires.ftc.teamcode.AutoRoutines;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class StaticBlueAuto extends OpMode {
    private Commands c = new Commands();
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();
    private final Pose start = poseFactory.of(85, 133.5, 270);
    private final Pose Flower1 = poseFactory.of(120, 95, 0);
    private final Pose F1Clear = poseFactory.of(130, 95, 0);
    private final Pose shoot = poseFactory.of(85, 15, 90);
    private final Pose Flower2 = poseFactory.of(95, 20, 270);
    private final Pose F2Clear = poseFactory.of(95, 10, 270);
    private final Pose park = poseFactory.of(130, 20, 180);
    private final Pose controlPoint = poseFactory.of(125, 50, 45);
    public Path startToFlower() {return line(start, Flower1).linear(start, Flower1);}
    public Path clearFlower1() {return line(Flower1, F1Clear).linear(Flower1, F1Clear);}
    public Path disengageFlower1() {return line(F1Clear, Flower1).linear(F1Clear, Flower1);}
    public Path flower1ToShoot() {return curve(Flower1, controlPoint, shoot).linear(Flower1, shoot);}
    public Path shootToFlower2() {return line(shoot, Flower2).linear(shoot, Flower2);}
    public Path clearFlower2() {return line(Flower2, F2Clear).linear(Flower2, F2Clear);}
    public Path disengageFlower2() {return line(F2Clear, Flower2).linear(F2Clear, Flower2);}
    public Path Flower2ToShoot() {return line(Flower2, shoot).linear(Flower2, shoot);}
    public Path park() {return line(shoot, park).linear(shoot, park);}
    private Command autoRoutine =
            c.fixTurret(hardwareMap)
                    .with(
                            c.shoot(hardwareMap, this, false, follower, getRuntime() + 3)
                            .then(
                                    follow(follower, startToFlower())
                                    .with(c.flowerPlowerDown(hardwareMap))
                            )
                            .then(
                                    follow(follower, clearFlower1())
                                    .with(c.runIntake(hardwareMap, this, getRuntime() + 2.5))
                            )
                            .then(follow(follower, disengageFlower1()))
                            .then(
                                    follow(follower, flower1ToShoot())
                                    .with(c.flowerPlowerUp(hardwareMap))
                            )
                            .then(c.shoot(hardwareMap, this, false, follower, getRuntime() + 3))
                            .then(
                                    follow(follower, shootToFlower2())
                                    .with(c.flowerPlowerDown(hardwareMap))
                            )
                            .then(
                                    follow(follower, clearFlower2())
                                    .with(c.runIntake(hardwareMap, this, getRuntime() + 2.5))
                            )
                            .then(
                                    follow(follower, disengageFlower2())
                            )
                            .then(
                                    follow(follower, Flower2ToShoot())
                                    .with(c.flowerPlowerUp(hardwareMap))
                            )
                            .then(c.shoot(hardwareMap, this, false, follower, getRuntime() + 3))
                            .then(follow(follower, park()))
                    );

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
