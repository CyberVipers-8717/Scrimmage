package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.ExponentialProfile.Constraints;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

public class AlignToHub extends Command {

    private final DriveSubsystem m_drive;
    private final PIDController distanceController = new PIDController(1, 0, 0.1);
    private final ProfiledPIDController rotationControl = new ProfiledPIDController(1, 0, 0.1, 
    new TrapezoidProfile.Constraints(Math.PI * 2, Math.PI * 4));

    private final Translation2d CenterOfHub = new Translation2d(4.625, 4.034);
    private final double desiredDistance = 2.134;
    private final double ySpeed;

    public AlignToHub(DriveSubsystem m_drive, double ySpeed){

        this.m_drive = m_drive;
        this.ySpeed = ySpeed;

        rotationControl.enableContinuousInput(-Math.PI, Math.PI);
        distanceController.setTolerance(0.1);

    }

    @Override
    public void initialize() {

        rotationControl.reset(m_drive.getPose().getRotation().getRadians());
        distanceController.reset();

    }

    @Override
    public void execute() {

        Translation2d currentPosition = m_drive.getPose().getTranslation();
        Translation2d hubToRobot = CenterOfHub.minus(currentPosition);
        double currentDistance = hubToRobot.getNorm();
        Translation2d radiusVector = hubToRobot.div(currentDistance);
        Translation2d tangentVector = new Translation2d(-radiusVector.getY(), radiusVector.getX());
        double distanceSpeed = -distanceController.calculate(currentDistance, desiredDistance);
        //double rotationSpeed = rotationControl.calculate(m_drive.getPose().getRotation(), );
    }

    @Override
    public void end(boolean isInturrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }





}
