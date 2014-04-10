import lejos.nxt.*;
import lejos.robotics.Color;
import lejos.robotics.*;
import lejos.nxt.addon.*;

public class Code3 {
    private final LightSensor l1 = new LightSensor(SensorPort.S1,true);
    private final LightSensor l2 = new LightSensor(SensorPort.S2,true);
    private final ColorSensor cs = new ColorSensor(SensorPort.S3);
    private final DCMotor ml = new RCXMotor(MotorPort.A);
    private final DCMotor mr = new RCXMotor(MotorPort.B);
    private final UltrasonicSensor us = new UltrasonicSensor(SensorPort.S4);
    private void print() {
	LCD.drawString("Light: ", 0, 0); 
	LCD.drawString("Color: ", 0, 1); 
	LCD.drawString("Light: ", 0, 2); 
	LCD.drawString("Ultra: ", 0, 3); 
	LCD.drawInt(getL(1),4,10,0);
	LCD.drawInt(getC() ,4,5,1);
	LCD.drawInt(getCL() ,4,10,1);
	LCD.drawInt(getL(2),4,10,2);
	LCD.drawInt(us.getDistance(),4,10,3);
    }
    private int getC() {return cs.getColor().getColor();}//1 = green, 7 = black, 6 = white?
    private int getCL() {return cs.getNormalizedLightValue();}
    private int getL(int i){return ((i==1)?l1:l2).readValue();} // white: 35< sort: <35 lav
    private boolean isWhite(int i) {return getL(i) > 42;}

    private boolean startzone() {
	ml.setPower(100);
	mr.setPower(100);
	ml.forward();
	mr.forward();
	while (getC() == 1) {
	    print();
	}
	return true;
    }
    private boolean up() throws Exception {
	while (true) {
	    print();
	    if (us.getDistance() == 11) {
		return true;
	    }
	    else {
		if (getC() == 7 || (isWhite(1) && isWhite(2))) {
		    ml.setPower(100);
		    mr.setPower(100);
		}
		if (!isWhite(1))
		    mr.setPower(30);
		if (!isWhite(2))
		    ml.setPower(30);
	    }
	}
    }
    private boolean RightTurn() throws Exception {
	int turn = 300;
	int forward = 500;
	ml.setPower(100);
	mr.setPower(100);
	Thread.sleep(50);
	ml.setPower(100);
	mr.setPower(-100);
	Thread.sleep(turn);
	mr.setPower(100);
	Thread.sleep(forward);
	mr.setPower(-100);
	Thread.sleep(150);
	ml.stop();
	mr.stop();
	return false;
    }

    public Code3()  throws Exception {
	//	rightturn();
	cs.setFloodlight(Color.WHITE);
	//startzone();
	//RightTurn();
	
	startzone();
	LCD.drawString("startzone done", 0, 5); 
	up();
	LCD.drawString("niveau 1 done", 0, 5); 
	//RightTurn();
	LCD.drawString("rightturn done", 0, 5); 
	//up();
	LCD.drawString("niveau 2 done", 0, 5); 	
	while(true){
	    print();
	}
	
    }

    public static void main(String[] args)  throws Exception {
	Button.ESCAPE.addButtonListener(new ButtonListener() {
		public void buttonPressed(Button b) {
		    //playSound("FF1_victory_2.wav");
		    System.exit(1);
		}
		public void buttonReleased(Button b) {} 
	    });
	
	new Code3();
    }
}
