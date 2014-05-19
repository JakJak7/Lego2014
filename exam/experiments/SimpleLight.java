import lejos.nxt.*;
import java.io.*;
import lejos.robotics.Color;
import lejos.nxt.ADSensorPort;
/**
   Simple program for LeJos NXT
   for reading lightvalue to screen and
   by pressing enter writing the values to a file
  
   @author Henrik K. Christensen
   @date 19. maj 2014
*/
public class SimpleLight {
    private static LightSensor l1 = new LightSensor(SensorPort.S1,false);
    private static LightSensor l2 = new LightSensor(SensorPort.S2,false);
    private static ColorSensor c3 = new ColorSensor(SensorPort.S3);
    private static ColorSensor c4 = new ColorSensor(SensorPort.S4);

    private static File f;
    private static FileOutputStream fos;
    public static void writeS(String s) throws Exception {
	for(int i=0; i<s.length(); i++)
	    fos.write((byte)s.charAt(i));
    }
    public static void main (String[] aArg) throws Exception {

	f = new File("SimpleLightMes.txt");
	if (f.exists()) f.delete();
	f.createNewFile();
	fos = new FileOutputStream(f);
	LCD.drawString("Press..",0,1);
	LCD.drawString("LEFT/RIGHT:", 0, 2);
	LCD.drawString("for light/or not", 0, 3);
	LCD.drawString("ENTER: to save.", 0, 4);
	LCD.drawString("LEFT/RIGHT", 0, 5);
	LCD.drawString("Continue.", 0, 5);


	while (!Button.LEFT.isDown() && !Button.RIGHT.isDown() && !Button.ESCAPE.isDown());
	LCD.clear();
	writeS("LightSensor #1: readValue() | readNormalizedValue() | #2. | #2.");
	writeS("ColorSensor #3: r | g | b | #4 | #4 | #4\n");
	while (!Button.ESCAPE.isDown()) {
	    if (Button.LEFT.isDown()) {
		l1.setFloodlight(true);
		l2.setFloodlight(true);
		c3.setFloodlight(Color.WHITE);
		c4.setFloodlight(Color.WHITE);
	    }
	    else if (Button.RIGHT.isDown()) {
		l1.setFloodlight(false);
		l2.setFloodlight(false);
		c3.setFloodlight(Color.WHITE);
		c4.setFloodlight(Color.WHITE);
	    }
	    ColorSensor.Color c3r = c3.getRawColor();
	    ColorSensor.Color c4r = c4.getRawColor();
	    if (Button.ENTER.isDown()) {
		writeS(l1.readValue()+"\t"+l1.readNormalizedValue()+'\t');
		writeS(l2.readValue()+"\t"+l2.readNormalizedValue()+'\t');
		writeS(c3r.getRed()+"|"+c3r.getGreen()+"|"+c3r.getBlue()+'\t');
		writeS(c4r.getRed()+"|"+c4r.getGreen()+"|"+c4r.getBlue()+'\t');
		writeS("\n");
		while (Button.ENTER.isDown());
	    }
	    LCD.drawString("Light1:", 0, 0);
	    LCD.drawString(":", 9, 0);
	    LCD.drawString("Light2:", 0, 1);
	    LCD.drawString(":", 9, 1);
	    LCD.drawString("C3:", 0, 2);
	    LCD.drawString(":", 9, 2);
	    LCD.drawString("C4:", 0, 3);
	    LCD.drawString(":", 9, 3);
	    LCD.drawInt(l1.readValue(), 2, 7, 0);
	    LCD.drawInt(l1.readNormalizedValue(), 3, 10, 0);
	    LCD.drawInt(l2.readValue(), 2, 7, 1);
	    LCD.drawInt(l2.readNormalizedValue(), 3, 10, 1);
	    LCD.drawInt(c3r.getRed(), 2, 4, 2);
	    LCD.drawInt(c3r.getGreen(), 2, 9, 2);
	    LCD.drawInt(c3r.getBlue(), 2, 13, 2);
	    LCD.drawInt(c4r.getRed(), 2, 4, 3);
	    LCD.drawInt(c4r.getGreen(), 2, 9, 3);
	    LCD.drawInt(c4r.getBlue(), 2, 13, 3);
	}
	fos.close();
    }
}

