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
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.Config;
import frc.robot.Convert;
import frc.robot.Network;
import frc.robot.PIDConstants;
import frc.robot.Robot;
import frc.robot.commands.ArmManual;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Arm extends Subsystem {

    public WPI_TalonSRX wrist;

    private final int startPos = 280;
    private double target = startPos;
    private double targetA = target;//adjusted target
    private double akP = 2.2;
    private double akI = 0;
    private double akD = 60;
    private double akF = 1023.0/225.0;
    private double akPeak = 1;
    private double akRamp = 0.09;
    private double akAllowable = 25;
    private int akCruise = 130;
    private int akCruiseItem = 110;
    //private double akAccelTime = 0.9;//seconds (1.1)
    private int akAccel = 260;
    private int akAccelItem = 210;
    //behavior constants
    private final double akAntiArm = 0.08;//percent with unburdened arm
    private final double akAntiItem = 0.13;//percent with burdened arm
    private double akAntiGrav = akAntiArm;//How much PercentOutput is required for the motor to stall while horizontal
    public final int akMinB = Convert.getCounts(-85);//0 degrees straight up, positive forward
    public final int akMaxB = Convert.getCounts(-75);
    public final int akMinF = Convert.getCounts(23);
    public final int akMaxF = Convert.getCounts(120);
    public final int akHatchOutFree = Convert.getCounts(80);
    public final int akHatchOutItem = Convert.getCounts(78);
    public int akHatchOutF = akHatchOutFree;
    public final int akHatchOutB = akMinB;
    //state
    private boolean manual=false;
    private boolean armHadItem = false;
    private boolean armGotItem = false;
    private boolean armLostItem = false;

    public Arm() {
        wrist = new WPI_TalonSRX(7);

        Config.configAllStart(wrist);

        wrist.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, PIDConstants.kIdx, Config.kTimeout);
        wrist.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 10, Config.kTimeout);
        Config.configSensor(wrist, startPos);
        wrist.setInverted(false);
        wrist.setSensorPhase(false);
        Config.configCruise(akCruise, wrist);
        Config.configAccel(akAccel, wrist);
        wrist.configMotionSCurveStrength(4, Config.kTimeout);
        Config.configClosed(wrist, akP, akI, akD, akF, akPeak, akRamp);
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new ArmManual());
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        checkState();
        
        targetA=target;

        targetA=Math.max(((Robot.intake.getBackdriving() && !getHasItem())? Convert.getCounts(7):akMinF), targetA);
        targetA=Convert.limit(akMinB, akMaxF, targetA);

        double ff = calcGrav();//feed forward

        boolean liftResting = Robot.elevator.getIsResting();
        if(liftResting){
            targetA=startPos;//set to resting angle
            ff=0.06;//forward pressure on arm while down
        }

        if(!manual) aMotionPID(targetA, ff);

        putNetwork();
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private void checkState(){//state changes
        armGotItem=getHasItem()&&!armHadItem;
        armLostItem=!getHasItem()&&armHadItem;
        armHadItem=getHasItem();

        if(armGotItem){
            akHatchOutF=akHatchOutItem;
            Config.configCruise(akCruiseItem, wrist);
            Config.configAccel(akAccelItem, wrist);
        }
        else if(armLostItem){
            akHatchOutF=akHatchOutFree;
            Config.configCruise(akCruise, wrist);
            Config.configAccel(akAccel, wrist);
        }
        
        akAntiGrav=(getHasItem())? akAntiItem:akAntiArm;
        
        //feed
    }
    private void putNetwork(){
        Network.put("Arm Target", target);
        Network.put("Arm TargetA", targetA);
        Network.put("Arm Pos", getPos());
        Network.put("Arm Deg", getDeg());
        Network.put("Arm Native", Convert.getNative(wrist));
        Network.put("Arm Power", wrist.getMotorOutputPercent());
    }
    
    private void aMotionPID(double pos){
		wrist.set(ControlMode.MotionMagic, pos);
	}
	private void aMotionPID(double pos, double feed){
		wrist.set(ControlMode.MotionMagic, pos, DemandType.ArbitraryFeedForward, feed);
	}

    private double calcGrav(){//resist gravity
        double gravity = -Math.sin(Math.toRadians(getDeg()));//0 degrees is straight up, so gravity is a sin curve
        double counterForce = (gravity*akAntiGrav);//multiply by the output percent for holding stable while 90 degrees
        counterForce = Convert.limit(counterForce);
        return counterForce;
    }
    
    //interaction
    public boolean isTarget(){
        return (getPos()<=target+akAllowable && getPos()>=target-akAllowable);
    }
    public boolean isTarget(int t){
        return (getPos()<=t+akAllowable && getPos()>=t-akAllowable);
    }
    
    public int getPos(){
        return wrist.getSelectedSensorPosition();
    }
    public double getDeg(){
        return Convert.getDegrees(getPos());
    }
    public boolean getHasItem(){
        return Robot.manipulator.getIsOpen();
    }

    public void setTarget(int t){
        target=t;
    }
    public void setIsManual(boolean b){
        manual=b;
    }
    public void setWrist(double x){
		wrist.set(ControlMode.PercentOutput, x, DemandType.ArbitraryFeedForward, calcGrav());
    }
}

