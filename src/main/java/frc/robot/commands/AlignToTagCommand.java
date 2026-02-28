package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.TagAlignment;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.LimelightHelpers;

public class AlignToTagCommand extends Command {
    private final DriveSubsystem drive;
    private final String limelightName;
    private final double kP = 0.015;

public AlignToTagCommand(DriveSubsystem drive, String limelightName) {
    this.drive = drive;
    this.limelightName = limelightName;
    addRequirements(drive);
    System.out.println("AlignToTag Constructor Called");
}

public void initialize(){
    System.out.println("AlignToTag Initialized");
}

public void execute() {
    System.out.println("AlignToTag Executed");
    double rotSpeed = TagAlignment.aimAtTag(limelightName, kP);
    drive.drive(0, 0, rotSpeed, true);
}

public boolean isFinished() {
    if (!LimelightHelpers.getTV(limelightName)) {
        System.out.println("No valid target detected");
        return false;
    }
    
    boolean aligned = Math.abs(LimelightHelpers.getTX(limelightName)) < 2.0;

    if (aligned){
        System.out.println("Robot is aligned!");
    }

    return aligned;
    
    //return false;
}


public void end(boolean interrupted) {
    System.out.println("AlignToTag Command Ended(interrupted: " + interrupted + " )");
    drive.drive(0,0,0,true);
    System.out.println("Command ended");
}

}
