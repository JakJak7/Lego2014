import lejos.nxt.*;
import java.io.*;
import java.lang.Math;

public class FirstDriver {
    private LightSensor sensor = new LightSensor(SensorPort.S1,true);
    private int green = -1;
    private int black = 1024;
    private int white = -1;
    private int power = 600;

    public int light() {
	return sensor.readNormalizedValue();
    }

    public void calibrate() throws Exception {
	green = light();
	LCD.drawInt(green, 2, 7, 0);
	while (light() < green+40);
	Thread.sleep(100);
	white = light();
	LCD.drawInt(white, 2, 7, 2);
	while (light() > white-40);
	while (light() < white-20)
	    black = (light() < black)?light():black;
    }
    public void leftturn() {
	float wheelSize = 5.5f;
	float width = 19.6f;
	double degrees = 360*((width*2 * Math.PI)/4) / (wheelSize * Math.PI);
	degrees = degrees * 1.05f; //error correction because of inaccuracies
	Motor.B.rotate((int)degrees/2,true);
	Motor.C.rotate(-(int)degrees/2);
    }
    public void followP() {
	float Kp = 1f;
	int Tp = 70;
	int offset = (int) (white+black)/2;
	float Ki = 0.00000f;
	while (! Button.ESCAPE.isDown()) {
	    int error = light() - offset;
	    int Turn = (int) (Kp*error)
	    controlMotor(MotorPort.B,Tp+Turn);
	    controlMotor(MotorPort.C,Tp-Turn);
	}
    }
    public FirstDriver() throws Exception {
	Motor.B.setSpeed(power);
	Motor.C.setSpeed(power);
	Motor.B.forward();
	Motor.C.forward();

	LCD.drawString("green: ", 0, 0);
	LCD.drawString("black: ", 0, 1);
	LCD.drawString("white: ", 0, 2);

	calibrate();
	leftturn();
	followP();
	
    }
    public static void main (String[] aArg) throws Exception {
	new GridAccuracy();
    }
}
