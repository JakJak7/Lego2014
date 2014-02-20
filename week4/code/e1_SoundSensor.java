import lejos.nxt.*;

public class e1_SoundSensor {

   public static void main(String [] args) throws Exception {
	   
       //UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
       SoundSensor ss = new SoundSensor(SensorPort.S2);
       DataLogger dl = new DataLogger("w4.ex1.fixp.txt");
       dl.start();
       LCD.drawString("Sound Value: ", 0, 0);
	   
       int soundvalue = -1;
       while (! Button.ESCAPE.isDown())	{
	   soundvalue = ss.readValue();
           LCD.drawInt(soundvalue,3,13,0);
	   dl.writeSample(soundvalue);
	   Thread.sleep(1);
       }
       dl.close();
       LCD.clear();
       LCD.drawString("Program stopped", 0, 0);
       Thread.sleep(2000);
   }
}
