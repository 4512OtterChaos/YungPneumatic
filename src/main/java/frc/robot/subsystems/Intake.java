// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import frc.robot.common.*;
import frc.robot.subsystems.intakeCommands.*;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {

    public WPI_VictorSPX right;
    public WPI_VictorSPX left;

    private boolean isBackdriving = false;//hold cargo
    private final double backdrive = -0.3;
    private double targetPercent = 0;

    public Intake() {
        right = new WPI_VictorSPX(8);
        
        left = new WPI_VictorSPX(9);
        
        Config.configAllStart(right);
        Config.configAllStart(left);

        right.setInverted(false);
        left.setInverted(true);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new IntakeManual());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        double targetPercentA = targetPercent;

        if(isBackdriving && targetPercentA==0){
            targetPercentA=backdrive;
        }

        setIntake(targetPercentA);

        putNetwork();
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private void checkState(){
    }
    private void putNetwork(){
        Network.put("Backdriving", isBackdriving);
    }

    //interaction
    public boolean getBackdriving(){
        return isBackdriving;
    }
    
    public void setTarget(double percent){
        targetPercent=percent;
    }
    public void setBackdriving(boolean backdriving){
        isBackdriving=backdriving;
    }

    public void setIntake(double x){
		x*=(x<0)? 0.4:1;
		right.set(ControlMode.PercentOutput, x);
		left.set(ControlMode.PercentOutput, (x)+((x!=0)? ((x<0)? -0.15:0.15):0));
	}
}

