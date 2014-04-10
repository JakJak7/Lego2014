import lejos.nxt.*;
import lejos.robotics.Color;
import lejos.robotics.*;
import lejos.nxt.addon.*;

public class Code3 {
    private final LightSensor l1 = new LightSensor(SensorPort.S1,false);
    private final LightSensor l2 = new LightSensor(SensorPort.S2,false);
    private final ColorSensor cs = new ColorSensor(SensorPort.S3);
    private final DCMotor ml = new RCXMotor(MotorPort.A);
    private final DCMotor mr = new RCXMotor(MotorPort.B);

    private int getC() {return cs.getColor().getColor();}//1 = green, 7 = black, 6 = white?
    private int getCL() {return cs.getNormalizedLightValue();}
    private int getL(int i){return ((i==1)?l1:l2).readValue();}

    public Code3() {
	ml.setPower(100);
	mr.setPower(100);

	cs.setFloodlight(Color.WHITE);
	LCD.clear();
	LCD.drawString("Light: ", 0, 0); 
	LCD.drawString("Color: ", 0, 1); 
	LCD.drawString("Light: ", 0, 2); 

        while (true) {
	    LCD.drawInt(getL(1),4,10,0);
	    int cc = getC();
	    LCD.drawInt(cc ,4,5,1);
	    LCD.drawInt(getCL() ,4,10,1);
	    LCD.drawInt(getL(2),4,10,2);
	    
	    if (cc == 7) {
		ml.forward();
		mr.forward();
	    }
	    else
	    if (cc == 6) {
		ml.stop();
		mr.stop();
	    } 

	}

    }

    public static void main(String[] args) {
	Button.ESCAPE.addButtonListener(new ButtonListener() {
		public void buttonPressed(Button b) {System.exit(1);}
		public void buttonReleased(Button b) {} 
	    });
	
	new Code3();
    }
}
