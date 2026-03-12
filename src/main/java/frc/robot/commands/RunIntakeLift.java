package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class RunIntakeLift extends Command {
    private final IntakeSubsystem m_intake;
    private double speed;

    //variable speed
    public RunIntakeLift(IntakeSubsystem intakeSubsystem, double speed){
        m_intake = intakeSubsystem;
        this.speed = speed;
        addRequirements(m_intake);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        m_intake.setLiftMotor(speed);
    }

    @Override
    public void end(boolean interrupted) {
        m_intake.setLiftMotor(0);
    }

    @Override
    public boolean isFinished() {
        if (speed < 0 && m_intake.getLiftPosition() <= 0 ) { //going down and hit bottom
            return true;     
        } else if (speed > 0 && m_intake.getLiftPosition() >= 100) { //going up and hit top
            return true; 
        } else {
            return false; 
        } 
    }

}
