import lejos.nxt.*;
import java.io.*;

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
    private int power = -85;

    public void turnright() {
	controlMotor(MotorPort.B,-power);
	controlMotor(MotorPort.C,power);
	while(!Button.ESCAPE.isDown())
	    if (isblack(2))
		break;
    }
    public void turnleft() {
	controlMotor(MotorPort.B,power);
	controlMotor(MotorPort.C,-power);
	while(isblack(1));
	while(!Button.ESCAPE.isDown())
	    if (isblack(1))
		break;
    }
    public void follow() throws Exception{
	white = white-50;
	while (!Button.ESCAPE.isDown()) {
	    if (rnv(1) >= white && rnv(2) >= white) {
		controlMotor(MotorPort.B,power);
		controlMotor(MotorPort.C,power);
	    }
	    else if (rnv(1) >= white && rnv(2) < white) {
		controlMotor(MotorPort.B,power);
		controlMotor(MotorPort.C,0);
	    }

	    else if (rnv(1) < white && rnv(2) >= white) {
		controlMotor(MotorPort.B,0);
		controlMotor(MotorPort.C,power);
	    }
	    else if (rnv(1) < white && rnv(2) < white) {
		controlMotor(MotorPort.B,0);
		controlMotor(MotorPort.C,0);
		//break;
	    }
	    
	    LCD.drawInt(white, 2, 7, 2);
	    LCD.drawInt(rnv(1), 2, 7, 4);
	    LCD.drawInt(rnv(2), 2, 7, 5);
	    LCD.drawInt(cal, 2, 7, 6);
	    LCD.drawInt((rnv(1))-(rnv(2)), 2, 7, 7);
	    Thread.sleep(20);
	}
    }
    public GridAccuracy() throws Exception {
	controlMotor(MotorPort.B,power);
	controlMotor(MotorPort.C,power);

	LCD.drawString("green: ", 0, 0);
	LCD.drawString("black: ", 0, 1);
	LCD.drawString("white: ", 0, 2);

	calibrate();
	turnright();
	follow();
	turnright();
	follow();
    }
    public static void main (String[] aArg) throws Exception {
	new GridAccuracy();
    }
}
