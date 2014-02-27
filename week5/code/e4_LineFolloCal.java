import lejos.nxt.*;

public class e4_LineFolloCal
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
	LCD.drawString("Light: ", 0, 2); 

	while (! Button.ESCAPE.isDown()) {
	    LCD.drawInt(sensor.light(),4,10,2);
	    LCD.refresh();
	     
	    if (sensor.black() || sensor.white())
		Car.forward(power, power);
	    else
		Car.stop();
	    Thread.sleep(10);
	}

	Car.stop();
	LCD.clear();
	LCD.drawString("Program stopped", 0, 0);
	LCD.refresh();
    }
}
