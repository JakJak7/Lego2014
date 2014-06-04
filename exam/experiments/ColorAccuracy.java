import lejos.nxt.*;
import java.io.*;
import lejos.robotics.Color;

public class ColorAccuracy {
    private ColorSensor cs = new ColorSensor(SensorPort.S4);

    private MotorPort MP1 = MotorPort.A, MP2 = MotorPort.B;
    private String colorNames[] = {"None", "Red", "Green", "Blue", "Yellow",
				   "Megenta", "Orange", "White", "Black", "Pink",
				   "Grey", "Light Grey", "Dark Grey", "Cyan"};

    public static void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3);
    }
    
    private static File f;
    private static FileOutputStream fos;
    public static void writeS(String s) throws Exception {
	for(int i=0; i<s.length(); i++)
	    fos.write((byte)s.charAt(i));
    }

    public ColorAccuracy() throws Exception {
	f = new File("SimpleLightMes.txt");
	if (f.exists()) f.delete();
	f.createNewFile();
	fos = new FileOutputStream(f);

	cs.setFloodlight(Color.WHITE);
	controlMotor(MotorPort.A,70);
	controlMotor(MotorPort.B,70);

	writeS("r\tg\tb\n");
	int i = 0;
	while (!Button.ESCAPE.isDown()) {
	    ColorSensor.Color vals = cs.getColor();
	    ColorSensor.Color raw = cs.getRawColor();
	    LCD.clear();
	    LCD.drawString(colorNames[vals.getColor()+1], 0, 0);
	    writeS(i+"\t"+raw.getRed()+"\t"+raw.getGreen()+"\t"+raw.getBlue()+'\n');
	    Thread.sleep(5);
	    i++;
	}
	fos.close();
    }
    public static void main (String[] aArg) throws Exception {
	new ColorAccuracy();
    }
}
