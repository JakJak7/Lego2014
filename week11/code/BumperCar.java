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
public class BumperCar {
    protected static RegulatedMotor leftMotor = Motor.A;
    protected static RegulatedMotor rightMotor = Motor.C;
  
    public static void main(String[] args) {
	leftMotor.setSpeed(400);
	rightMotor.setSpeed(400);
	Behavior b1 = new DriveForward();
	Behavior b2 = new DetectWall();
	Behavior b3 = new ExitBehavior();

	Behavior[] behaviorList = {b1, b2, b3};
	Arbitrator arbitrator = new Arbitrator(behaviorList);
	LCD.drawString("Bumper Car",0,1);
	Button.waitForAnyPress();
	arbitrator.start();

    }
}
class UltraSonicStore extends Thread {
    private int distance;
    private UltrasonicSensor sonar = new UltrasonicSensor(SensorPort.S3);

    public void run() {
	while (true) {
	    sonar.ping();    
	    distance = sonar.getDistance();
	}
    }
    public int getDistance() {
	return distance;
    }
}

class ExitBehavior implements Behavior {
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

class DriveForward implements Behavior {
    private boolean _suppressed = false;

    public boolean takeControl() {
	return true;  // this behavior always wants control.
    }
    
    public void suppress() {
	_suppressed = true;// standard practice for suppress methods
    }

    public void action() {
	_suppressed = false;
	BumperCar.leftMotor.forward();
	BumperCar.rightMotor.forward();
	while (!_suppressed) {
	    Thread.yield(); //don't exit till suppressed
	}
	BumperCar.leftMotor.stop(); 
	BumperCar.leftMotor.stop();
    }
}


class DetectWall implements Behavior {
    private boolean _suppressed = false;
    private UltraSonicStore uss;
    private TouchSensor touch;

    public DetectWall() {
	touch = new TouchSensor(SensorPort.S1);
	uss = new UltraSonicStore();
	uss.start();
    }

    public boolean takeControl() {
	return touch.isPressed() || uss.getDistance() < 25;
    }

    public void suppress() {
	//Since  this is highest priority behavior, suppress will never be called.
    }

    public void action() {
	BumperCar.leftMotor.rotate(-180, true);// start Motor.A rotating backward
	BumperCar.rightMotor.rotate(-360);  // rotate C farther to make the turn
    }
}
