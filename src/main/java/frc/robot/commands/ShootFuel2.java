package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.ShooterSubsystem;

public class ShootFuel2 extends Command {
    //Create an instance of the specific subsystem class you want to create the command for
    private final ShooterSubsystem launcher;
    // Variable to represent the voltage
    private final double voltageSh;
    private final double voltageQ;
    //private final double voltageH;
    private final double speedH;
    //variable to represent the speed
    //private final double speed;
    //Timer class object to use a timer to track seconds elapsed 
    private final Timer timer = new Timer();

    //Constructor for command
    public ShootFuel2(ShooterSubsystem launcher, double voltageSh, double voltageQ, double speedH /*, double speed*/){
        this.launcher = launcher;
        //this.speed = speed;
        this.voltageSh = voltageSh;
        this.voltageQ = voltageQ;
        this.speedH = speedH;
        addRequirements(launcher);
    }


    //Starts the shooter as soon as the command is called
    //Also starts a timer to be used later
    //Outputs a message to let us know the command has been called and initialized
    @Override
    public void initialize(){
        launcher.setShooterVoltage(voltageSh);
        timer.start();
        System.out.println("***Shooter has started!***");
    }

    //Method for the things that happen periodically when the command is running
    //Only other thing we need is for the queuer to start, as shooter has already been started
    //Checks if a certain amount of time (1 sec for now) has elapsed, then starts the queuer
    @Override
    public void execute(){
        //Could also use:
        //if(timer.get() > 1){}
        if(timer.hasElapsed(1)){
            launcher.setShooterVoltage(voltageSh);
            launcher.setQueuerVoltage(voltageQ);
            //launcher.setHopperPower(voltageH);
            launcher.setHopperSpeed(speedH);
        } else {
            launcher.stopQueuer();
            launcher.stopHopper();

        }
    }

    //End condition, essentially the things that should happen when command ends
    //Stops all motors in shooter subsystem (shooter & queuer)
    //Stops and resets the timer
    //Outputs a message to let us know the method has been called
    @Override
    public void end(boolean interrupted){
        launcher.stop();
        timer.stop();
        timer.reset();
        System.out.println("Shooter has stopped. :(");
    }

    //Essentially a condition for finishing the command before button is released
    //In this case, we don't want the command to stop unless we stop it,
    //so if we use "return false;", it never stops by itself
    @Override
    public boolean isFinished(){
        return false;
    }
}