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

import org.firstinspires.ftc.teamcode.mechanisms.FlowerPlower;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.PositionalAim;
import org.firstinspires.ftc.teamcode.mechanisms.Turret;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class StaticBlueAuto extends OpMode {
    Turret turret = new Turret();
    PositionalAim autoAim = new PositionalAim();
    Intake intake = new Intake();
    FlowerPlower plower = new FlowerPlower();
    public static Pose lastPosition = null;
    private Commands c = new Commands();
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();
    private final Pose start = poseFactory.of(85, 133.5, 270);
    private final Pose Flower1 = poseFactory.of(120, 95, 0);
    private final Pose F1Clear = poseFactory.of(131.25, 95, 0);
    private final Pose shoot = poseFactory.of(85, 14, 90);
    private final Pose Flower2 = poseFactory.of(95, 25, 270);
    private final Pose F2Clear = poseFactory.of(95, 13.75, 270);
    private final Pose park = poseFactory.of(130, 20, 180);
    private final Pose controlPoint1 = poseFactory.of(125, 50, 45);
    private final Pose controlPoint2 = poseFactory.of(90, 40, 135);
    public Path startToFlower() {return line(start, Flower1).linear(start, Flower1);}
    public Path clearFlower1() {return line(Flower1, F1Clear).linear(Flower1, F1Clear);}
    public Path disengageFlower1() {return line(F1Clear, Flower1).linear(F1Clear, Flower1);}
    public Path flower1ToShoot() {return curve(Flower1, controlPoint1, shoot).linear(Flower1, shoot);}
    public Path shootToFlower2() {return line(shoot, Flower2).linear(shoot, Flower2);}
    public Path clearFlower2() {return line(Flower2, F2Clear).linear(Flower2, F2Clear);}
    public Path disengageFlower2() {return line(F2Clear, Flower2).linear(F2Clear, Flower2);}
    public Path Flower2ToShoot() {return line(Flower2, shoot).linear(Flower2, shoot);}
    public Path park() {return curve(shoot, controlPoint2, park).linear(shoot, park);}
    private Command autoRoutine() {
        return c.shoot(this, getRuntime() + 3, intake, turret, autoAim)
                .then(
                        follow(follower, startToFlower())
                        .with(c.flowerPlowerDown(plower))
                )
                .then(
                        follow(follower, clearFlower1())
                        .with(c.runIntake(hardwareMap, this, getRuntime() + 8, intake))
                )
                .then(follow(follower, disengageFlower1()))
                .then(
                        follow(follower, flower1ToShoot())
                        .with(c.flowerPlowerUp(plower))
                )
                .then(c.shoot(this, getRuntime() + 15, intake, turret, autoAim))
                .then(
                        follow(follower, shootToFlower2())
                        .with(c.flowerPlowerDown(plower))
                )
                .then(
                        follow(follower, clearFlower2())
                        .with(c.runIntake(hardwareMap, this, getRuntime() + 19, intake))
                )
                .then(follow(follower, disengageFlower2()))
                .then(
                        follow(follower, Flower2ToShoot())
                        .with(c.flowerPlowerUp(plower))
                )
                .then(c.shoot(this, getRuntime() + 24, intake, turret, autoAim))
                .then(follow(follower, park()));
    }

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        turret.init(hardwareMap);
        autoAim.init(hardwareMap, this, false, false, follower, turret);
        intake.init(hardwareMap);
        plower.init(hardwareMap);

        intake.gateClosed();
        plower.plowUp();
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

        autoAim.loop();

        lastPosition = follower.pose();
    }
}
