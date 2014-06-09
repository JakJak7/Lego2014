package org.lejos.sample.segwaypilotdemo;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroSensor;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.SegowayPilot;

/*import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.robotics.navigation.Segoway;
*/
public class e4_Sejway implements MoveListener {
	
    public static void main(String [] args) throws InterruptedException {
	NXTMotor left = new NXTMotor(MotorPort.B);
	NXTMotor right = new NXTMotor(MotorPort.C);
		
	GyroSensor g = new GyroSensor(SensorPort.S1);
				
	// The track width is for the AnyWay. Make sure to use the appropriate wheel size.
	SegowayPilot pilot = new SegowayPilot(left, right, g, SegowayPilot.WHEEL_SIZE_NXT2, 10.45); 
		
	// If the robot is tippy, try slowing down the speed:
	pilot.setTravelSpeed(80);
		
	MoveListener listy = new e4_Sejway();
	pilot.addMoveListener(listy);
		
	// Draw three squares
	for(int i=0;i<12;i++) {
	    pilot.travel(50);
	    pilot.rotate(90);
	    Thread.sleep(2000);
	}
    }

    public void moveStarted(Move move, MoveProvider mp) {
	System.out.println("MOVE STARTED");
    }

    public void moveStopped(Move move, MoveProvider mp) {
	System.out.println("DISTANCE " + move.getDistanceTraveled());
	System.out.println("ANGLE " + move.getAngleTurned());
    }	    

    /*public static void main(String[] args) throws Exception{
      GyroSensor g = new GyroSensor(SensorPort.S1);
      NXTMotor m1 = new NXTMotor(MotorPort.B);
      NXTMotor m2 = new NXTMotor(MotorPort.C);
      Segoway seq = new Segoway(m1,m2,g,(double) 56.0);

      seq.run();
      //Thread.sleep(30*1000);
      }
    */
}