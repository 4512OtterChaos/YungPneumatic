package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.sensors.PigeonIMU;
public class Input{
    /* Sensors */
    private static PigeonIMU pigeon = new PigeonIMU(5);
    private static PigeonIMU.GeneralStatus pStat = new PigeonIMU.GeneralStatus();
    private static double[] ypr = new double[3];//yaw[0], pitch[1], roll[2]
    public static Limelight frontLime;
    public static Limelight backLime;
    
    /* Controls */
	public static XboxController xbox; //object for controller --more buttons :)
    private static Hand KLEFT = GenericHID.Hand.kLeft; //constant referring to
    private static Hand KRIGHT = GenericHID.Hand.kRight;//the side of controller

    public static void init(){
        /* Controls' assignment*/
		xbox = new XboxController(0);
        
        /* Sensor assignment *///code matches electrical
        //comment these out as necessary
        pigeon.getGeneralStatus(pStat);
        pigeon.getYawPitchRoll(ypr);
        frontLime = new Limelight();
        backLime = new Limelight("limelight-one");
    }

    /**
     * @param value What value should be constrained
     * @return Value constrained to deadband.
     */
	private static double deadband(double value) {
		double deadzone = 0.14;//smallest amount you can recognize from the controller
		if ((value >= +deadzone)||(value <= -deadzone)) {
			return value;//outside deadband
		}else{
			return 0;//inside deadband
		}
    }

    /**
     * @param value What value should be constrained.
     * @return Value constrained for motor output(-1 to 1).
     */
    public static double limit(double value){
        return Math.max(-1,Math.min(1,value));
    }

    public static double constrainAngle(double x, double min, double max){
        while(x<min){//constrain angles 0 - 360
            x += max;
        } if(x>max){
            x = x % max;
        }
        return x;
    }

    public static double getLeftY(){
        double joy = -deadband(xbox.getY(KLEFT));
        return joy;
    }
    public static double getLeftX(){
        double joy = deadband(xbox.getX(KLEFT));
        return joy;
    }
    public static double getRightY(){
        double joy = -deadband(xbox.getY(KRIGHT));
        return joy;
    }
    public static double getRightX(){
        double joy = deadband(xbox.getX(KRIGHT));
        return joy;
    }
    public static boolean getRightBumper(){
        return xbox.getBumper(KRIGHT);
    }
    public static boolean getLeftBumper(){
        return xbox.getBumper(KLEFT);
    }
    public static boolean getAButton(){
        return xbox.getAButton();
    }
    public static boolean getXButton(){
        return xbox.getXButton();
    }
    public static boolean getYButton(){
        return xbox.getYButton();
    }
    public static boolean getBButton(){
        return xbox.getBButton();
    }
    public static double getRightTrigger(){
        return xbox.getTriggerAxis(KRIGHT);
    }
    public static double getLeftTrigger(){
        return xbox.getTriggerAxis(KLEFT);
    }

    public static void displayStats(){
        SmartDashboard.putNumber("RJoyX", getRightX());
        SmartDashboard.putNumber("RJoyY", getRightY());
        SmartDashboard.putNumber("LJoyX", getLeftX());
        SmartDashboard.putNumber("LJoyY", getLeftY());
        SmartDashboard.putNumberArray("YPR", ypr);
        SmartDashboard.putNumber("backTx", backLime.getTx());
        SmartDashboard.putNumber("backTy", backLime.getTy());
        SmartDashboard.putNumber("backTa", backLime.getTa());
        SmartDashboard.putNumber("frontTx", frontLime.getTx());
        SmartDashboard.putNumber("frontTy", frontLime.getTy());
        SmartDashboard.putNumber("frontTa", frontLime.getTa());
    }
}