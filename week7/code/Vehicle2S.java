import lejos.nxt.*;
import java.lang.Math;

public class Vehicle2S {

    private void mPower(int p1, int p2) {//-100 -> 100
	int d1 = (p1 > 0)?1:2;
	int d2 = (p2 > 0)?1:2;
	if (p1 < 0) p1 = -p1;
	if (p2 < 0) p2 = -p2;
	
        LCD.drawString("mPower1: ",0,2);
	LCD.drawInt((int) (Math.pow(-1,d1+1)*p1),4,10,2); 
	LCD.drawString("mPower2: ",0,3);
	LCD.drawInt((int) (Math.pow(-1,d2+1)*p2),4,10,3); 

	// anti hyl!
	if (p1 < 50) d1 = 4;
	if (p2 < 50) d2 = 4;

	if (behavior == 2) { //b
	    int buf = p1;
	    p1 = p2;
	    p2 = buf;
	    buf = d1;
	    d1 = d2;
	    d2 = buf;
	}
	MotorPort.B.controlMotor(p1,d1);
	MotorPort.C.controlMotor(p2,d2);
    }

    private int map(int raw) {
	return (int) (100-raw/255.0*100);
    }
    private int behavior = -1;
    public Vehicle2S(int behavior) throws Exception {
	this.behavior = behavior;
	UltrasonicSensor us1 = new UltrasonicSensor(SensorPort.S1);
	UltrasonicSensor us2 = new UltrasonicSensor(SensorPort.S2);
	
	Thread.sleep(120); //Sensor value falling

	while (!Button.ESCAPE.isDown()) {
	    int d1 = map(us1.getDistance());
	    int d2 = map(us2.getDistance());
	    LCD.drawString("sValue: ",0,0);
	    LCD.drawInt(d1,4,10,0); 
	    LCD.drawString("sValue: ",0,1);
	    LCD.drawInt(d2,4,10,1); 
	    mPower(d1,d2);
	    Thread.sleep(30);
	}
	LCD.clear();
    }

    public static void main (String[] aArg) throws Exception {
	new Vehicle2S(2);
    }
}
