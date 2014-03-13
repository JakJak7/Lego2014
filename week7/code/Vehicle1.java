import lejos.nxt.*;
import java.lang.Math;

public class Vehicle1 {
    private int behavior;
    private int sValue() {
	int value = SensorPort.S1.readRawValue();
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

    private int map(int behavior, int raw) {
	if (behavior == 1) // [0;100]
	    return (int) (100-raw/1023.0*100);
	else if (behavior == 2) // [-100;100]
	    return map(1,raw)*2-100;
	else if (behavior == 3) // Party Mode (BabyBaby)
	    return (int) ((100-Math.max(0,raw-362)/460.0*100)*2-100);
	return 0;
    }

    public Vehicle1(int behavior) throws Exception {
	this.behavior = behavior;
	SensorPort.S1.setTypeAndMode(SensorConstants.TYPE_SOUND_DB,SensorConstants.MODE_RAW);
	Thread.sleep(120); //Sensor value falling
	run();
	LCD.clear();
    }

    protected void run() throws Exception {
	//DataLogger dl = new DataLogger("samp.dat");
	//dl.start();
       while (! Button.ESCAPE.isDown()) {
	   mPower(map(behavior, sValue()));
	   //dl.writeSample(sValue());
	   //Thread.sleep(50);
       }
       //dl.close();
    }

    public static void main (String[] aArg) throws Exception {
	new Vehicle1(2);
    }
}
