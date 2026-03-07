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

    @Override
    public void initialize() {
        launcher.setQueuerPower(speed);
        launcher.setShooterRPM(speed);
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {
        launcher.stop();
    }

    @Override
    public boolean isFinished(){
        return false;
    }

}
