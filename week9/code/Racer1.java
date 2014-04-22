import lejos.nxt.*;
import lejos.nxt.Sound;
import lejos.robotics.*;
import lejos.nxt.addon.*;
import java.lang.Math.*;
import java.io.*;

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
    private int whitevalue = 42;

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
    private boolean isWhite(int i) {return getL(i)>whitevalue;}
    private int max=100;
    
    private boolean startzone() throws Exception {
	ml.forward();
	mr.backward();
	ml.setPower(max);
	mr.setPower(max);
	return true;
    }
    private boolean up() throws Exception {
	long timeout = System.currentTimeMillis()+2000;
	while (true) {
	    print();
	    if (timeout < System.currentTimeMillis() && getU() >= 10 && getU() != 255) {
		while (getU() >= 10){}
		return true;
	    }
	    else
		if (isWhite(LEFT) && isWhite(RIGHT)) {
		    ml.setPower(max);
		    mr.setPower(max);
		}
		else if (!isWhite(LEFT) && !isWhite(RIGHT)) {
		    mr.setPower(max);
		    ml.setPower(max);
		}
		else if (!isWhite(LEFT)) {
		    mr.setPower(max);
		    ml.setPower(75);
		}
		else if (!isWhite(RIGHT)){
		    ml.setPower(max);
		    mr.setPower(75);
		}
	}
    }
    private boolean down() throws Exception {
	long timeout = System.currentTimeMillis()+1000;
	max = 90;
	while (true) {
	    print();
	    if (timeout < System.currentTimeMillis() && getU() <= 8) {
		while (getU() <= 8){}
		return true;
	    }
	    else
		if (isWhite(LEFT) && isWhite(RIGHT)) {
		    ml.setPower(max);
		    mr.setPower(max);
		}
		else if (!isWhite(LEFT)) {
		    mr.setPower(max);
		    ml.setPower(max);
		}
		else if (!isWhite(RIGHT)){
		    ml.setPower(max);
		    mr.setPower(max);
		}
	}
    }
    private boolean Turn(int target,int deviation) throws Exception {
	int p = max;

	while (true) {
	    print();
	    int angle = (target-getC())%360;

	    if (angle > 180)
		angle = angle-360;

	    if (angle < -deviation) {
		ml.setPower(-p);
		mr.setPower(p);
	    }
	    else if (angle > deviation) {
		ml.setPower(p);
		mr.setPower(-p);
	    }
	    else {
		ml.setPower(0);
		mr.setPower(0);
		Thread.sleep(30);
		angle = (target-getC())%360;
		p-=2;
		if (angle > -deviation && angle < deviation)
		    break;
	    }
	}
	ml.setPower(max);
	mr.setPower(max);
	return true;
    }

    private boolean GetPos(int side) {
	ml.setPower(max);
	mr.setPower(max);
	int c = 0;
	while (c < 2) {
	    if (!isWhite(side)) {
		c++;
		Sound.beep();
		if (c == 2)
		    break;
	    }
	    while(!isWhite(side));
	    while(isWhite(side));
	}
	return true;
    }

    public Racer1() throws Exception {
	starttime = System.currentTimeMillis();
	startzone();
	up();
	Turn(220,4);
	GetPos(RIGHT);
	Turn(240,3);
	up();
	Turn(100,4);
	GetPos(LEFT);
	Turn(352,4);
	up();
	Turn(222,2);
	down();
	
	ml.stop();
	mr.stop();
	LCD.drawString("End time:", 0, 7); 
	LCD.drawInt((int) (System.currentTimeMillis()-starttime),4,10,7);

	File f = new File("FF1_Victory_2.wav");
	Sound.playSample(f);

	while(true)
	    print();	
    }

    public static void main(String[] args)  throws Exception {
	Button.ESCAPE.addButtonListener(new ButtonListener() {
		public void buttonPressed(Button b) {
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
