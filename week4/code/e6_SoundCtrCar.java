import lejos.nxt.*;
/**
 * The locomotions of a  LEGO 9797 car is controlled by
 * sound detected through a microphone on port 1. 
 * 
 * @author  Ole Caprani
 * @version 23.08.07
 */
public class e6_SoundCtrCar 
{
    private static SoundSensor sound1 = new SoundSensor(SensorPort.S1);
    private static SoundSensor sound2 = new SoundSensor(SensorPort.S2);
	
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

        LCD.drawString("Party!!: ",0,0);
        LCD.refresh();
	int soundLevel1,soundLevel2;
        while (! Button.ESCAPE.isDown()) {
	    soundLevel1 = (sound1.readValue()-3)*3;
	    soundLevel2 = (sound2.readValue()-3)*3;
	    LCD.drawInt(soundLevel1,4,10,0); 
	    LCD.drawInt(soundLevel2,4,10,1);
	    /*if (soundLevel1 < 16)
		soundLevel1 = 0;
	    if (soundLevel2 < 16)
		soundLevel2 = 0;
	    */
	    Car.forward(soundLevel1, soundLevel2);
       }
       Car.stop();
       LCD.clear();
       LCD.drawString("Program stopped", 0, 0);
       Thread.sleep(2000); 
   }
}
