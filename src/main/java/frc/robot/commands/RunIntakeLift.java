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
        //m_intake.setLiftMotor(0);
        m_intake.stopLift();
    }

    @Override
    public boolean isFinished() {
        if (speed < 0 && m_intake.getLiftPosition() <= 1 ) { //going up and hit top
            System.out.println("********Command end condition 1********");
            return true;     
        } else if (speed > 0 && m_intake.getLiftPosition() >= 10) { //going down and hit bottom
            System.out.println("********Command end condition 2********");
            return true; 
        } else {
            return false; 
        } 
    }

}
