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

public class ShooterSubsystem extends SubsystemBase{

    //Set up motors
    private final SparkMax m_shooterL;
    private final SparkMax m_shooterF;
    private final SparkMax m_queuer;
    private final SparkMax m_hopper;
    
    //Set up encoders
    private final RelativeEncoder shooterEncoderL;
    private final RelativeEncoder shooterEncoderF;
    private final RelativeEncoder queuerEncoder;
    private final RelativeEncoder hopperEncoder;

    //Set up config objects
    private final SparkMaxConfig shooterConfigL;
    private final SparkMaxConfig shooterConfigF;
    private final SparkMaxConfig queuerConfig;
    private final SparkMaxConfig hopperConfig;

    //PID Controllers for shooter and queuer
    private final SparkClosedLoopController shooterControllerL;
    private final SparkClosedLoopController shooterControllerF;
    private final SparkClosedLoopController queuerController;
    private final SparkClosedLoopController hopperController;

    //PID variables for shooter & queuer
    //Starting values for now, change to meet needs
    private static final double shooter_kP = 0.003;
    private static final double shooter_kI = 0.00000125;
    private static final double shooter_kD = 0;
    
    private static final double queuer_kP = 0.2;
    private static final double queuer_kI = 0;
    private static final double queuer_kD = 0;

    private static final double hopper_kP = 0.2;   
    private static final double hopper_kI = 0.0;   
    private static final double hopper_kD = 0;   


    // 1 / 5676 (Neo motor max speed)
    private static final double FF = 0.000175;

    public ShooterSubsystem(){
        //Initialize motors
        m_shooterL = new SparkMax(10, MotorType.kBrushless);
        m_shooterF = new SparkMax(17, MotorType.kBrushless);
        m_queuer = new SparkMax(9, MotorType.kBrushless);
        //Change device ID when hopper is finished
        m_hopper = new SparkMax(14, MotorType.kBrushless);

        //Initialize encoders
        shooterEncoderL = m_shooterL.getEncoder();
        shooterEncoderF = m_shooterF.getEncoder();
        queuerEncoder = m_queuer.getEncoder();
        hopperEncoder = m_hopper.getEncoder();

        //Initialize PID controllers
        shooterControllerL = m_shooterL.getClosedLoopController();
        shooterControllerF = m_shooterF.getClosedLoopController();
        queuerController = m_queuer.getClosedLoopController();
        hopperController = m_hopper.getClosedLoopController();

        //Initialize configurations
        shooterConfigL = new SparkMaxConfig();
        shooterConfigF = new SparkMaxConfig();
        queuerConfig = new SparkMaxConfig();
        hopperConfig = new SparkMaxConfig();

        //Set up configurations, starting with idle mode and current limit
        shooterConfigL.idleMode(IdleMode.kCoast);
        shooterConfigL.smartCurrentLimit(75);
        //Now set up closed loop control configus
        shooterConfigL.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(shooter_kP, shooter_kI, shooter_kD)
            .velocityFF(FF)
            .outputRange(-1, 1);
        
        shooterConfigF.idleMode(IdleMode.kCoast);
        shooterConfigF.smartCurrentLimit(75);
        //shooterConfigF.follow(10);
        shooterConfigF.follow(10, true);

        //Do the same for the queuer config
        queuerConfig.idleMode(IdleMode.kCoast);
        queuerConfig.smartCurrentLimit(60);

        queuerConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(queuer_kP, queuer_kI, queuer_kD)
            .velocityFF(FF)
            .outputRange(-1, 1);

        // Do the same for hopper config
        hopperConfig.idleMode(IdleMode.kCoast);
        hopperConfig.smartCurrentLimit(50);

        hopperConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .pid(hopper_kP, hopper_kI, hopper_kD)
            .velocityFF(FF)
            .outputRange(-1, 1);

        //Apply the configurations to motors and set inverted to true if needed
        shooterConfigL.inverted(true);
        m_shooterL.configure(shooterConfigL, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        //shooterConfigF.inverted(true); //Its opposite to leader motor because of the gearbox
        m_shooterF.configure(shooterConfigF, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        queuerConfig.inverted(true);
        m_queuer.configure(queuerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        hopperConfig.inverted(true);
        m_hopper.configure(hopperConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    //Once constructor finished, create helpful methods for simple commands or to return specific values

    //Follower shooter motor(F) always follows Leader(L) Only change leader
    // //Set Shooter to specific RPM
    public void setShooterRPM(double rpm){
        shooterControllerL.setSetpoint(rpm, SparkMax.ControlType.kVelocity);
    }

    // Set shooter to specific voltage
    public void setShooterVoltage(double voltage) {
        m_shooterL.setVoltage(voltage);
        //shooterController.setSetpoint(voltage, SparkMax.ControlType.kVoltage);
    }

    // Set shooter to specific speed
    public void setShooterSpeed(double speed) {
        m_shooterL.set(speed);
    }

    // Method to set the shooter motor speed based on percentage of speed, not rpm
    // Change number inside .set() to desired percent (range from -1 to 1) and change
    // setShooterRPM() to setShooterPower() in appropriate commands
    // public void setShooterPower(double rpm){
    //     m_shooter.setVoltage(rpm);
    //     //shooterController.setSetpoint(rpm, SparkMax.ControlType.kVoltage);
    // }
    
    //Return the current RPM of the shooter
    public double getShooterRPM(){
        return shooterEncoderL.getVelocity();
    }

    //Return the current draw of the shooter motor
    public double getShooterCurrent(){
        return m_shooterL.getOutputCurrent();
    }

    //Set queuer to specific RPM
    // public void setQueuerRPM(double rpm){
    //     queuerController.setSetpoint(rpm, SparkMax.ControlType.kVelocity);
    // }

    //Method to set the queuer motor speed based on percentage of speed, not rpm
    //Change number inside .set() to desired percent (range from -1 to 1) and change
    //setQueuerRPM() to setQueuerPower() in appropriate commands
    public void setQueuerVoltage(double voltage){
        m_queuer.setVoltage(voltage);
    }

    //Return the current RPM of the queuer
    public double getQueuerRPM(){
        return queuerEncoder.getVelocity();
    }

    //Return the current draw of the queuer motor
    public double getQueuerCurrent(){
        return m_queuer.getOutputCurrent();
    }

    //Stop queuer motor
    public void stopQueuer(){
        m_queuer.stopMotor();
    }

    //Method to set the Hopper motor speed based on percentage of speed, not rpm
    //Change number inside .set() to desired percent (range from -1 to 1) and change
    //setHopperRPM() to setHopperPower() in appropriate commands
    public void setHopperVoltage(double volts){
        m_hopper.setVoltage(volts);
    }

    public void setHopperSpeed(double speed) {
        m_hopper.set(speed);
    }

    public void setHopperRPM(double rpm){
        hopperController.setSetpoint(rpm, SparkMax.ControlType.kVelocity);
    }
    //return curremt rpm of hopper
    public double getHopperRPM(){
        return hopperEncoder.getVelocity();
    }

    //Return the current draw from hopper motor
    public double getHopperCurrent(){
        return m_hopper.getOutputCurrent();
    }

    //Stop hopper motor
    public void stopHopper(){
        m_hopper.stopMotor();
    }


    //Stop all motors
    public void stop(){
        m_shooterL.stopMotor();
        m_shooterF.stopMotor();
        m_queuer.stopMotor();
        m_hopper.stopMotor();
    }

    //Periodic function is predefined and occurs periodically (50 times a second)
    public void periodic(){
        SmartDashboard.putNumber("Shooter RPM: ", getShooterRPM());
        SmartDashboard.putNumber("Queuer RPM: ", getQueuerRPM());
        SmartDashboard.putNumber("Hopper RPM: ", getHopperRPM());
        SmartDashboard.putNumber("Shooter Current (A): ", getShooterCurrent());
        SmartDashboard.putNumber("Queuer Current (A): ", getQueuerCurrent());
        //SmartDashboard.putNumber("Hopper Current (A): ", getHopperCurrent());
    }    

}
