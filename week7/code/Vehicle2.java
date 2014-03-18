import lejos.nxt.*;
import java.lang.Math;

public class Vehicle2 {
    private int lValue(int i) {
	int value = (i == 1)?SensorPort.S1:SensorPort.S2;
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
	if (p1 < 50) d1 = 4;
	if (p2 < 50) d2 = 4;

	MotorPort.B.controlMotor(p1,d1);
	MotorPort.C.controlMotor(p2,d2);
    }

    private int map(int raw) {
	// Hvordan er mapping på light- og sonic-sensor
	return (int) (100-raw/1023.0*100);
    }

    public Vehicle2() throws Exception {
	SensorPort.S1.setTypeAndMode(SensorConstants.TYPE_LIGHT_INACTIVE,SensorConstants.MODE_RAW);
	Thread.sleep(120); //Sensor value falling

	while (!Button.ESCAPE.isDown()) {
	    mPower(lValue(1),lValue(2));
	}
	LCD.clear();
    }

    public static void main (String[] aArg) throws Exception {
	new Vehicle2();
    }
}
