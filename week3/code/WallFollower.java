import lejos.nxt.*;

public class WallFollower{
    public static void main(String[] args) throws Exception {

     UltrasonicSensor us = new UltrasonicSensor(SensorPort.S4);
     final int  noObject = 255;
     int distance,
         desiredDistance = 35, // cm
         power, 
         minPower = 50,
	 dt = 30; //ms
     float error = 0.0f, prevError = 0.0f, derivative = 0.0f,
	 Kp = 10.0f,
	 Kd = 10.0f;
	  
     LCD.drawString("Distance: ", 0, 1);
     LCD.drawString("Power:    ", 0, 2);

	int tooClose = 25;
	int tooFar = 45;

     DataLogger dl = new DataLogger("Sample.txt");
     dl.start();

	while (! Button.ESCAPE.isDown())
	{		   
		distance = us.getDistance();
		
		if ( distance < 50 ) 
		{
			if( distance < 35 ){
				Car.forward(-50, 100);
				LCD.drawString("Too close ", 0, 3);
			} else {
				Car.forward(100, 100);
				LCD.drawString("Full speed! ", 0, 3);
			}
		}
		else {
			Car.forward(100, 85);
			LCD.drawString("Too far ", 0, 3);
		}
        LCD.drawInt(distance,4,10,1);
        
		dl.writeSample(distance);
		Thread.sleep(30);
	}
	
     dl.close();
     Car.stop();
     LCD.clear();
     LCD.drawString("Program stopped", 0, 0);
     Thread.sleep(2000);
 }
}
