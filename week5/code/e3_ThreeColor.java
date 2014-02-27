import lejos.nxt.*;

public class e3_ThreeColor
{
    public static void main (String[] aArg) throws Exception {
	final int power = 80;
	
	Button.ESCAPE.addButtonListener(new ButtonListener() {
		public void buttonPressed(Button b) {
		    LCD.drawString("ENTER pressed", 0, 0);
		    System.exit(1);
		}

		public void buttonReleased(Button b) {
		    LCD.clear();
		}
	    });

	ThreeColorSensor sensor = new ThreeColorSensor(SensorPort.S1);
	 
	sensor.calibrate();
	LCD.clear();
	while (true) {
	    String s = "White";
	    if (sensor.black())
		s = "Black";
	    else if (sensor.green())
		s = "Green";
	    LCD.drawString(s, 0, 0);
	    Thread.sleep(1000);
	}
    }
}
