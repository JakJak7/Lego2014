import lejos.nxt.*;
/**
 * The locomotions of a  LEGO 9797 car is controlled by
 * sound detected through a microphone on port 1. 
 * 
 * @author  Ole Caprani
 * @version 23.08.07
 */
public class e5_Klap2 {

    private static SoundSensor sound = new SoundSensor(SensorPort.S1);
	
    public static void main(String [] args) throws Exception
    {
	Button.ESCAPE.addButtonListener(new ButtonListener() {
		public void buttonPressed(Button b) {
		    LCD.drawString("ENTER pressed", 0, 0);
		    System.exit(1);
		}

		public void buttonReleased(Button b) {
		    LCD.clear();
		}
	    });
	   	   
        int soundLevel;
	int low = 10;
	int high = 70;
        while (true) {
	    soundLevel = sound.readValue();
	    LCD.drawInt(soundLevel,4,10,0); 
	    if (soundLevel > low && soundLevel < high) {
		Thread.sleep(25);
		soundLevel = sound.readValue();
		if (soundLevel > high) {
		    Thread.sleep(150);
		    soundLevel = sound.readValue();
		    if (soundLevel > low && soundLevel < high) {
			Thread.sleep(100);
			soundLevel = sound.readValue();
			if (soundLevel < low) {
			    LCD.drawString("KLAP DETECTED!!", 0, 0);
			    Thread.sleep(1000);
			    LCD.clear();
			}
		    }
		}
	    }
	}
    }
}
