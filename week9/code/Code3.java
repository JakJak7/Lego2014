import lejos.nxt.*;
import lejos.robotics.Color;
import lejos.robotics.*;
import lejos.nxt.addon.*;
import java.lang.Math.*;

public class Code3 {
    private final LightSensor l1 = new LightSensor(SensorPort.S1,true);
    private final LightSensor l2 = new LightSensor(SensorPort.S2,true);
    private final ColorSensor cs = new ColorSensor(SensorPort.S3);
    private final DCMotor ml = new RCXMotor(MotorPort.A);
    private final DCMotor mr = new RCXMotor(MotorPort.B);
    private final UltrasonicSensor us = new UltrasonicSensor(SensorPort.S4);
    private void print() {
	LCD.drawString("Light: ", 0, 0); 
	LCD.drawString("Color: ", 0, 1); 
	LCD.drawString("Light: ", 0, 2); 
	LCD.drawString("Ultra: ", 0, 3); 
	LCD.drawInt(getL(1),4,10,0);
	LCD.drawInt(getC() ,4,5,1);
	LCD.drawInt(getCL() ,4,10,1);
	LCD.drawInt(getL(2),4,10,2);
	LCD.drawInt(us.getDistance(),4,10,3);
    }
    private int getC() {return cs.getColor().getColor();}//1 = green, 7 = black, 6 = white?
    private int getCL() {return cs.getNormalizedLightValue();}
    private int getL(int i){return ((i==1)?l1:l2).readValue();} // white: 35< sort: <35 lav
    private boolean isWhite(int i) {return getL(i) > 42;}
    private DCMotorControl DCMC = new DCMotorControl();


    private boolean startzone() throws Exception {
	ml.forward();
	mr.forward();

	DCMC.acc(ml,mr,0,100,30);
	while (getC() == 1)
	    print();

	return true;
    }
    private boolean up() throws Exception {
	while (true) {
	    print();
	    if (us.getDistance() >= 11)
		return true;
	    else {
		if (getC() == 7 || (isWhite(1) && isWhite(2))) {
		    ml.setPower(100);
		    mr.setPower(100);
		}
		if (!isWhite(1))
		    mr.setPower(20);
		if (!isWhite(2))
		    ml.setPower(20);
	    }
	}
    }
    private boolean RightTurn() throws Exception {
	ml.setPower(100);
	mr.setPower(-100);
	Thread.sleep(250);
	
	while (isWhite(1) && isWhite(2)) {}
	ml.stop();
	mr.stop();
	return true;
	
    }

    public Code3() throws Exception {
	cs.setFloodlight(Color.WHITE);

	startzone();
	LCD.drawString("startzone done", 0, 5); 
	up();
	LCD.drawString("niveau 1 done", 0, 5); 
	RightTurn();
	LCD.drawString("rightturn done", 0, 5); 
	//up();
	LCD.drawString("niveau 2 done", 0, 5); 	
	while(true){
	    print();
	}
	
    }

    public static void main(String[] args)  throws Exception {
	Button.ESCAPE.addButtonListener(new ButtonListener() {
		public void buttonPressed(Button b) {
		    //playSound("FF1_victory_2.wav");
		    System.exit(1);
		}
		public void buttonReleased(Button b) {} 
	    });
	
	new Code3();

    }
}

class DCMotorControl extends Thread {
    protected void acc(DCMotor m, int start, int end, int step) throws Exception {
	m.setPower(start);
	if (start < end)
	    for(int i = 1;i <= step; i++) {
		Thread.sleep(5);
		m.setPower(start+(end-start)/step*i);
	    }
	else 
	    for(int i = 1;i <= step; i++) {
		Thread.sleep(5);
		m.setPower(start+(end-start)/step*i);
	    }
	m.setPower(end);	
    }
    protected void acc(DCMotor m1,DCMotor m2, int start, int end, int step) throws Exception {
	m1.setPower(start);
	m2.setPower(start);
	if (start < end)
	    for(int i = 1;i <= step; i++) {
		Thread.sleep(5);
		m1.setPower(start+(end-start)/step*i);
		m2.setPower(start+(end-start)/step*i);
	    }
	else 
	    for(int i = 1;i <= step; i++) {
		Thread.sleep(5);
		m1.setPower(start+(end-start)/step*i);
		m2.setPower(start+(end-start)/step*i);
	    }
	m1.setPower(end);
	m2.setPower(end);
    }
}