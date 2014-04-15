import lejos.nxt.*;
import lejos.nxt.addon.CompassHTSensor;

public class DegreeTest {
    public static void main(String[] args)  throws Exception {
	CompassHTSensor chs  = new CompassHTSensor(SensorPort.S4);

	while (true) {
	    LCD.drawString("Degree: ", 0, 0); 
	    LCD.drawInt((int) chs.getDegrees(),4,10,0);
	}

    }
}