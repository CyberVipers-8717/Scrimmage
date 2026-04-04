package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Limelight.LimelightHelpers;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Constants;
import frc.robot.Constants.DriveConstants;
import frc.robot.RobotContainer;

public class AimandRange extends Command {
    private final DriveSubsystem m_drive;
    private final String limelightName;

  // Slew rate limiters to make joystick inputs more gentle; 1/3 sec from 0 to 1.
  private final SlewRateLimiter m_xspeedLimiter = new SlewRateLimiter(3);
  private final SlewRateLimiter m_yspeedLimiter = new SlewRateLimiter(3);
  private final SlewRateLimiter m_rotLimiter = new SlewRateLimiter(3);

    public AimandRange(DriveSubsystem m_drive, String limelightName){
        this.m_drive = m_drive;
        this.limelightName = limelightName;

        addRequirements(m_drive);
    }

    @Override
    public void initialize(){

    }

    @Override
    public void execute(){
    // Get the x speed. We are inverting this because Xbox controllers return
    // negative values when we push forward.
    var xSpeed =
        -m_xspeedLimiter.calculate(MathUtil.applyDeadband(RobotContainer.m_driverController.getLeftY(), 0.02))
            * Constants.DriveConstants.kMaxSpeedMetersPerSecond;

    // Get the y speed or sideways/strafe speed. We are inverting this because
    // we want a positive value when we pull to the left. Xbox controllers
    // return positive values when you pull to the right by default.
    var ySpeed =
        m_yspeedLimiter.calculate(MathUtil.applyDeadband(RobotContainer.m_driverController.getLeftX(), 0.02))
            * Constants.DriveConstants.kMaxSpeedMetersPerSecond;

    // Get the rate of angular rotation. We are inverting this because we want a
    // positive value when we pull to the left (remember, CCW is positive in
    // mathematics). Xbox controllers return positive values when you pull to
    // the right by default.
    var rot =
    -m_rotLimiter.calculate(MathUtil.applyDeadband(RobotContainer.m_driverController.getRightX(), 0.02))
        * Constants.DriveConstants.kMaxSpeedMetersPerSecond;

    final var rot_limelight = limelight_aim_proportional();
    rot = rot_limelight;

    final var forward_limelight = limelight_range_proportional();
    xSpeed = forward_limelight;


    m_drive.drive(xSpeed, ySpeed, rot, false);
    
}

    @Override
    public boolean isFinished(){
        return false;
    }

    @Override
    public void end(boolean isInteruppted){
        
    }

  // simple proportional turning control with Limelight.
  // "proportional control" is a control algorithm in which the output is proportional to the error.
  // in this case, we are going to return an angular velocity that is proportional to the 
  // "tx" value from the Limelight.
  double limelight_aim_proportional()
  {    
    // kP (constant of proportionality)
    // this is a hand-tuned number that determines the aggressiveness of our proportional control loop
    // if it is too high, the robot will oscillate.
    // if it is too low, the robot will never reach its target
    // if the robot never turns in the correct direction, kP should be inverted.
    double kP = .035;

    // tx ranges from (-hfov/2) to (hfov/2) in degrees. If your target is on the rightmost edge of 
    // your limelight 3 feed, tx should return roughly 31 degrees.
    double targetingAngularVelocity = LimelightHelpers.getTX("limelight") * kP;

    // convert to radians per second for our drive method
    targetingAngularVelocity *= Constants.DriveConstants.kMaxAngularSpeed;

    //invert since tx is positive when the target is to the right of the crosshair
    targetingAngularVelocity *= -1.0;

    return targetingAngularVelocity;
  }

  // simple proportional ranging control with Limelight's "ty" value
  // this works best if your Limelight's mount height and target mount height are different.
  // if your limelight and target are mounted at the same or similar heights, use "ta" (area) for target ranging rather than "ty"
  double limelight_range_proportional()
  {    
    double kP = .1;
    double targetingForwardSpeed = LimelightHelpers.getTY("limelight") * kP;
    targetingForwardSpeed *= Constants.DriveConstants.kMaxSpeedMetersPerSecond;
    targetingForwardSpeed *= -1.0;
    return targetingForwardSpeed;
  }

  /*private void drive(boolean fieldRelative) {
    // Get the x speed. We are inverting this because Xbox controllers return
    // negative values when we push forward.
    var xSpeed =
        -m_xspeedLimiter.calculate(MathUtil.applyDeadband(RobotContainer.m_driverController.getLeftY(), 0.02))
            * Constants.DriveConstants.kMaxSpeedMetersPerSecond;

    // Get the y speed or sideways/strafe speed. We are inverting this because
    // we want a positive value when we pull to the left. Xbox controllers
    // return positive values when you pull to the right by default.
    var ySpeed =
        m_yspeedLimiter.calculate(MathUtil.applyDeadband(RobotContainer.m_driverController.getLeftX(), 0.02))
            * Constants.DriveConstants.kMaxSpeedMetersPerSecond;

    // Get the rate of angular rotation. We are inverting this because we want a
    // positive value when we pull to the left (remember, CCW is positive in
    // mathematics). Xbox controllers return positive values when you pull to
    // the right by default.
    var rot =
        -m_rotLimiter.calculate(MathUtil.applyDeadband(RobotContainer.m_driverController.getRightX(), 0.02))
            * Constants.DriveConstants.kMaxSpeedMetersPerSecond;

    // while the A-button is pressed, overwrite some of the driving values with the output of our limelight methods
    if(RobotContainer.m_driverController.getAButton())
    {
        final var rot_limelight = limelight_aim_proportional();
        rot = rot_limelight;

        final var forward_limelight = limelight_range_proportional();
        xSpeed = forward_limelight;

        //while using Limelight, turn off field-relative driving.
        fieldRelative = false;
    }

    m_drive.drive(xSpeed, ySpeed, rot, fieldRelative);
  }*/
}
