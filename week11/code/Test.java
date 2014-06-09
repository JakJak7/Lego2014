//package org.lejos.sample.bumpercar;
import lejos.nxt.*;
import lejos.nxt.Sound;
import lejos.robotics.RegulatedMotor;
import java.io.*;

public class Test {
    protected final RegulatedMotor ml = Motor.A;
    protected final RegulatedMotor mr = Motor.C;
    private final LightSensor light = new LightSensor(SensorPort.S1,true);
  
    public Test() {
	long s = System.currentTimeMillis();
	PlayBenny pb = new PlayBenny();
	MotorPort.A.controlMotor(100,2);
	MotorPort.B.controlMotor(100,2);
	MotorPort.C.controlMotor(100,2);
	pb.start();
	while (light.readValue() < 36){}
	while (true) {
	    while (light.readValue() > 36){
		if (System.currentTimeMillis()-s > 110000)
		    break;
		MotorPort.A.controlMotor(100,1);
		MotorPort.B.controlMotor(100,1);
	    }
	    if (System.currentTimeMillis()-s > 110000)
		break;
	    MotorPort.A.controlMotor(100,3);
	    MotorPort.B.controlMotor(100,3);
	}
	MotorPort.A.controlMotor(100,1);
	MotorPort.B.controlMotor(100,1);
	while(true){}
    }
    public static void main(String[] args) throws Exception {
	Thread.sleep(3000); //3000
	new Test();
    }
}
class PlayBenny extends Thread {
    File f = new File("benny_ill.wav");
    public void run() {
	while (true) {
	    Sound.playSample(f,100);
	    try {Thread.sleep(14500);}
	    catch(Exception e){}
	}
    }
}