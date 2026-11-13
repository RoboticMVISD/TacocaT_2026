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
public class StaticRedAuto extends OpMode {
    Turret turret = new Turret();
    PositionalAim autoAim = new PositionalAim();
    Intake intake = new Intake();
    FlowerPlower plower = new FlowerPlower();
    public static Pose lastPosition = null;
    private Commands c = new Commands();
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();
    private final Pose start = poseFactory.of(57.5, 8, 90);
    private final Pose Flower1 = poseFactory.of(20, 47.5, 180);
    private final Pose F1Clear = poseFactory.of(12, 47.5, 180);
    private final Pose shoot = poseFactory.of(57.5, 126, 270);
    private final Pose Flower2 = poseFactory.of(47.5, 120, 90);
    private final Pose F2Clear = poseFactory.of(47.5, 127.5, 90);
    private final Pose park = poseFactory.of(14.5, 122, 0);
    private final Pose controlPoint1 = poseFactory.of(15, 90, 225);
    private final Pose controlPoint2 = poseFactory.of(50, 100, 315);
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
                                .with(c.runIntake(hardwareMap, this, getRuntime() + 7, intake))
                )
                .then(follow(follower, disengageFlower1()))
                .then(
                        follow(follower, flower1ToShoot())
                                .with(c.flowerPlowerUp(plower))
                )
                .then(c.shoot(this, getRuntime() + 14, intake, turret, autoAim))
                .then(
                        follow(follower, shootToFlower2())
                                .with(c.flowerPlowerDown(plower))
                )
                .then(
                        follow(follower, clearFlower2())
                                .with(c.runIntake(hardwareMap, this, getRuntime() + 17, intake))
                )
                .then(follow(follower, disengageFlower2()))
                .then(
                        follow(follower, Flower2ToShoot())
                                .with(c.flowerPlowerUp(plower))
                )
                .then(c.shoot(this, getRuntime() + 22, intake, turret, autoAim))
                .then(follow(follower, park()));
    }

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(start);

        turret.init(hardwareMap);
        autoAim.init(hardwareMap, this, false, true, follower, turret);
        intake.init(hardwareMap);
        plower.init(hardwareMap);
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
