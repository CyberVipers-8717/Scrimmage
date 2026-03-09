
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class RunIntake extends Command {
    private final IntakeSubsystem m_intake;
    private double speed;
    

    //variable speed
    public RunIntake(IntakeSubsystem IntakeSubsystem, double speed) {
        m_intake = IntakeSubsystem;
        this.speed = speed;
        addRequirements(m_intake);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        m_intake.setMotor(speed);
    }

    @Override
    //turns off motor when command ends
    public void end(boolean interrupted) {
        m_intake.setMotor(0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}