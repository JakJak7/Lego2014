import lejos.nxt.*;
import java.io.*;

public class GridAccuracy {
    private LightSensor s1 = new LightSensor(SensorPort.S1,true);
    private LightSensor s2 = new LightSensor(SensorPort.S2,true);
    private int green = 580;
    private int black = 580;
    private int white = 500;

    public static void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3);
    }
    public int rnv(int i) {
	return ((i==1)?s1:s2).readNormalizedValue();
    }
    /*    private static File f;
    private static FileOutputStream fos;

    public static void writeS(String s) throws Exception {
	for(int i=0; i<s.length(); i++)
	    fos.write((byte)s.charAt(i));
    }
    */
    public void calibrate() {
	// Green
	green = rnv(1);
	LCD.drawInt(green, 2, 7, 0);
	while (rnv(1) < green+20);
	black = rnv(1);
	LCD.drawInt(black, 2, 7, 1);
	while (rnv(1) < black+20);
	white = rnv(1);
	LCD.drawInt(white, 2, 7, 2);
	
    }
    public GridAccuracy() throws Exception {
	LCD.drawString("green: ", 0, 0);
	LCD.drawString("black: ", 0, 1);
	LCD.drawString("white: ", 0, 2);
	//calibrate();
	controlMotor(MotorPort.B,-70);
	controlMotor(MotorPort.C,-70);
	/*
	f = new File("GridAccuracy.txt");
	if (f.exists()) f.delete();
	f.createNewFile();
	fos = new FileOutputStream(f);
	int samplenr = 0;
	writeS(samplenr+++"\t");
	writeS(rnv()+"\n");
	Thread.sleep(5);
	*/
	//calebrate
	int cal = rnv(1)-rnv(2);

	while (!Button.ESCAPE.isDown()) {
	    int power = -90;
	    if (rnv(1) >= white && rnv(2) >= white) {
		controlMotor(MotorPort.B,power);
		controlMotor(MotorPort.C,power);
	    }
	    else if (rnv(1) >= white && rnv(2) < white) {
		controlMotor(MotorPort.B,0);
		controlMotor(MotorPort.C,power);
	    }

	    else if (rnv(1) < white && rnv(2) >= white) {
		controlMotor(MotorPort.B,power);
		controlMotor(MotorPort.C,0);
	    }
	    else if (rnv(1) < white && rnv(2) < white) {
		controlMotor(MotorPort.B,0);
		controlMotor(MotorPort.C,0);
	    }
	    LCD.drawInt(green, 2, 7, 0);
	    LCD.drawInt(black, 2, 7, 1);
	    LCD.drawInt(white, 2, 7, 2);
	    LCD.drawInt(rnv(0)+cal, 2, 7, 5);
	    LCD.drawInt(rnv(1)-cal, 2, 7, 6);
	}
	//fos.close();
    }
    public static void main (String[] aArg) throws Exception {
	new GridAccuracy();
    }
}
