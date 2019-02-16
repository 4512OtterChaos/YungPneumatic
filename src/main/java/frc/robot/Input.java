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
    public static XboxController fightStick;
    private static Hand KLEFT = GenericHID.Hand.kLeft; //constant referring to
    private static Hand KRIGHT = GenericHID.Hand.kRight;//the side of controller

    public static void init(){
        /* Controls' assignment*/
        xbox = new XboxController(0);
        fightStick = new XboxController(1);
        
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

    public static double getLeftY(XboxController controller){
        double joy = -deadband(controller.getY(KLEFT));
        return joy;
    }
    public static double getLeftX(XboxController controller){
        double joy = deadband(controller.getX(KLEFT));
        return joy;
    }
    public static double getRightY(XboxController controller){
        double joy = -deadband(controller.getY(KRIGHT));
        return joy;
    }
    public static double getRightX(XboxController controller){
        double joy = deadband(controller.getX(KRIGHT));
        return joy;
    }
    public static boolean getRightBumper(XboxController controller){
        return controller.getBumper(KRIGHT);
    }
    public static boolean getLeftBumper(XboxController controller){
        return controller.getBumper(KLEFT);
    }
    public static boolean getAButton(XboxController controller){
        return controller.getAButton();
    }
    public static boolean getXButton(XboxController controller){
        return controller.getXButton();
    }
    public static boolean getYButton(XboxController controller){
        return controller.getYButton();
    }
    public static boolean getBButton(XboxController controller){
        return controller.getBButton();
    }
    public static double getRightTrigger(XboxController controller){
        return controller.getTriggerAxis(KRIGHT);
    }
    public static double getLeftTrigger(XboxController controller){
        return controller.getTriggerAxis(KLEFT);
    }

    public static void displayStats(){
        SmartDashboard.putNumber("RJoyX", getRightX(xbox));
        SmartDashboard.putNumber("RJoyY", getRightY(xbox));
        SmartDashboard.putNumber("LJoyX", getLeftX(xbox));
        SmartDashboard.putNumber("LJoyY", getLeftY(xbox));
        SmartDashboard.putNumber("RJoyX", getRightX(fightStick));
        SmartDashboard.putNumber("RJoyY", getRightY(fightStick));
        SmartDashboard.putNumber("LJoyX", getLeftX(fightStick));
        SmartDashboard.putNumber("LJoyY", getLeftY(fightStick));
        SmartDashboard.putNumberArray("YPR", ypr);
        SmartDashboard.putNumber("backTx", backLime.getTx());
        SmartDashboard.putNumber("backTy", backLime.getTy());
        SmartDashboard.putNumber("backTa", backLime.getTa());
        SmartDashboard.putNumber("frontTx", frontLime.getTx());
        SmartDashboard.putNumber("frontTy", frontLime.getTy());
        SmartDashboard.putNumber("frontTa", frontLime.getTa());
    }
}