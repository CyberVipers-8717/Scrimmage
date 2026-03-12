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
   // private final SparkMax m_lift2;
    private final SparkMax m_intake;
    
    //Set up encoders
    private final RelativeEncoder lift1Encoder;
   // private final RelativeEncoder lift2Encoder;
    private final RelativeEncoder intakeEncoder;

    //Set up config objects
    private final SparkMaxConfig lift1Config;
   // private final SparkMaxConfig lift2Config;
    private final SparkMaxConfig intakeConfig;

    //PID Controllers for hopper motors
    private final SparkClosedLoopController lift1Controller;
  //  private final SparkClosedLoopController lift2Controller;
    private final SparkClosedLoopController intakeController;

     //PID variables for shooter & queuer
    //Starting values for now, change to meet needs
    private static final double lift1_kP = 0.075;
    private static final double lift1_kI = 0.003;
    private static final double lift1_kD = 0;
    
    // private static final double lift2_kP = 0.0001;
    // private static final double lift2_kI = 0;
    // private static final double lift2_kD = 0.0001;

    private static final double intake_kP = 0.2;
    private static final double intake_kI = 0;
    private static final double intake_kD = 0;
    
    // 1 / 5676 (Neo motor max speed)
    private static final double FF = 0.000175;

    public IntakeSubsystem(){

         //Initialize motors (need to change random ids)
        m_lift1 = new SparkMax(15, MotorType.kBrushless);
        //m_lift2 = new SparkMax(2, MotorType.kBrushless);
        m_intake = new SparkMax(12, MotorType.kBrushless);


        //Initialize encoders
        lift1Encoder = m_lift1.getEncoder();
        //lift2Encoder = m_lift2.getEncoder();
        intakeEncoder = m_intake.getEncoder();

        //Initialize PID controllers
        lift1Controller = m_lift1.getClosedLoopController();
        //lift2Controller = m_lift2.getClosedLoopController();
        intakeController = m_intake.getClosedLoopController();

        //Initialize configurations
        lift1Config = new SparkMaxConfig();
        //lift2Config = new SparkMaxConfig();
        intakeConfig = new SparkMaxConfig();

        //Set up configurations, starting with idle mode and current limit
        lift1Config.idleMode(IdleMode.kBrake);
        lift1Config.smartCurrentLimit(40);
        //Now set up closed loop control configs
        lift1Config.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(lift1_kP, lift1_kI, lift1_kD)
            .outputRange(-1, 1);
        
        //Same for the hopper2 config
        // lift2Config.idleMode(IdleMode.kBreak);
        // lift2Config.smartCurrentLimit(40);

        // lift2Config.closedLoop
        //     .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        //     .pid(lift2_kP, lift2_kI, lift2_kD)
        //     .velocityFF(FF)
        //     .outputRange(-1, 1);

        //Same for the intake config
        intakeConfig.idleMode(IdleMode.kCoast);
        intakeConfig.smartCurrentLimit(40);

        intakeConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(intake_kP, intake_kI, intake_kD)
            .velocityFF(FF)
            .outputRange(-1, 1);
        
        //Apply the configurations to motors and set inverted to true if needed
        lift1Config.inverted(true);
        m_lift1.configure(lift1Config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

       // lift2Config.inverted(true);
       // m_lift2.configure(lift2Config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        intakeConfig.inverted(true);
        m_intake.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    //Set and return volts of intake motor
    public void setIntakePower(double volts){
        m_intake.setVoltage(volts);
    }
    
    //Return the current volts of the intake motor
    public double getIntakeVolts(){
        return intakeEncoder.getVelocity();
    }

    //Return the current draw of the intake motor
    public double getIntakeCurrent(){
        return m_intake.getOutputCurrent();
    }

    public void setLiftPower(double pos) {
        lift1Controller.setSetpoint(pos, SparkMax.ControlType.kPosition);
    }

    public void setMotor(double speed) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMotor'");
    }
    
}



