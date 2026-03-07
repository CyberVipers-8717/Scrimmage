// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OIConstants;
import frc.robot.commands.AlignToTagCommand;
//import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.DriveToTag;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.ShootFuel;
import frc.robot.commands.ShootFuel2;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.DriveSubsystem;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();
  private final ShooterSubsystem m_shoot = new ShooterSubsystem();


  // Replace with CommandPS4Controller or CommandJoystick if needed
  //private final CommandXboxController m_driverController = new CommandXboxController(0);
  private final XboxController m_driverController = new XboxController(0);


  private final SendableChooser<Command> autoChooser;
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    NamedCommands.registerCommand("Shoot Fuel", new ShootFuel(m_shoot, 2500).withTimeout(5));
    // Configure the trigger bindings
    configureBindings();

    m_robotDrive.setDefaultCommand(
        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        new RunCommand(
            () -> m_robotDrive.drive(
                -MathUtil.applyDeadband(m_driverController.getRightY(), OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(m_driverController.getRightX(), OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(m_driverController.getLeftX(), OIConstants.kDriveDeadband),
                true),
            m_robotDrive));

    autoChooser = AutoBuilder.buildAutoChooser();

    SmartDashboard.putData("Auto Chooser", autoChooser);

  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    //new Trigger(m_exampleSubsystem::exampleCondition).onTrue(new ExampleCommand(m_exampleSubsystem));
    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    //m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
    //m_driverController.b().whileTrue(new ShootFuel(m_shoot, 1100));

    //While X button is held, the wheels will be set in an X formation
    new JoystickButton(m_driverController, Button.kLeftBumper.value)
      .whileTrue(new RunCommand(
            () -> m_robotDrive.setX(),
            m_robotDrive));
    

    //  new JoystickButton(m_driverController, Button.kX.value)
    //   .whileTrue(new RunCommand(
    //         () -> m_robotDrive.zeroHeading(),
    //         m_robotDrive));
    /*
    While the Right Bumper is held, the shoot command sequence is executed.
    The shoot command sequence first spins the shooter to the set rpm below,
    then starts up the queuer to the same rpm (or to a fraction which can be
    changed in the specific command to start up the queuer).
  */
 

    // new JoystickButton(m_driverController, Button.kLeftBumper.value)
    //   .whileTrue(new AlignToTagCommand(m_robotDrive, "limelight-front"));
    
    //Shooter voltage from 5.5 to 24
    new JoystickButton(m_driverController, Button.kRightBumper.value)
      .whileTrue(new ShootFuel2(m_shoot, 5.5));
    new JoystickButton(m_driverController, Button.kA.value)
      .whileTrue(new ShootFuel2(m_shoot, 7.5));
    new JoystickButton(m_driverController, Button.kB.value)
      .whileTrue(new ShootFuel2(m_shoot, 9.5));
    new JoystickButton(m_driverController, Button.kY.value)
      .whileTrue(new ShootFuel2(m_shoot, 11.5));
    new JoystickButton(m_driverController, Button.kX.value)
      .whileTrue(new ShootFuel2(m_shoot, 13.5));

   /* 
    new JoystickButton(m_driverController, Button.kRightBumper.value)
      .whileTrue(new ShootFuel(m_shoot, 5.5));  
    new JoystickButton(m_driverController, Button.kA.value)
      .whileTrue(new ShootFuel(m_shoot, 7.5));
    new JoystickButton(m_driverController, Button.kB.value)
      .whileTrue(new ShootFuel(m_shoot, 9.5));
    new JoystickButton(m_driverController, Button.kY.value)
      .whileTrue(new ShootFuel(m_shoot, 11.5));
    new JoystickButton(m_driverController, Button.kX.value)
      .whileTrue(new ShootFuel(m_shoot, 13.5));
    */
    
    

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }
}
