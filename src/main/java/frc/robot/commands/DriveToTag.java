package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Limelight.LimelightHelpers;

/*
import frc.robot.Constants.DriveConstants;
import frc.robot.RobotContainer;
import frc.robot.TagAlignment;
import frc.robot.commands.AlignToTagCommand;
*/

public class DriveToTag extends Command{
    private final DriveSubsystem drive;
    private final String limelightName;
    //private final PIDController xController;
    //private final PIDController yController;
    //private final PIDController rotController;
    private final double kPr = 0.035;
    private final double kPd = 0.01;
    private static final double kMaxSpeed = 1.0; // 3 meters per second
    //private static final double kMaxAngularSpeed = Math.PI; // 1/2 rotation per second
    private static final double targetArea = 18.3;

    // Slew rate limiters to make joystick inputs more gentle; 1/3 sec from 0 to 1.
    private final SlewRateLimiter m_xspeedLimiter = new SlewRateLimiter(3);
    private final SlewRateLimiter m_yspeedLimiter = new SlewRateLimiter(3);
    private final SlewRateLimiter m_rotLimiter = new SlewRateLimiter(3);

    public DriveToTag (DriveSubsystem drive, String limelightName){
        this.drive = drive;
        this.limelightName = limelightName;
        addRequirements(drive);
        System.out.println("***DriveToTag Command Constructor Called***");
    }

    public void initialize(){
        LimelightHelpers.setLEDMode_ForceOn(limelightName);
        System.out.println("***DriveToTag Command Initialized***");
    }

    public void execute(boolean fieldRelative){
        System.out.println("***DriveToTag Command Executed***");
        /*
        //get x speed, 
        var xSpeed = -m_xspeedLimiter.calculate(
            MathUtil.applyDeadband(RobotContainer.m_driverController.getLeftY(), 0.02)) 
            * kMaxSpeed;
        //get y speed
        var ySpeed = -m_yspeedLimiter.calculate(
            MathUtil.applyDeadband(RobotContainer.m_driverController.getLeftX(), 0.02)) 
            * kMaxSpeed;
        //get rotation 
        var rot = -m_rotLimiter.calculate(
            MathUtil.applyDeadband(RobotContainer.m_driverController.getRightX(), 0.02)) 
            * kMaxAngularSpeed;

        if(RobotContainer.m_driverController.getAButton()){
            rot = TagAlignment.aimAtTag(limelightName, kPr);
            //xSpeed = TagAlignment.driveToTag(limelightName, kPd);
            fieldRelative = false;
        }

        drive.drive(xSpeed, ySpeed, rot, fieldRelative);*/
        if (LimelightHelpers.getTV(limelightName)){
            
            double tx = LimelightHelpers.getTX(limelightName);
            double ta = LimelightHelpers.getTA(limelightName);

            double aimingError = -tx;
            double distanceError = targetArea - ta;
            double aimingAdjust = 0.0;
            double distanceAdjust = kPd * distanceError;

            if (tx > 2.0){
                aimingAdjust = (kPr * aimingError);
            }

            //distanceAdjust = -m_xspeedLimiter.calculate(Math.max(Math.min(distanceAdjust, kMaxSpeed), -kMaxSpeed));
            distanceAdjust = Math.max(Math.min(distanceAdjust, kMaxSpeed), -kMaxSpeed);
            drive.drive(distanceAdjust, 0, aimingAdjust, false);
        }
    }

    public boolean isFinished(){
        return LimelightHelpers.getTV(limelightName) && LimelightHelpers.getTA(limelightName) >= targetArea;
    }
    
    public void end (boolean interrupted){
        drive.drive(0, 0, 0, true);
        LimelightHelpers.setLEDMode_ForceOff(limelightName);
    }
}