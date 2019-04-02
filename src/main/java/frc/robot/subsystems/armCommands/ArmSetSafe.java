// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package frc.robot.subsystems.armCommands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
public class ArmSetSafe extends Command {

    /*
    private int startPos;
    private boolean unsafe = false;
    private boolean high = false;
    private double time = 0.6;
    */
    public ArmSetSafe() {
        requires(Robot.arm);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        /*
        int curr = Robot.elevator.getPos();
        int cutoff = RobotMap.ELEV_SUPPLY+RobotMap.ELEV_ERROR;
        int safehigh = RobotMap.ELEV_HATCH2;
        high = curr>cutoff;
        time = Convert.interpolate(0.6, 0.05, (curr-cutoff)/(double)(safehigh-cutoff));
        */
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        Robot.arm.setTarget(RobotMap.ARM_CLOSE_FORWARD);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        //if(high && timeSinceInitialized()>=time) return true;
        return Robot.arm.isTarget(RobotMap.ARM_CLOSE_FORWARD);
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
