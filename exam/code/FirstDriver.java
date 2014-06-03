import lejos.nxt.*;
import java.io.*;
import java.lang.Math;

public class FirstDriver {
    private LightSensor sensor = new LightSensor(SensorPort.S1,true);
    private NXTRegulatedMotor M3 = Motor.C;
    private MotorPort MP1 = MotorPort.A, MP2 = MotorPort.B;
    private int green = -1;
    private int black = 1024;
    private int white = -1;
    private int power = 70;

    public int light() {
	return sensor.readNormalizedValue();
    }
    public void grap() {
	M3.rotateTo(-330);
    }
    public void release() {
	M3.rotateTo(0);
    }
    public static void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3);
    }

    public void calibrate() throws Exception {
	//	controlMotor(MP1,power);
	//controlMotor(MP2,power);

	green = light();
	LCD.drawInt(green, 2, 7, 0);
	while (light() < green+40);
	Thread.sleep(100);
	white = light();
	LCD.drawInt(white, 2, 7, 2);
	while (light() > white-40);
	while (light() < white-20)
	    black = (light() < black)?light():black;
	LCD.drawInt(black, 2, 7, 1);
    }
    public void leftturn() {
	float wheelSize = 5.5f;
	float width = 19.6f;
	double degrees = 360*((width*2 * Math.PI)/4) / (wheelSize * Math.PI);
	degrees = degrees * 1.1f; //error correction because of inaccuracies
	Motor.A.rotate((int)degrees/2,true);
	Motor.B.rotate(-(int)degrees/2);
    }
    public void followP() {
	float Kp = 1f;
	int Tp = 60;
	int offset = (int) (white+black)/2;
	float Ki = 0.00000f;
	while (! Button.ESCAPE.isDown()) {
	    int error = light() - offset;
	    int Turn = (int) (Kp*error);
	    controlMotor(MP1,(Tp+Turn));
	    controlMotor(MP2,(Tp-Turn));
	}
    }
    public FirstDriver() throws Exception {
	Motor.A.setSpeed(600);// 2 RPM
	Motor.B.setSpeed(600);
	Motor.A.forward();
	Motor.B.forward();

	LCD.drawString("green: ", 0, 0);
	LCD.drawString("black: ", 0, 1);
	LCD.drawString("white: ", 0, 2);

	//grap();
	//release();

	calibrate();
	leftturn();
	followP();
    }
    public static void main (String[] aArg) throws Exception {
	new FirstDriver();
    }
}
