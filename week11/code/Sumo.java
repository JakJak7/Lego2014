//package org.lejos.sample.bumpercar;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
//import lejos.robotics.MirrorMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * Demonstration of the Behavior subsumption classes.
 * 
 * Requires a wheeled vehicle with two independently controlled
 * motors connected to motor ports A and C, and 
 * a touch sensor connected to sensor  port 1 and
 * an ultrasonic sensor connected to port 3;
 * 
 * @author Brian Bagnall and Lawrie Griffiths, modified by Roger Glassey
 *
 */
public class Sumo {
    public static void main(String[] args) {
	MotorPort.A.controlMotor(100,1);
	MotorPort.B.controlMotor(100,1);
	MotorPort.C.controlMotor(100,1);
	mc.forward();


	//Behavior b1 = new DriveForward();
	//Behavior b2 = new DetectWall();
	/*	Behavior b3 = new StayinBlack();

	Behavior[] behaviorList = {b3};
	Arbitrator arbitrator = new Arbitrator(behaviorList);
	Button.waitForAnyPress();
	arbitrator.start();
	*/
    }
}
class Black implements Behavior {
    public boolean takeControl() {
	return Button.ESCAPE.isDown();
    }
    
    public void suppress() {
	// No need to supress, the action is to terminate the program..
    }

    public void action() {
	System.exit(0);
    }
}
s