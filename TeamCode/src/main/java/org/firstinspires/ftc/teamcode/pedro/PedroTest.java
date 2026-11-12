package org.firstinspires.ftc.teamcode.pedro;

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

@Autonomous
public class PedroTest extends OpMode {
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(118, 12, 0);
    private final Pose startGoal1Start = poseFactory.of(118, 12, 0);
    private final Pose startGoal1 = poseFactory.of(90, 18, 180);
    private final Pose startGoal2 = poseFactory.of(60, 24, 360);
    private final Pose goalPark1Start = poseFactory.of(60, 24, 0);
    private final Pose goalPark1 = poseFactory.of(95, 29.5, 180);
    private final Pose goalPark2 = poseFactory.of(130, 35, 360);

    public Path startGoal1() {return line(startGoal1Start, startGoal1).linear(startGoal1Start, startGoal1);}

    public Path startGoal2() {
        return line(startGoal1, startGoal2).linear(startGoal1, startGoal2);
    }

    public Path goalPark1() {return line(goalPark1Start, goalPark1).linear(goalPark1Start, goalPark1);}

    public Path goalPark2() {
        return line(goalPark1, goalPark2).linear(goalPark1, goalPark2);
    }
    private Command autoRoutine() {
        return sequential(
                follow(follower, startGoal1()),
                follow(follower, startGoal2()),
                follow(follower, goalPark1()),
                follow(follower, goalPark2())
        );
    }


    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
    }
}
