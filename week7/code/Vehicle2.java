import lejos.nxt.*;
import java.lang.Math;

public class Vehicle2 {
    private int lValue(int i) {
	int value = ((i == 1)?SensorPort.S3:SensorPort.S4).readRawValue();
        LCD.drawString("lValue: ",0,i-1);
	LCD.drawInt(value,4,10,i-1); 
	return map(value);
    }

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
	if (p1 < 50) p1 = 50;
	if (p2 < 50) p2 = 50;
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

    private int MIN_LIGHT = 1023;
    private int MAX_LIGHT = 0;
    private int[] joarkin = new int[300];
    private int size = 0;
    private int pos = 0;

    private int map(int raw) {
	if (behavior == 1 || behavior == 2)
	    return (int) (100-raw/1023.0*100);
	else if (behavior == 3 || behavior == 4) {
	    if (behavior == 4) {
		if (size == joarkin.length) { //pop
		    int pop = joarkin[pos];
		    if (pop == MIN_LIGHT) 
			for (int i = 0;i < joarkin.length;i++) {
			    MIN_LIGHT = 1023;
			    if (joarkin[i] < MIN_LIGHT)
				MIN_LIGHT = joarkin[i];
			}
		    if (pop == MAX_LIGHT) 
			for (int i = 0;i< joarkin.length;i++) {
			    MAX_LIGHT = 0;
			    if (joarkin[i] > MAX_LIGHT)
				MAX_LIGHT = joarkin[i];
			}
		}
		else
		    size++;
		joarkin[pos] = raw;
		pos++;
		if (pos == joarkin.length)
		    pos = 0;
	    }

	    if (raw < MIN_LIGHT) MIN_LIGHT = raw;
	    if (raw > MAX_LIGHT) MAX_LIGHT = raw;
	    if (MIN_LIGHT == MAX_LIGHT) MAX_LIGHT++;
	    int out = (int) 100-((raw-MAX_LIGHT)*100)/(MIN_LIGHT-MAX_LIGHT);
	    if (out < 0) return 0;
	    if (out > 100) return 100;
	    return out;
	}
	else
	    return -1;
    }
    private int behavior = -1;
    public Vehicle2(int behavior) throws Exception {
	this.behavior = behavior;
	SensorPort.S3.setTypeAndMode(SensorConstants.TYPE_LIGHT_INACTIVE,SensorConstants.MODE_RAW);
	SensorPort.S4.setTypeAndMode(SensorConstants.TYPE_LIGHT_INACTIVE,SensorConstants.MODE_RAW);
	Thread.sleep(120); //Sensor value falling

	while (!Button.ESCAPE.isDown()) {
	    mPower(lValue(1),lValue(2));
	    Thread.sleep(10);
	}
	LCD.clear();
    }

    public static void main (String[] aArg) throws Exception {
	new Vehicle2(4);
    }
}
