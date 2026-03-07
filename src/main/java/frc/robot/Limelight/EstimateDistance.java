package frc.robot.Limelight;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.Limelight.LimelightHelpers;

public class EstimateDistance {
    String limelightName;

    //From limelight docs: https://docs.limelightvision.io/docs/docs-limelight/tutorials/tutorial-estimating-distance
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry ty = table.getEntry("ty");
    double targetOffsetAngle_Vertical = ty.getDouble(0.0);

    // how many degrees back is your limelight rotated from perfectly vertical?
    double limelightMountAngleDegrees = 0.0;

    // distance from the center of the Limelight lens to the floor
    double limelightLensHeightInches = 18.0;

    // distance from the target to the floor
    double goalHeightInches = 72.0;

    double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
    double angleToGoalRadians = angleToGoalDegrees * (Math.PI / 180.0);

    //calculate distance
    double distanceFromLimelightToGoalInches = (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);

    double distanceInMeters = distanceFromLimelightToGoalInches * 0.0254;
    
    //If apriltag in view, return the distance from
    public double distanceFromHub(){
        if(TagAlignment.hasValidTarget(limelightName)){
            return distanceInMeters;
        }
        return 0;
    }
}
