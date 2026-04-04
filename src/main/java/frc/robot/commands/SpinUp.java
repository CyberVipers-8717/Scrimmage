package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class SpinUp extends Command {
    private final ShooterSubsystem Shooting;

    private double speed;

    public SpinUp(ShooterSubsystem Shooting, double speed) {
        addRequirements(Shooting);
        this.Shooting = Shooting;
        this.speed = speed;
    }

@Override
public void initialize() {
    Shooting.setShooterRPM(speed);
    /*New Line*/Shooting.setQueuerVoltage(0);
    System.out.println("Shooter Initialized");

}

@Override
public void execute() {

}

@Override
public void end(boolean interrupted) {

}

@Override
public boolean isFinished() {
    return false;
}

}
