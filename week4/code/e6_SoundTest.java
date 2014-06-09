import lejos.nxt.*;
/**
   Læser lyd input fra port 1 og port 2 (to sensors) .. og gemmer 
   værdierne i datalogger, så det er til at lave en graf over
   værdierne og derefter anslå hvor meget forskelligt de måler.
 */
public class e6_SoundTest 
{
    private static SoundSensor sound1 = new SoundSensor(SensorPort.S1);
    private static SoundSensor sound2 = new SoundSensor(SensorPort.S2);
    private static DataLogger dl1 = new DataLogger("w6.m1.s");
    private static DataLogger dl2 = new DataLogger("w6.m2.s");
    public static void main(String [] args) throws Exception
    {
	Button.ESCAPE.addButtonListener(new ButtonListener() {
		public void buttonPressed(Button b) {
		    LCD.drawString("ESCAPE pressed", 0, 0);
		    System.exit(1);
		}
		
		public void buttonReleased(Button b) {
		    LCD.clear();
		}
	    });

        LCD.drawString("dB level: ",0,0);
        LCD.refresh();
	int soundLevel1,soundLevel2;
	dl1.start();
	dl2.start();
	while (! Button.ESCAPE.isDown()) {
	    soundLevel1 = sound1.readValue();
	    soundLevel2 = sound2.readValue();
	    LCD.drawInt(soundLevel1,4,10,0); 
	    LCD.drawInt(soundLevel2,4,10,1); 
	    dl1.writeSample(soundLevel1);
	    dl2.writeSample(soundLevel2);
	    Thread.sleep(10);
	}
	dl1.close();
	dl2.close();
	LCD.clear();
	LCD.drawString("Program stopped", 0, 0);
	Thread.sleep(2000);
    }
}
