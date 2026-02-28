package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class Launch extends Command {
    private final ShooterSubsystem launcher;
    private double speed;

    public Launch(ShooterSubsystem launcher, double speed) {
        addRequirements(launcher);
        this.launcher = launcher;
        this.speed = speed;
    }

    public void initialize() {
        launcher.setQueuerRPM(speed);
        launcher.setShooterRPM(speed);

    }

    public void execute() {

    }

    public void end() {

    }

    public boolean isFinished(){
        return false;
    }

}
