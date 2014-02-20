import lejos.nxt.*;
/**
 * The locomotions of a  LEGO 9797 car is controlled by
 * sound detected through a microphone on port 1. 
 * 
 * @author  Ole Caprani
 * @version 23.08.07
 */
public class e6_SoundTest2
{
    private static SoundSensor sound1 = new SoundSensor(SensorPort.S1);
    private static SoundSensor sound2 = new SoundSensor(SensorPort.S2);
    private static DataLogger dl1 = new DataLogger("w6.2.m1.s");
    private static DataLogger dl2 = new DataLogger("w6.2.m2.s");
    private static int abs(int i) {
	return (i < 0)?-i:i;
    }
    public static void main(String [] args) throws Exception
    {
	Button.ESCAPE.addButtonListener(new ButtonListener() {
		public void buttonPressed(Button b) {
		    LCD.drawString("ENTER pressed", 0, 0);
		    Car.stop();
		    System.exit(1);
		}
		
		public void buttonReleased(Button b) {
		    LCD.clear();
		}
	    });

        LCD.drawString("dB level: ",0,0);
        LCD.refresh();
	int soundLevel1 = sound1.readValue();
	int soundLevel2 = sound2.readValue();
	int ab = abs(soundLevel1-soundLevel2)/2;
	ab = (soundLevel1 < soundLevel2)?ab:-ab;
	dl1.start();
	dl2.start();
	while (! Button.ESCAPE.isDown()) {
	    soundLevel1 = sound1.readValue()-ab;
	    soundLevel2 = sound2.readValue()-ab;
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
