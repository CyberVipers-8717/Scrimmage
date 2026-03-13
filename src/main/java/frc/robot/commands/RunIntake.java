
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class RunIntake extends Command {
    private final IntakeSubsystem m_intake;
    private double volts;
    

    //variable speed
    public RunIntake(IntakeSubsystem IntakeSubsystem, double volts) {
        m_intake = IntakeSubsystem;
        this.volts = volts;
        addRequirements(m_intake);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        m_intake.setIntakePower(volts);
    }

    @Override
    //turns off motor when command ends
    public void end(boolean interrupted) {
        m_intake.setIntakePower(0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}