import lejos.nxt.*;
import java.io.*;
import java.lang.Math;

public class GridAccuracy {
    private LightSensor s1 = new LightSensor(SensorPort.S1,true);
    private LightSensor s2 = new LightSensor(SensorPort.S2,true);
    private int green = -1;
    private int black = 1024;
    private int white = -1;
    private int cal = 0;

    public static void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3);
    }
    public int rnv(int i) {
	int nor = ((i==1)?s1:s2).readNormalizedValue();
	return nor+((i==1)?-cal:cal);
    }

    public void calibrate() throws Exception {
	//calibrate for margin
	cal = (rnv(1)-rnv(2))/2;
	cal = 0;
	// Green
	green = rnv(1);
	LCD.drawInt(green, 2, 7, 0);
	while (rnv(1) < green+40);
	Thread.sleep(100);
	white = rnv(1);
	LCD.drawInt(white, 2, 7, 2);
	while (rnv(1) > white-40);
	while (rnv(1) < white-20)
	    black = (rnv(1) < black)?rnv(1):black;
	LCD.drawInt(black, 2, 7, 1);
    }
    public boolean isblack(int v) {
	int threshold = 15;
	return (rnv(1) > black-threshold && rnv(1) < black+threshold);
    }
    private int power = -70;

    public void leftright() {
	float wheelSize = 5.5f;
	float width = 19.6f;
	double degrees = 360*((width*2 * Math.PI)/4) / (wheelSize * Math.PI);
	degrees = degrees * 1.05f; //error correction because of inaccuracies
	Motor.B.rotate((int)degrees/2,true);
	Motor.C.rotate(-(int)degrees/2);

    }
    public void follow() {//black = 491
	LightSensor sensor = new LightSensor(SensorPort.S1);
	LCD.clear();
	float Kp = 1f;
	int Tp = 70;
	int offset = (int) (white+black)/2;
	float Ki = 0.00000f;
	int integral =0;
	LCD.drawInt(offset,0,3,0);
	while (! Button.ESCAPE.isDown()) {
	    int LightValue = sensor.readNormalizedValue();
	    LCD.drawInt(LightValue,0,3,1);
	    LCD.drawInt(offset,0,3,2);
	    int error = LightValue - offset;
	    integral += error;
	    int Turn = (int) (Kp*error) + (int) (Ki*integral);
	    int powerA = Tp+Turn;
	    int powerB = Tp-Turn;
	    LCD.drawInt(powerA,0,3,3);
	    LCD.drawInt(powerB,0,3,4);

	    LCD.drawInt(integral,0,3,5);
	    LCD.drawInt((int) (Ki*integral),0,3,5);
	    controlMotor(MotorPort.B,-powerA);
	    controlMotor(MotorPort.C,-powerB);
	}
    }
    public GridAccuracy() throws Exception {
	Motor.B.setSpeed(600);// 2 RPM
	Motor.C.setSpeed(600);
	Motor.B.backward();
	Motor.C.backward();

	LCD.drawString("green: ", 0, 0);
	LCD.drawString("black: ", 0, 1);
	LCD.drawString("white: ", 0, 2);

	calibrate();
	leftright();
	follow();
	
    }
    public static void main (String[] aArg) throws Exception {
	new GridAccuracy();
    }
}
