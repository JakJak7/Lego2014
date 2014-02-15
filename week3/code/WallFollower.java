import lejos.nxt.*;

public class WallFollower{
    public static void main(String[] args) throws Execption {

     UltrasonicSensor us = new UltrasonicSensor(SensorPort.S4);
     final int  noObject = 255;
     int distance,
         desiredDistance = 35, // cm
         power, 
	 previousPower,
         minPower = 50,
	 dt = 30; //ms
     float error = 0.0f, prevError = 0.0f, derivative = 0.0f,
	 Kp = 10.0f,
	 Kd = 10.0f;
	  
     LCD.drawString("Distance: ", 0, 1);
     LCD.drawString("Power:    ", 0, 2);

     DataLogger dl = new DataLogger("Sample.txt");
     dl.start();

     while (! Button.ESCAPE.isDown())
     {		   
         distance = us.getDistance();
		 
         if ( distance != noObject ) 
         {
             error = distance - desiredDistance;
