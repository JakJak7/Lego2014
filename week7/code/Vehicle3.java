import lejos.nxt.*;
import java.lang.Math;

public class Vehicle3 {
    private UltrasonicSensor us1;
    private UltrasonicSensor us2;

    private int soundValue(int i) {
	int value = ((i == 1)?SensorPort.S3:SensorPort.S4).readRawValue();
        LCD.drawString("sound: ",0,i+1);
	LCD.drawInt(value,4,10,i+1); 
	return soundMap(value);
    }

    private int sonicValue(int i) {
	int value = ((i == 1)?us1:us2).getDistance();
        LCD.drawString("sonic: ",0,i-1);
	LCD.drawInt(value,4,10,i-1); 
	if (value > 40)
	    value = 255;
	return sonicMap(value);
    }

    private void mPower(int p1, int p2) {//-100 -> 100
	int d1 = (p1 > 0)?1:2;
	int d2 = (p2 > 0)?1:2;
	if (p1 < 0) p1 = -p1;
	if (p2 < 0) p2 = -p2;
	
        LCD.drawString("Power1: ",0,4);
	LCD.drawInt((int) (Math.pow(-1,d1+1)*p1),4,10,4); 
	LCD.drawString("Power2: ",0,5);
	LCD.drawInt((int) (Math.pow(-1,d2+1)*p2),4,10,5); 

	// anti hyl!
	if (p1 < 50) p1 = 50;
	if (p2 < 50) p2 = 50;

	MotorPort.B.controlMotor(p1,d1);
	MotorPort.C.controlMotor(p2,d2);
    }
    private int sonicMap(int raw) {
	return (int) -(100-raw/255.0*100);
    }

    private int MIN_SOUND = 1023;
    private int MAX_SOUND = 0;
    private int[] joarkin = new int[300];
    private int size = 0;
    private int pos = 0;

    private int soundMap(int raw) {
	if (size == joarkin.length) { //pop
	    int pop = joarkin[pos];
	    if (pop == MIN_SOUND) 
		for (int i = 0;i < joarkin.length;i++) {
		    MIN_SOUND = 1023;
		    if (joarkin[i] < MIN_SOUND)
			MIN_SOUND = joarkin[i];
		}
	    if (pop == MAX_SOUND) 
		for (int i = 0;i< joarkin.length;i++) {
		    MAX_SOUND = 0;
		    if (joarkin[i] > MAX_SOUND)
			MAX_SOUND = joarkin[i];
		}
	}
	else
	    size++;
	joarkin[pos] = raw;
	pos++;
	if (pos == joarkin.length)
	    pos = 0;

	if (raw < MIN_SOUND) MIN_SOUND = raw;
	if (raw > MAX_SOUND) MAX_SOUND = raw;
	if (MIN_SOUND == MAX_SOUND) MAX_SOUND++;
	int out = (int) ((raw-MAX_SOUND)*100)/(MIN_SOUND-MAX_SOUND);
	if (out < 0) return 0;
	if (out > 100) return 100;
	return out;
    }

    public Vehicle3() throws Exception {
	this.us1 = new UltrasonicSensor(SensorPort.S1);
	this.us2 = new UltrasonicSensor(SensorPort.S2);
	SensorPort.S3.setTypeAndMode(SensorConstants.TYPE_LIGHT_INACTIVE,SensorConstants.MODE_RAW);
	SensorPort.S4.setTypeAndMode(SensorConstants.TYPE_LIGHT_INACTIVE,SensorConstants.MODE_RAW);
	Thread.sleep(120); //Sensor value falling

	while (!Button.ESCAPE.isDown()) {
	    mPower(soundValue(1)+sonicValue(1),soundValue(2)+sonicValue(2));
	    Thread.sleep(30);
	}
	LCD.clear();
    }

    public static void main (String[] aArg) throws Exception {
	new Vehicle3();
    }
}
