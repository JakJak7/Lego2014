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
    private int speed = 600;

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
	else if (value == 0) m.controlMotor(value,3);
    }

    public void calibrate() throws Exception {
	Motor.A.setSpeed(speed);
	Motor.B.setSpeed(speed);
	Motor.A.forward();
	Motor.B.forward();

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
	Motor.A.stop();
	Motor.B.stop();
    }
    public void turn() {
	float wheelSize = 5.6f;
	float width = 20.1f;
	double degrees = 90*width/wheelSize*1.4;

	Motor.A.rotate((int)degrees,true);
	Motor.B.rotate(-(int)degrees);
    }
    public void followP() {
	float Kp = 1f;
	int Tp = 60;
	int offset = (int) (white+black)/2;
	//	offset = (int) (611+670)/2;
	while (! Button.ESCAPE.isDown()) {
	    int error = light() - offset;
	    int Turn = (int) (Kp*error);
	    controlMotor(MP1,(Tp+Turn));
	    controlMotor(MP2,(Tp-Turn));
	}
    }
    public void turnSolar() {
	grap();
	Motor.A.rotate(90,true);
	Motor.B.rotate(90);
	turn();
	turn();
	Motor.A.rotate(-200,true);
	Motor.B.rotate(-200);
	release();
	Motor.A.rotate(-330,true);
	Motor.B.rotate(-330);
	//  !!! RUN !!
	grap();
	Motor.A.rotate(4*330,true);
	Motor.B.rotate(4*330);
	release();
    }
    public FirstDriver() throws Exception {
	M3.rotateTo(-270,true);
	LCD.drawString("green: ", 0, 0);
	LCD.drawString("black: ", 0, 1);
	LCD.drawString("white: ", 0, 2);
	//turnSolar();
	calibrate();
	turn();
	
	followP();
    }
    public static void main (String[] aArg) throws Exception {
	new FirstDriver();
    }
}
