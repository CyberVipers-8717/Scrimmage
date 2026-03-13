
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class ClimbSubsystem extends SubsystemBase {
    //Set up climb motor
    private final SparkMax m_climb;

    //Set up encoders
    private final RelativeEncoder m_climbEncoder;

    //Set up config objects
    private final SparkMaxConfig climbConfig;

    //PID controllers for climb
    private final SparkClosedLoopController climbController;

    //PID variavles for da climb
    //change and tune these if needed
    private static final double climb_kP = 0.0001;
    private static final double climb_kI = 0.0001;
    private static final double climb_kD = 0.0001;

    //1 / 5676 (Neo motor max speed)
    private static final double FF = 0.000175;

    public ClimbSubsystem(){
       
        //Initialize climb motors (change device id ofc when the climb is on there)
        m_climb = new SparkMax(13, MotorType.kBrushless);

        //Initialize encoders
        m_climbEncoder = m_climb.getEncoder();

        //Initialize PIDs
        climbController = m_climb.getClosedLoopController();

        //Initialize configurations
        climbConfig = new SparkMaxConfig();

        //Set up configurations, start with idle mode and current limit
        climbConfig.idleMode(IdleMode.kBrake);
        climbConfig.smartCurrentLimit(60); 
        //Now set up closed loop control configs
        climbConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(climb_kP, climb_kI, climb_kD)
            .velocityFF(FF)
            .outputRange(-1, 1);
        
        //Apply the configurations to the motor and set inverted to true if needed
        climbConfig.inverted(false);
        m_climb.configure(climbConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        //reset encoder
        m_climbEncoder.setPosition(0);
    }

    //Big Constructor is finished, create helpful methods for simple comands or to return to specific values
   
    //Resets Encoders
    public void zeroEncoder(){
        m_climbEncoder.setPosition(0);
    }

    //moves the eclimb motor
    public void setMotor(double speed){
        m_climb.set(speed);
    }

    //gets the current position of the climb in rotations
    public double getClimbPosition() {
        return m_climbEncoder.getPosition(); 
    }

    //Return the current draw from climb motor
    public double getClimbCurrent(){
        return m_climb.getOutputCurrent();
    }
    
    //stop climb motor (maybe need this)
    public void stopClimb(){
        m_climb.stopMotor();
    }

    //Periodic function is predefined and occurs periodically (50 times a second)
    public void periodic(){
        SmartDashboard.putNumber("Climb Position: ", getClimbPosition());
        SmartDashboard.putNumber("Climb Current (A): ", getClimbCurrent());
    }
}
