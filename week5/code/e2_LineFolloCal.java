import lejos.nxt.*;

public class e2_LineFolloCal
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

	BlackWhiteSensor sensor = new BlackWhiteSensor(SensorPort.S1);
	 
	sensor.calibrate();

	LCD.clear();
	LCD.drawString("Light: ", 0, 2); 

	while (! Button.ESCAPE.isDown()) {
	    LCD.drawInt(sensor.light(),4,10,2);
	    LCD.refresh();
	     
	    if (sensor.black())
		Car.forward(power, 0);
	    else
		Car.forward(0, power);
	     
	    Thread.sleep(10);
	}

	Car.stop();
	LCD.clear();
	LCD.drawString("Program stopped", 0, 0);
	LCD.refresh();
    }
}
