import lejos.nxt.*;
import java.io.*;
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
    private static LightSensor l3 = new LightSensor(SensorPort.S3,false);
    private static LightSensor l4 = new LightSensor(SensorPort.S4,false);
    private static File f;
    private static FileOutputStream fos;
    public static void writeS(String s) {
	try {
            for(int i=0; i<s.length(); i++)
		fos.write((byte)s.charAt(i));
	}
	catch(Exception e){}
    }
    public static void main (String[] aArg) throws Exception {
	f = new File("SimpleLightMes.txt");
	try {
	    if (f.exists()) f.delete();
	    f.createNewFile();
	}catch (Exception e){}
	fos = new FileOutputStream(f);
	LCD.drawString("Press..",0,1);
	LCD.drawString("LEFT/RIGHT:", 0, 2);
	LCD.drawString("for light/or not", 0, 3);
	LCD.drawString("ENTER: to save.", 0, 4);
	LCD.drawString("LEFT/RIGHT", 0, 5);
	LCD.drawString("Continue.", 0, 5);


	while (!Button.LEFT.isDown() && !Button.RIGHT.isDown() && !Button.ESCAPE.isDown());
	LCD.clear();
	writeS("v1\tr1\tv2\tr2\tv3\tr3\tv4\tr4\n");
	while (!Button.ESCAPE.isDown()) {
	    if (Button.LEFT.isDown()) {
		l1.setFloodlight(true);
		l2.setFloodlight(true);
		l3.setFloodlight(true);
		l4.setFloodlight(true);
	    }
	    else if (Button.RIGHT.isDown()) {
		l1.setFloodlight(false);
		l2.setFloodlight(false);
		l3.setFloodlight(false);
		l4.setFloodlight(false);
	    }
	    if (Button.ENTER.isDown()) {
		writeS(l1.readValue()+"\t"+l1.readNormalizedValue()+'\t');
		writeS(l2.readValue()+"\t"+l2.readNormalizedValue()+'\t');
		writeS(l3.readValue()+"\t"+l3.readNormalizedValue()+'\t');
		writeS(l4.readValue()+"\t"+l4.readNormalizedValue()+'\n');
		while (Button.ENTER.isDown());
	    }
	    LCD.drawString("Light1:", 0, 0);
	    LCD.drawString("Light2:", 0, 1);
	    LCD.drawString("Light3:", 0, 2);
	    LCD.drawString("Light4:", 0, 3);
	    LCD.drawString(":", 9, 0);
	    LCD.drawString(":", 9, 1);
	    LCD.drawString(":", 9, 2);
	    LCD.drawString(":", 9, 3);
	    LCD.drawInt(l1.readValue(), 2, 7, 0);
	    LCD.drawInt(l1.readNormalizedValue(), 3, 10, 0);
	    LCD.drawInt(l2.readValue(), 2, 7, 1);
	    LCD.drawInt(l2.readNormalizedValue(), 3, 10, 1);
	    LCD.drawInt(l3.readValue(), 2, 7, 2);
	    LCD.drawInt(l3.readNormalizedValue(), 3, 10, 2);
	    LCD.drawInt(l4.readValue(), 2, 7, 3);
	    LCD.drawInt(l4.readNormalizedValue(), 3, 10, 3);
	}
	try {
	    fos.close();
	}
	catch (Exception e){}
    }
}

