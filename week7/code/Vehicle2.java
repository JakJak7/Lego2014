import lejos.nxt.*;
import java.lang.Math;

public class Vehicle2 {
    private int behavior;
    private int sValue() {
	int value = LightSensor(SensorPort.S1,true);
        LCD.drawString("sValue: ",0,0);
	LCD.drawInt(value,4,10,0); 
	return value;
    }

    private void mPower(int power) {
	int d1 = (power > 0)?1:2;
	power = (power < 0)?-power:power;
        LCD.drawString("mPower: ",0,1);
	LCD.drawInt((int) (Math.pow(-1,d1+1)*power),4,10,1); 

	if (power < 50) // anti hyl
	    d1 = 4;
	MotorPort.B.controlMotor(power,d1);
	MotorPort.C.controlMotor(power,d1);
    }

    public Vehicle2(int behavior) throws Exception {
	this.behavior = behavior;
	SensorPort.S1.setTypeAndMode(SensorConstants.TYPE_SOUND_DB,SensorConstants.MODE_RAW);
	Thread.sleep(120); //Sensor value falling
	run();
	LCD.clear();
    }

    public static void main (String[] aArg) throws Exception {
	new Vehicle2();
    }
}
