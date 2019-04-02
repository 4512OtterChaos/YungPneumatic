// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package frc.robot.subsystems.manipulatorCommands;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.Robot;
import frc.robot.control.controlCommands.RumbleEvent;
public class OpenClaw extends InstantCommand {

    public OpenClaw() {
        requires(Robot.manipulator);
    }

    // Called once when this command runs
    @Override
    protected void initialize() {
        Robot.manipulator.setClaw(true);
        Scheduler.getInstance().add(new RumbleEvent(0.5, 0.3));
    }

}
