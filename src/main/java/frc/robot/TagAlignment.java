package frc.robot;

import frc.robot.Constants.DriveConstants;

public class TagAlignment {

    public static double aimAtTag(String limelightName, double kP) {
        //if limelight DOES NOT have valid target, no rotation speed applied
        if (!hasValidTarget(limelightName)){
            return 0.0;
        }

        double tx = LimelightHelpers.getTX(limelightName);
        System.out.println("Horizontal offset: " + tx + " degrees");
        double targetingAngularVelocity = tx * kP;
        System.out.println("Target velocity = " + targetingAngularVelocity);
        return -targetingAngularVelocity;
    }

    public static double driveToTag(String limelightName, double kP){
        //if limelight DOES NOT have valid target, no forward speed applied
        if (!hasValidTarget(limelightName)){
            return 0.0;
        }

        double targetingForwardSpeed = LimelightHelpers.getTA(limelightName) * kP;
        targetingForwardSpeed *= DriveConstants.kMaxSpeedMetersPerSecond;
        targetingForwardSpeed *= -1.0;
        return targetingForwardSpeed;
    }

    //method to return if limelight has a valid target, true if yes, false if no
    public static boolean hasValidTarget (String limelightName){
        //if limelight DOES NOT have valid target, print "no valid target" and return false
        if (!LimelightHelpers.getTV(limelightName)) {
            System.out.println("No valid target!");
            return false;
        //else if limelight DOES have valid target, print "has valid target" and return true
        } else {
            System.out.println("Has target!");
            return true;
        }
    }
}
