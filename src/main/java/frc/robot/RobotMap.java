package frc.robot;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class RobotMap{
    /*
	dRightF.
	dRightB.
	dLeftF.
	dLeftB.
	liftF.
	liftB.
	wrist.
	intakeR.
	intakeL.
	*/
    public static TalonSRX dRightF = new TalonSRX(1);
	public static TalonSRX dRightB = new TalonSRX(2);//pigeon
	public static TalonSRX dLeftF = new TalonSRX(3);
	public static TalonSRX dLeftB = new TalonSRX(4);
	public static TalonSRX liftF = new TalonSRX(5);
	public static TalonSRX liftB = new TalonSRX(6);
	public static TalonSRX wrist = new TalonSRX(7);
	public static VictorSPX intakeR = new VictorSPX(8);
    public static VictorSPX intakeL = new VictorSPX(9);

    public static TalonSRX[] driveMotors = {dRightF, dRightB, dLeftF, dLeftB};//12.75:1 400 rpm
    public static TalonSRX[] liftMotors = {liftF, liftB};//5.95:1 800 rpm 
    public static TalonSRX[] wristMotors = {wrist};//100:1 132 rpm
    public static BaseMotorController[] intakeMotors = {intakeR, intakeL};
	public static BaseMotorController[] allMotors = {dRightF, dRightB, dLeftF, dLeftB, liftF, liftB, wrist, intakeR, intakeL};
	
	public static Compressor compressor = new Compressor(0);
	public static DoubleSolenoid crabber = new DoubleSolenoid(4,5);
	public static DoubleSolenoid crabPop = new DoubleSolenoid(6,7);
	public static DoubleSolenoid intakeFlip = new DoubleSolenoid(0,1);
	public static DoubleSolenoid liftStop = new DoubleSolenoid(2,3);

	public static DigitalInput stage1Top = new DigitalInput(0);
	public static DigitalInput carriageTop = new DigitalInput(1);
	public static DigitalInput stage1Bot = new DigitalInput(2);
	public static DigitalInput carriageBot = new DigitalInput(3);

    
    /**
     * Configure the behavior of the electrical components(motor controllers, pnuematics, etc.)
     */
    public static void config(){
		configFactory(allMotors);
		//idle
		configNeutral(NeutralMode.Brake, allMotors);
		configNeutral(NeutralMode.Coast, liftMotors);
		//limits
		configPeak(Constants.kPeakReverse, Constants.kPeakForward, allMotors);
		configNominal(Constants.kNominalReverse, Constants.kNominalReverse, allMotors);

		configPeak(-Constants.wkPeak, Constants.wkPeak, wristMotors);
		configPeak(-Constants.lkPeak, Constants.lkPeak, liftMotors);
		configPeak(-Constants.dkPeak, Constants.dkPeak, driveMotors);
		//define sensor
		dRightF.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kIdx, Constants.kTimeout);
		dRightF.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 10, Constants.kTimeout);

		dLeftF.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kIdx, Constants.kTimeout);
		dLeftF.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 10, Constants.kTimeout);

		liftF.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Constants.kIdx, Constants.kTimeout);
		liftF.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 10, Constants.kTimeout);

		wrist.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Constants.kIdx, Constants.kTimeout);
		wrist.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 10, Constants.kTimeout);
        //behavior
        dRightB.follow(dRightF);
		dLeftB.follow(dLeftF);
        liftB.follow(liftF);

		dRightF.setInverted(true);
		dRightB.setInverted(InvertType.FollowMaster);
		dRightF.setSensorPhase(true);

		dLeftF.setInverted(false);
		dLeftB.setInverted(InvertType.FollowMaster);
		dLeftF.setSensorPhase(true);

		liftF.setInverted(true);
		liftB.setInverted(InvertType.FollowMaster);
        liftF.setSensorPhase(true);

		wrist.setInverted(false);
        wrist.setSensorPhase(false);

        intakeR.configOpenloopRamp(Constants.ikRamp);
        intakeR.setInverted(false);
        intakeL.configOpenloopRamp(Constants.ikRamp);
		intakeL.setInverted(true);
		//pid
		zeroSensor(allMotors);
		wrist.setSelectedSensorPosition(280, Constants.kIdx, Constants.kTimeout);
		configClosed(driveMotors, Constants.dkP, Constants.dkI, Constants.dkD, Constants.dkF, Constants.dkPeak, Constants.dkRamp);
		configClosed(liftMotors, Constants.lkP, Constants.lkI, Constants.lkD, Constants.lkF, Constants.lkPeak, Constants.lkRamp);
		configClosed(wristMotors, Constants.wkP, Constants.wkI, Constants.wkD, Constants.wkF, Constants.wkPeak, Constants.wkRamp);
		liftF.configAllowableClosedloopError(Constants.kIdx, Constants.lkAllowableClosed, Constants.kTimeout);
		configCruise(Constants.lkCruise, liftF);
		configAccel(Constants.lkAccel, liftF);
		configCruise(Constants.wkCruise, wrist);
		configAccel(Constants.wkAccel, wrist);
	}
	
	public static void zeroSensor(BaseMotorController motor){
		motor.setSelectedSensorPosition(0, Constants.kIdx, Constants.kTimeout);
	}
	public static void zeroSensor(BaseMotorController[] motors){
		for(BaseMotorController motor:motors){
			motor.setSelectedSensorPosition(0, Constants.kIdx, Constants.kTimeout);
		}
	}
    
    public static void configPeak(double negative, double positive, BaseMotorController[] motors){
        for(BaseMotorController motor:motors){
            motor.configPeakOutputForward(positive, Constants.kTimeout);
            motor.configPeakOutputReverse(negative, Constants.kTimeout);
        }
	}
	public static void configNominal(double negative, double positive, BaseMotorController[] motors){
		for(BaseMotorController motor:motors){
			motor.configNominalOutputForward(positive, Constants.kTimeout);
			motor.configNominalOutputReverse(negative, Constants.kTimeout);
		}
	}
    public static void configNeutral(NeutralMode neutral, BaseMotorController[] motors){
        for(BaseMotorController motor:motors){
            motor.setNeutralMode(neutral);
        }
    }
    public static void configFactory(BaseMotorController[] motors){
        for(BaseMotorController motor:motors){
            motor.configFactoryDefault();
        }
	}
	public static void configCruise(int uPer100ms, BaseMotorController motor){
		motor.configMotionCruiseVelocity(uPer100ms, Constants.kTimeout);
	}
	public static void configAccel(int uPer100msPer1s, BaseMotorController motor){
		motor.configMotionAcceleration(uPer100msPer1s, Constants.kTimeout);
	}
	public static void configPID(TalonSRX motor, double p, double i, double d, double f){
		motor.config_kP(Constants.kIdx, p, Constants.kTimeout);
		motor.config_kI(Constants.kIdx, i, Constants.kTimeout);
		motor.config_kD(Constants.kIdx, d, Constants.kTimeout);
		motor.config_kF(Constants.kIdx, f, Constants.kTimeout);
    }
	public static void configClosed(TalonSRX[] motors, double p, double i, double d, double f, double peak, double ramp){
		for(TalonSRX motor:motors){
			motor.configClosedloopRamp(ramp);
			motor.configClosedLoopPeakOutput(Constants.kIdx, peak);
			motor.configAllowableClosedloopError(Constants.kIdx, Constants.kAllowableClosed, Constants.kTimeout);
			configPID(motor, p, i, d, f);
		}
	}

    public static double toNative(double rpm){//convert rpm to native talon units
		return rpm*4096.0/600.0f;
	}
	public static double toRPM(double nativeU){//convert native talon units to rpm
		return nativeU/4096.0*600f;
	}

	public static double getRPM(BaseMotorController motor){
		return toRPM(motor.getSelectedSensorVelocity());
	}
	public static double getNative(BaseMotorController motor){
		return motor.getSelectedSensorVelocity();
	}
	public static double getPos(BaseMotorController motor){
		return motor.getSelectedSensorPosition();
	}

	public static double getDegrees(TalonSRX arm){//straight up is 0 degrees, negative forward
		double percent = getPos(arm)/Constants.kRotCounts;
		return percent*360;
	}
	public static double getDegrees(double counts){
		double percent = counts/Constants.kRotCounts;
		return percent*360;
	}
	public static double getCounts(double degree){
		double percent = degree/360.0;
		return percent*Constants.kRotCounts;
	}
	

	public static boolean getSwitch(DigitalInput dio){
		return !dio.get();
	}

    public static void displayStats(){
		//motor outputs
		Network.put("Right Drive Counts", getPos(dRightF));
		Network.put("Right Drive RPM", getRPM(dRightF));
		Network.put("Right Drive NativeV", getNative(dRightF));
		Network.put("Left Drive Counts", getPos(dLeftF));
		Network.put("Left Drive RPM", getRPM(dLeftF));
		Network.put("Left Drive NativeV", getNative(dLeftF));
		Network.put("Lift Counts", getPos(liftF));
		Network.put("Lift RPM", getRPM(liftF));
		Network.put("Lift NativeV", getNative(liftF));
		Network.put("Wrist Counts", getPos(wrist));
		Network.put("Wrist RPM", getRPM(wrist));
		Network.put("Wrist NativeV", getNative(wrist));
		Network.put("Wrist Degree", getDegrees(wrist));
		//pid
		double[] dPIDMap = {toRPM(Teleop.rightTarget), getRPM(dRightF), toRPM(Teleop.leftTarget), getRPM(dLeftF)};
		double[] lPIDMap = {Teleop.liftTarget, getPos(liftF)};
		double[] wPIDMap = {Teleop.wristTarget, getPos(wrist)};
		Network.put("Drive PID Map", dPIDMap);
		Network.put("Lift PID Map", lPIDMap);
		Network.put("Wrist PID Map", wPIDMap);
		//electrical
		Network.put("Compressor Current", compressor.getCompressorCurrent());
		Network.put("Compressor Switch", compressor.getPressureSwitchValue());
		Network.put("Stage 1 Bot", getSwitch(stage1Bot));
		Network.put("Stage 1 Top", getSwitch(stage1Top));
		Network.put("Carriage Top", getSwitch(carriageTop));
		Network.put("Carriage Bot", getSwitch(carriageBot));
	}
}