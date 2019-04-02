// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package frc.robot.subsystems.driveCommands;

import edu.wpi.first.wpilibj.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.common.Limelight;
public class VisionTurnTarget extends PIDCommand {

    private Limelight lime;

    private static final double
        P = 0.010,
        I = 0.0,
        D = 0.005;

    private final double dead = 0.8;//angle of negligence
    private final double minimum = 0.05;

    public VisionTurnTarget() {
        super("VisionTurnTarget", P, I, D, Robot.drive);
        setSetpoint(0);
        getPIDController().setAbsoluteTolerance(dead);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        lime = Robot.chassis.frontLime;
        Robot.drive.shiftSet(1);
        lime.lightOn();

        super.initialize();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    @Override
    protected void usePIDOutput(double output){
        if(lime.getTx()>0) output+=minimum;
        else output-=minimum;
        Robot.drive.setTurn(output);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return getPIDController().onTarget();
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.drive.setDrive(0,0);
        Robot.drive.shiftDefault();
        lime.lightOff();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected double returnPIDInput(){
        return lime.getTx();
    }
}
