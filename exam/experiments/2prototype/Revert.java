import lejos.nxt.*;
import java.io.*;
import java.lang.Math;

public class Revert {
    private LightSensor sensor = new LightSensor(SensorPort.S1,true);
    private NXTRegulatedMotor M3 = Motor.C;
    private MotorPort MP1 = MotorPort.A, MP2 = MotorPort.B;
    private int green = -1;
    private int black = 1024;
    private int white = -1;
    private int power = 50;

    public int light() {
	return sensor.readNormalizedValue();
    }
    public void grap() {
	M3.rotateTo(-270);
    }
    public void release() {
	M3.rotateTo(0);
    }
    public static void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3); //3 stop
    }

    public void calibrate() throws Exception {
	controlMotor(MP1,power);
	controlMotor(MP2,power);
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
    public void turn() {
	MP1.resetTachoCount();
	controlMotor(MP1,-power);
	controlMotor(MP2,power);
	while (MP1.getTachoCount()>-300);
	controlMotor(MP1,0);
	controlMotor(MP2,0);
    }
    public void move(int i) {
	MP1.resetTachoCount();
	int d = (i<0)?-1:1;
	controlMotor(MP1,power*d);
	controlMotor(MP2,power*d);
	while (d*MP1.getTachoCount()<i*d);
	controlMotor(MP1,0);
	controlMotor(MP2,0);
    }
    public void followP() {
	float Kp = 1f;
	int Tp = 50;
	int offset = (int) ((float) white/4+ (float) black/4*3);
	//	offset = (int) (611+670)/2;
	while (! Button.ESCAPE.isDown()) {
	    int error = light() - offset;
	    int Turn = (int) (Kp*error);
	    controlMotor(MP1,-(Tp+Turn));
	    controlMotor(MP2,-(Tp-Turn));
	}
    }
    public void turnSolar() {
	grap();
	move(200);
	turn();
	turn();
	move(-30);
	release();
	move(-200);
	grap();
	move(2*800);
	release();
    }
    public Revert() throws Exception {
	LCD.drawString("green: ", 0, 0);
	LCD.drawString("black: ", 0, 1);
	LCD.drawString("white: ", 0, 2);

	//turnSolar();
	
	calibrate();
	move(200);
	turn();
	followP();
    }
    public static void main (String[] aArg) throws Exception {
	new Revert();
    }
}
