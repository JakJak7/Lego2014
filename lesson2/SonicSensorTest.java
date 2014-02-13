import lejos.nxt.*;
/**
 * A simple sonar sensor test program.
 * 
 * The sensor should be connected to port 1. In the
 * known bugs and limitations of leJOS NXJ version alfa_03
 * it is mentioned that a gap of at least 300 msec is 
 * needed between calls of getDistance. This is the reason 
 * for the delay of 300 msec between sonar readings in the 
 * loop.
 * 
 * @author  Ole Caprani
 * @version 30.08.07
 */
public class SonicSensorTest 
{

   public static void main(String [] args)  
   throws Exception 
   {
	   
       UltrasonicSensor us = new UltrasonicSensor(SensorPort.S4);
       
       //DataLogger dl = new DataLogger("Sample.txt");
       DataLogger dl = new DataLogger("Sample.txt");
       int distance = 0;

       LCD.drawString("Distance(cm) ", 0, 0);
	   
		dl.start();
		
       while (! Button.ESCAPE.isDown())
       {
	   //distance = us.getDistance();
           //LCD.drawInt(distance,3,13,0);
	   
			dl.writeSample(distance);
			//Thread.sleep(1);
       }
       LCD.clear();
       LCD.drawString("Program stopped", 0, 0);
       dl.close();
       Thread.sleep(2000);
   }
}
