import lejos.nxt.*;
import java.io.*;
import java.lang.Math;

public class FirstDriver {
    private final LightSensor sensorF = new LightSensor(SensorPort.S1,true);
    private final LightSensor sensorB = new LightSensor(SensorPort.S2,true);
    private final NXTRegulatedMotor M3 = Motor.C;
    private final MotorPort MP1 = MotorPort.A, MP2 = MotorPort.B;
    private final int LEFT = -1,RIGHT=1,FRONT=0,BACK=1;
    private int green = -1, black1F = 1024,black1B = 1024, white1F = -1, white1B = -1;
    private int power = 80, black2F = 1024,black2B = 1024, white2F = -1, white2B = -1;


    public int light(int end) {
	return ((end == FRONT)?sensorF:sensorB).readNormalizedValue();
    }
    public void grap() {
	M3.rotateTo(-270);
    }
    public void release() {
	M3.rotateTo(0);
    }
    public static void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,4); //3 stop
    }

    public void calibrate() throws Exception {
	controlMotor(MP1,power);
	controlMotor(MP2,power);
	green = light(FRONT);
	LCD.drawInt(green, 2, 7, 0);
	while (light(FRONT) < green+40);
	Thread.sleep(100);
	white1F = light(FRONT); // 1 FRONT WHITE
	LCD.drawInt(white1F, 2, 7, 2);
	while (light(FRONT) > white1F-40);
	while (light(FRONT) < white1F-20) // 1 FRONT BLACK
	    black1F = (light(FRONT) < black1F)?light(FRONT):black1F;
	LCD.drawInt(black1F, 2, 11, 1);
	white2F = light(BACK); // 2 FRONT WHITE
	LCD.drawInt(white2F, 2, 10, 2);
	while (light(BACK) > white2F-40);
	while (light(BACK) < white2F-20) // 2 FRONT BLACK
	    black2F = (light(BACK) < black2F)?light(BACK):black2F;
	LCD.drawInt(black2F, 2, 11, 1);
	turn(LEFT);
	turn(LEFT);
	white1B = light(FRONT); // 1 WHITE BACK
	white2B = light(BACK); // 2 WHITE BACK
	controlMotor(MP1,-power);
	controlMotor(MP2,-power);
	while (light(FRONT) > white1B-40);
	while (light(FRONT) < white1B-20) // 1 BLACK BACK
	    black1B = (light(FRONT) < black1B)?light(FRONT):black1B;
	controlMotor(MP1,power);
	controlMotor(MP2,power);
	while (light(BACK) > white2B-40);
	while (light(BACK) < white2B-20) // 2 BLACK BACK
	    black2B = (light(BACK) < black2B)?light(BACK):black2B;
    }
    public void turn(int direction) {
	MP1.resetTachoCount();
	controlMotor(MP1,power*direction);
	controlMotor(MP2,-power*direction);
	while (MP1.getTachoCount()*direction<300);
	controlMotor(MP1,0);
	controlMotor(MP2,0);
    }
    public void move(int i) {
	MP1.resetTachoCount();
	int d = (i<0)?-1:1;
	controlMotor(MP1,power*d);
	controlMotor(MP2,power*d);
	while (d*MP1.getTachoCount()<i*d);
	controlMotor(MP1,0);
	controlMotor(MP2,0);
    }
    public void followP_BACK() {
	float Kp = 1f;
	int Tp = 50;
	int offset = (int) (white2B/5+black2B/5*4);
	while (!Button.ESCAPE.isDown()) {
	    int error = light(BACK) - offset;
	    int Turn = (int) (Kp*error);
	    controlMotor(MP1,-((Tp-Turn)));
	    controlMotor(MP2,-((Tp+Turn)));
	}
    }
    int ru = 0;
    public void followP(int direction) {
	float Kp = 1f;
	int Tp = 80;
	int offset = (int) (white1F/8+black1F/8*7);
	if (direction == BACK)
	    offset = (int) (white1B/4+black1B/4*3);
	LCD.clear();
	LCD.drawInt(offset,0,0);
	boolean rt = false;

	while (!Button.ESCAPE.isDown()) {
	    int error = light(FRONT) - offset;
	    int Turn = (int) (Kp*error);
	    controlMotor(MP1,(Tp+Turn));
	    controlMotor(MP2,(Tp-Turn));
	    dl.extra = "\t"+rt+"\t"+offset+"\t"+ru;
	    if (light(FRONT) < offset)
		rt = true;
	    
	    if (rt) {
		LCD.drawInt(33333333,0,0);
		LCD.drawInt(ru,0,1);
		LCD.drawInt(offset,0,2);
		LCD.drawInt(light(FRONT),0,3);
		
		if (ru == 0 && light(FRONT) < offset - 30) 
		    break;
		else if (ru == 1 && light(FRONT) < offset - 15) 
		    break;
	    }

	}
	ru++;
    }
    public void turnSolar() {
	grap();
	move(200);
	turn(LEFT);
	turn(LEFT);
	move(-30);
	release();
	move(-200);
	grap();
	move(2*800);
	release();
    }
    private Datalog dl;
    public FirstDriver() throws Exception {
	dl = new Datalog();
	LCD.drawString("green: ", 0, 0);
	LCD.drawString("black: ", 0, 1);
	LCD.drawString("white: ", 0, 2);

	calibrate();
	move(-80);
	turn(LEFT);
	followP(FRONT);
	move(240);
	turn(RIGHT);
	followP(BACK);

	
	//turnSolar();
	

    }
    public static void main (String[] aArg) throws Exception {
	new FirstDriver();
    }
}
