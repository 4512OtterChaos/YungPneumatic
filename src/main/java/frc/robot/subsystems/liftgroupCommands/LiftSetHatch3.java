// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package frc.robot.subsystems.liftgroupCommands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.armCommands.ArmSetHatchOut;
import frc.robot.subsystems.elevatorCommands.ElevatorSetHatch3;
import frc.robot.subsystems.manipulatorCommands.ClosePusher;
public class LiftSetHatch3 extends CommandGroup {

    public LiftSetHatch3() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
        addParallel(new ElevatorSetHatch3());
        addParallel(new ClosePusher());
        addSequential(new ConditionalCommand(new WaitCommand(0.3), new WaitCommand(0.1)){
            @Override
            protected boolean condition() {
                return Robot.elevator.getPos()<=RobotMap.ELEV_HATCH1+RobotMap.ELEV_ERROR;
            }
        });
        addSequential(new ArmSetHatchOut());
    } 
}
