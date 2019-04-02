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

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.common.Convert;
import frc.robot.common.Limelight;
import frc.robot.common.Network;
public class DriveVision extends Command {

    private Limelight lime;
    private final double maxSpeed = 0.35;//fastest while tracking
    private final double safeArea = 6.5;//percent area when close
    private final double pow = (4/2.0);//curve motor response when close
    private final double dead = 2.5;//angle of negligence

    public DriveVision() {
        requires(Robot.drive);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        lime = Robot.chassis.frontLime;
        Robot.drive.shiftSet(maxSpeed);
        lime.lightOn();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double forward = Robot.oi.driverXbox.getLeftY();
        double turn = 0.75*Robot.oi.driverXbox.getRightX();
        if(lime.getTv()==1){
            double limeTurn = lime.getTx();
            if(!(Math.abs(limeTurn)<=dead)){
                limeTurn /= (15.0);//degrees -> percentage fov
                limeTurn = ((limeTurn<0)? -1:1)*Math.pow(Math.abs(limeTurn), pow);
                if(limeTurn<0){
                    limeTurn = -1*Math.pow(Math.abs(limeTurn), pow);
                    limeTurn = Convert.limit(limeTurn);
                    limeTurn = 0.88*limeTurn-0.12;
                }
                else{
                    limeTurn = 1*Math.pow(Math.abs(limeTurn), pow);
                    limeTurn = Convert.limit(limeTurn);
                    limeTurn = 0.88*limeTurn+0.12; 
                }
                turn += limeTurn;
            }
            
            //
            double area = (lime.getTa());
            double limeForward = 1*((safeArea-area)/safeArea);
            forward+=limeForward;
            Network.put("Target Distance", area);
        }
        else{
            Network.put("Target Distance", 0);
        }
        Robot.drive.setForward(forward);
        Robot.drive.setTurn(turn);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        Robot.drive.shiftDefault();
        lime.lightOff();
    }
}
