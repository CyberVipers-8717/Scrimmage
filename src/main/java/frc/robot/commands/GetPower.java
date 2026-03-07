package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Limelight.LimelightHelpers;
import frc.robot.subsystems.ShooterSubsystem;

//Command (composition?) to align robot with apriltag, get distance from 
public class GetPower extends Command {
    private final ShooterSubsystem m_shooter;
    private final String limelightName;

    public GetPower(ShooterSubsystem m_shooter, String limelightName) {
        this.m_shooter = m_shooter;
        this.limelightName = limelightName;
        addRequirements(m_shooter);
        System.out.println("GetPower called");
    }

    public void initialize(){

    }

    public void exeute(){

    }

    public void end(){

    }

    public boolean isFinished(){
        return false;
    }

}

