package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.ShooterSubsystem;

public class RunHopper extends Command{
    private ShooterSubsystem m_shooter;
    private double speed;

    public RunHopper(ShooterSubsystem m_shooter, double speed){
        this.m_shooter = m_shooter;
        this.speed = speed;
        addRequirements(m_shooter);
    }

    @Override
    public void initialize(){
        System.out.println("***RUN HOPPER HAS BEEN INITIALIZED***");
    }

    @Override
    public void execute(){
        m_shooter.setHopperSpeed(speed);
        SmartDashboard.getNumber("Hopper speed: " , m_shooter.getHopperRPM());
        //m_shooter.setHopperPower(voltage);
        //m_shooter.setHopperRPM(rpm);
    }

    @Override
    public boolean isFinished(){
        return false;
        //m_intake.setHopperSpeed(0);
    }

    @Override
    public void end(boolean interuppted){
        m_shooter.stopHopper();
    }

}
