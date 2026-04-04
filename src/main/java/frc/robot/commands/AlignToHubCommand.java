package frc.robot.commands;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

public class AlignToHubCommand extends Command{
    private final DriveSubsystem m_drive;
    private final String limelightName;

    private final double xSpeed;
    private final double ySpeed;

    private final ProfiledPIDController profiledPIDController = 
        new ProfiledPIDController(1, 0, 0, 
            new TrapezoidProfile.Constraints(Math.PI * 2, Math.PI * 2));

    private final Translation2d hubCenter = new Translation2d(4.625, 4.05);

    public AlignToHubCommand(DriveSubsystem m_drive, String limelightName, double xSpeed, double ySpeed){
        this.m_drive = m_drive;
        this.limelightName = limelightName;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;       
        
        addRequirements(m_drive);
    }

    @Override
    public void initialize() {
        profiledPIDController.reset(m_drive.getPose().getRotation().getRadians());
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean isInteruppted){

    }


}
