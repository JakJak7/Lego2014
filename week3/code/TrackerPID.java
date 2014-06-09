import lejos.nxt.*;
/**
 * A LEGO 9797 car with a sonar sensor. The sonar is used
 * to maintain the car at a constant distance 
 * to objects in front of the car.
 * 
 * The sonar sensor should be connected to port 1. The
 * left motor should be connected to port C and the right 
 * motor to port B.
 * 
 * @author  Ole Caprani
 * @version 24.08.08
 */
public class TrackerPID
{
  public static void main (String[] aArg)
  throws Exception
  {
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
	     derivative = (error - prevError)/dt;
             power = (int)(Kp * error) + (int)(Kd * derivative);
             if ( error > 0 )
             { 
                 power = Math.min(minPower + power,100);
                 Car.forward(power,power);
                 LCD.drawString("Forward ", 0, 3);
             }
             else 
             {
                 power = Math.min(minPower + Math.abs(power),100);
                 Car.backward(power, power);
		         LCD.drawString("Backward", 0, 3);
		    	 
             }
             LCD.drawInt(distance,4,10,1);
             LCD.drawInt(power, 4,10,2);
	 }
         else
             Car.forward(100, 100);
	 dl.writeSample(distance);
	 prevError = error;
	 //previousPower = power;
         Thread.sleep(dt);
     }
	 
     dl.close();
     Car.stop();
     LCD.clear();
     LCD.drawString("Program stopped", 0, 0);
     Thread.sleep(2000);
   }
}
