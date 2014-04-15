import lejos.nxt.*;
import lejos.robotics.*;
import lejos.nxt.addon.*;
import java.lang.Math.*;

public class Racer1 {
    private final LightSensor l1 = new LightSensor(SensorPort.S1,true);
    private final LightSensor l2 = new LightSensor(SensorPort.S2,true);
    private final UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
    private final CompassHTSensor chs  = new CompassHTSensor(SensorPort.S4);
    private final DCMotor ml = new RCXMotor(MotorPort.A);
    private final DCMotor mr = new RCXMotor(MotorPort.B);
    private final int LEFT=1;
    private final int RIGHT=2;
    private long starttime;
    
    private void print() {
	LCD.drawString("Light: ", 0, 0); 
	LCD.drawString("Light: ", 0, 1);
	LCD.drawString("Ultra: ", 0, 2);
	LCD.drawString("Compa: ", 0, 3);
	LCD.drawString("Time:", 0, 4); 
	LCD.drawInt(getL(LEFT),4,10,0);
	LCD.drawInt(getL(RIGHT),4,10,1);
	LCD.drawInt(getU(),4,10,2);
	LCD.drawInt(getC(),4,10,3);
	LCD.drawInt((int) (System.currentTimeMillis()-starttime),4,10,4);
    }
    private int getU() {return us.getDistance();}
    private int getC() {return (int) chs.getDegrees();}
    private int getL(int i){return ((i==LEFT)?l1:l2).readValue();}
    private boolean isWhite(int i) {return getL(i)>45;}
    
    private boolean startzone() throws Exception {
	mr.forward();
	ml.backward();
	ml.setPower(100);
	mr.setPower(100);
	return true;
    }
    private boolean up() throws Exception {
	long starttimeu = System.currentTimeMillis()+2000;
	while (true) {
	    print();
	    if (starttimeu < System.currentTimeMillis() && getU() >= 11 && getU() != 255)
		return true;
	    else
		if (isWhite(LEFT) && isWhite(RIGHT)) {
		    ml.setPower(100);
		    mr.setPower(100);
		}
		else if (!isWhite(LEFT)) mr.setPower(70);
		else if (!isWhite(RIGHT)) ml.setPower(70);
	}
    }
    private boolean RightTurn() throws Exception {
	ml.setPower(-100);
	mr.setPower(100);

	while (getC() < 190 || getC() > 200) //210 //turn left - turn right.. to hit the exact degree
	    print();
	ml.setPower(100);
	return true;
    }

    public Racer1() throws Exception {
	starttime = System.currentTimeMillis();
	startzone();
	LCD.drawString("a", 0, 6); 
	up();
	LCD.drawString("bb", 0, 6); 
	RightTurn();
	ml.stop();
	mr.stop();
	LCD.drawString("ccc", 0, 6); 

	LCD.drawString("End time:", 0, 7); 
	LCD.drawInt((int) (System.currentTimeMillis()-starttime),4,10,7);

	while(true)
	    print();	
    }

    public static void main(String[] args)  throws Exception {
	Button.ESCAPE.addButtonListener(new ButtonListener() {
		public void buttonPressed(Button b) {
		    //playSound("FF1_victory_2.wav");
		    System.exit(1);
		}
		public void buttonReleased(Button b) {} 
	    });
	
	new Racer1();
    }
}

/*
Accelerate motor
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
*/
