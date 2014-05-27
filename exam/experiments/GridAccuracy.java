import lejos.nxt.*;
import java.io.*;

public class GridAccuracy {
    private LightSensor sensor = new LightSensor(SensorPort.S1,true);
    private int green = -1;
    private int black = -1;
    private int white = -1;

    public static void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3);
    }
    public int rnv() {
	return sensor.readNormalizedValue();
    }
    private static File f;
    private static FileOutputStream fos;

    public static void writeS(String s) throws Exception {
	for(int i=0; i<s.length(); i++)
	    fos.write((byte)s.charAt(i));
    }
    public void calibrate() {
	// Green
	green = rnv();
	LCD.drawInt(green, 2, 7, 0);
	while (rnv() < green+20);
	black = rnv();
	LCD.drawInt(black, 2, 7, 1);
	while (rnv() < black+20);
	white = rnv();
	LCD.drawInt(white, 2, 7, 2);
	
    }
    public GridAccuracy() throws Exception {
	LCD.drawString("green: ", 0, 0);
	LCD.drawString("black: ", 0, 1);
	LCD.drawString("white: ", 0, 2);
	//calibrate();
	controlMotor(MotorPort.B,0);
	controlMotor(MotorPort.C,0);
	controlMotor(MotorPort.B,-70);
	controlMotor(MotorPort.C,-70);
	f = new File("GridAccuracy.txt");
	if (f.exists()) f.delete();
	f.createNewFile();
	fos = new FileOutputStream(f);
	int samplenr = 0;
	while (!Button.ESCAPE.isDown()) {
	    writeS(samplenr+++"\t");
	    writeS(rnv()+"\n");
	    Thread.sleep(5);
	    LCD.drawInt(green, 2, 7, 0);
	    LCD.drawInt(black, 2, 7, 1);
	    LCD.drawInt(white, 2, 7, 2);
	    LCD.drawInt(rnv(), 2, 7, 5);
	}
	fos.close();
    }
    public static void main (String[] aArg) throws Exception {
	new GridAccuracy();
    }
}
