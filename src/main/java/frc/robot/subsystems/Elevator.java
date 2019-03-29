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
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.common.*;
import frc.robot.Robot;
import frc.robot.subsystems.elevatorCommands.*;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {
    public WPI_TalonSRX front;
    public WPI_TalonSRX back;
    public DigitalInput stageTop = new DigitalInput(0);
    public DigitalInput carriageTop = new DigitalInput(1);
    public DigitalInput stageBot = new DigitalInput(2);
    public DigitalInput carriageBot = new DigitalInput(3);

    //pid
    private double target=0;
    private double targetA=target;
    private double ekP = 0.65;
    private double ekI = 0;//0.005
    private double ekD = 35;
    private double ekF = 1023.0/5000.0;
    private double ekPeak = 0.9;
    private double ekRamp = 0.13;
    private final int ekAllowable=400;
    private int ekCruise = 3600;
    //private double ekAccelTime = 0.8;//seconds
    private int ekAccel = 3900;//encoder counts per 100 ms per second
    //behavior
    public final double ekAntiGrav = 0.06;
    public final int ekBottom=0;
    public final int ekSupply=4850;
    public final int ekHatch1=5100;
    //public final int ekLowOver=6000;
    public final int ekCargoOut=7000;
    public final int ekHatch2=24500;
    public final int ekHatch3=47100;
    //public final int ekCargoIn=28200;
    //public final int ekCargoOut=40000;
    private boolean manual=false;

    public Elevator() {
        front = new WPI_TalonSRX(5);
        
        back = new WPI_TalonSRX(6);
        
        Config.configAllStart(front);
        Config.configAllStart(back);

        back.follow(front);
        front.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, MConstants.kIdx, Config.kTimeout);
        front.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 10, Config.kTimeout);
        Config.configSensor(front);
        front.setInverted(true);
        back.setInverted(InvertType.FollowMaster);
        front.setSensorPhase(true);
        Config.configCruise(ekCruise, front);
        Config.configAccel(ekAccel, front);
        front.configMotionSCurveStrength(4, Config.kTimeout);
        Config.configClosed(front, ekP, ekI, ekD, ekF, ekPeak, ekRamp);
        front.config_IntegralZone(MConstants.kIdx, ekAllowable, Config.kTimeout);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new ElevatorManual());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        checkState();

        targetA=Convert.limit(ekBottom, ekHatch3, target);//physical limits

        if(getWantRest()){
            targetA=0;
            if(!getIsResting()) targetA=-200;
        }

        if(isTarget(ekCargoOut) && isTarget((int)target, ekCargoOut)){
            Robot.intake.setBackdriving(true);
        }
        else{
            Robot.intake.setBackdriving(false);
        }

        if(!manual) eMotionPID(targetA, (getIsResting()? 0:ekAntiGrav));

        putNetwork();
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private void checkState(){//state changes
    }

    private void putNetwork(){
        Network.put("Carriage Top", getCarrTop());
        Network.put("Carriage Bot", getCarrBot());
        Network.put("Stage Top", getStageTop());
        Network.put("Stage Bot", getStageBot());
        Network.put("Elev Pos", getPos());
        Network.put("Elev Target", targetA);
        Network.put("Elev Power", front.getMotorOutputPercent());
    }

    private void eMotionPID(double pos){
		front.set(ControlMode.MotionMagic, pos);
	}
	private void eMotionPID(double pos, double feed){
		front.set(ControlMode.MotionMagic, pos, DemandType.ArbitraryFeedForward, feed);
	}
	private void ePosPID(double pos){
		front.set(ControlMode.Position, pos);
	}
	private void ePosPID(double pos, double feed){
		front.set(ControlMode.Position, pos, DemandType.ArbitraryFeedForward, feed);
    }
    
    public boolean isTarget(){//finish commands if the position meets target
        return (getPos()<=target+ekAllowable && getPos()>=target-ekAllowable);
    }
    public boolean isTarget(int target){
        return (getPos()<=target+ekAllowable && getPos()>=target-ekAllowable);
    }
    public boolean isTarget(int pos, int target){
        return (pos<=target+ekAllowable && pos>=target-ekAllowable);
    }

    //interaction
    public int getPos(){
        return front.getSelectedSensorPosition();
    }
    public boolean getIsResting(){
        return getCarrBot()&&getStageBot();
    }
    public boolean getWantRest(){
        return (getPos()<=3*ekAllowable && target<=getPos()+ekAllowable);
    }
    public boolean getCarrTop(){
        return !carriageTop.get();
    }
    public boolean getCarrBot(){
        return !carriageBot.get();
    }
    public boolean getStageTop(){
        return !stageTop.get();
    }
    public boolean getStageBot(){
        return !stageBot.get();
    }

    public void setTarget(int t){
        target = t;
    }

    public void setIsManual(boolean b){
        manual=b;
    }
    public void setElev(double x){
		front.set(ControlMode.PercentOutput, x, DemandType.ArbitraryFeedForward, (getIsResting()? 0:ekAntiGrav));
    }
}

