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

public void initialize() {
    Shooting.setShooterRPM(speed);
    System.out.println("Shooter Initialized");

}

public void execute() {

}

public void end() {

}

public boolean isFinished() {
    return false;
}

}
