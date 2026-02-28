package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ShooterSubsystem;

public class ShootFuel extends SequentialCommandGroup {

    public ShootFuel(ShooterSubsystem Fuel, double speed) {

        addCommands(
            new SpinUp(Fuel, speed).withTimeout(0.5),
            new Launch(Fuel, speed));
    }

}
