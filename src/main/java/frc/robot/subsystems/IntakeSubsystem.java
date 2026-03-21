package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase{
 
    //Set up motors
    private final SparkMax m_lift1;
    private final SparkMax m_intake;
    
    //Set up encoders
    private final RelativeEncoder lift1Encoder;
    private final RelativeEncoder intakeEncoder;

    //Set up config objects
    private final SparkMaxConfig lift1Config;
    private final SparkMaxConfig intakeConfig;

    //PID Controllers for intake motors
    private final SparkClosedLoopController lift1Controller;
    private final SparkClosedLoopController intakeController;

    //PID variables for lift & intake
    //Starting values for now, change to meet needs
    private static final double lift1_kP = 0.0005;
    private static final double lift1_kI = 0.00001;
    private static final double lift1_kD = 0;
    
   
    private static final double intake_kP = 0.2;
    private static final double intake_kI = 0;
    private static final double intake_kD = 0;

    // Holding variables
    private boolean holding = false;
    private double holdPosition = 0;
    
    // 1 / 5676 (Neo motor max speed)
    private static final double FF = 0.000175;

    public IntakeSubsystem(){

         //Initialize motors (need to change random ids)
        m_lift1 = new SparkMax(15, MotorType.kBrushless);
        //m_lift2 = new SparkMax(2, MotorType.kBrushless);
        m_intake = new SparkMax(12, MotorType.kBrushless);


        //Initialize encoders
        lift1Encoder = m_lift1.getEncoder();
        intakeEncoder = m_intake.getEncoder();

        // Set encoders to 0
        lift1Encoder.setPosition(0);
        intakeEncoder.setPosition(0);

        //Initialize PID controllers
        lift1Controller = m_lift1.getClosedLoopController();
        intakeController = m_intake.getClosedLoopController();

        //Initialize configurations
        lift1Config = new SparkMaxConfig();
        intakeConfig = new SparkMaxConfig();

        //Set up configurations, starting with idle mode and current limit
        lift1Config.idleMode(IdleMode.kBrake);
        lift1Config.smartCurrentLimit(60);
        //Now set up closed loop control configs
        lift1Config.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(lift1_kP, lift1_kI, lift1_kD)
            .outputRange(-0.05, 0.05);
        

        //Same for the intake config
        intakeConfig.idleMode(IdleMode.kCoast);
        intakeConfig.smartCurrentLimit(40);

        intakeConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(intake_kP, intake_kI, intake_kD)
            //.velocityFF(FF)
            .outputRange(-1, 1);
        
        //Apply the configurations to motors and set inverted to true if needed
        lift1Config.inverted(true);
        m_lift1.configure(lift1Config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);


        intakeConfig.inverted(true);
        m_intake.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    //Set intake power using voltage
    public void setIntakePower(double volts){
        m_intake.setVoltage(volts);
    }

    //Set intake speed
    public void setIntakeRPM(double speed){
        intakeController.setSetpoint(speed, SparkMax.ControlType.kVelocity);
    }
    
    //Return the current volts of the intake motor
    public double getIntakeVolts(){
        return m_intake.getBusVoltage();
    }

    //Return the current draw of the intake motor
    public double getIntakeCurrent(){
        return m_intake.getOutputCurrent();
    }

    public double getLiftPosition() {
        return lift1Encoder.getPosition();
    }
    
    public void setLiftPosition(double pos) {
        lift1Controller.setSetpoint(pos, SparkMax.ControlType.kPosition);
    }

    public void stopLift(){
        m_lift1.stopMotor();
    }

    public void setLiftMotor(double speed) {
        if (speed != 0) {
            if (getLiftPosition() <= 10 && speed > 0) {
                m_lift1.set(speed);
            } else if (getLiftPosition() >= 0 && speed < 0) {
                m_lift1.set(speed);
            } else {
                System.out.println("********Subsystem end condition 1********");
                m_lift1.set(0);
            }
            holding = false;
        }
        else {
            if (!holding) {
                holdPosition = getLiftPosition();
                holding = true;
                System.out.println("********Subsystem end condition 2********");
            }
            lift1Controller.setSetpoint(holdPosition, SparkMax.ControlType.kPosition);
        }
    }

    public void setLift1Motor(double speed){
        m_lift1.set(speed);
    }

    public void setIntakeMotor(double speed) {
        m_intake.set(speed);
    }

   //Periodic function is predefined and occurs periodically (50 times a second)
    public void periodic(){
        SmartDashboard.putNumber("Lift Position: ", getLiftPosition());
    }   
}



