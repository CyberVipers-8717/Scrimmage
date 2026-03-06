package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;

import frc.robot.subsystems.ShooterSubsystem;

public class ShootFuel2 extends Command {
    //Create an instance of the specific subsystem class you want to create the command for
    private final ShooterSubsystem launcher;
    //variable to represent the speed in either rpm or power
    private final double speed;
    //Timer class object to use a timer to track seconds elapsed 
    private final Timer timer = new Timer();

    //Constructor for command
    public ShootFuel2(ShooterSubsystem launcher, double speed){
        this.launcher = launcher;
        this.speed = speed;
        addRequirements(launcher);
    }

    //Starts the shooter as soon as the command is called
    //Also starts a timer to be used later
    //Outputs a message to let us know the command has been called and initialized
    @Override
    public void initialize(){
        launcher.setShooterPower(speed);
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
            launcher.setQueuerPower(speed);
            launcher.setHopperPower(speed * 0.75); // sets hopper speed slower
            // System.out.println("Shooter at target RPM, Queuer activated.");
            // System.out.println("Shooter is running at: " + launcher.getShooterRPM());
            // System.out.println("Queuer is running at: " + launcher.getQueuerRPM());
            // System.out.println("Queuer current (A): " + launcher.getQueuerCurrent());
        } else {
            launcher.stopQueuer();
            launcher.stopHopper();
            // System.out.println("Waiting for shooter to reach target RPM.");
            // System.out.println("Current RPM: " + launcher.getShooterRPM());
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