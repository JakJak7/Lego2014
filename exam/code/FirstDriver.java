import lejos.nxt.*;
import java.io.*;
import lejos.robotics.Color;

public class FirstDriver {
    private final LightSensor sensorF = new LightSensor(SensorPort.S1,true);
    private final LightSensor sensorB = new LightSensor(SensorPort.S2,true);
    private final LightSensor sensorL = new LightSensor(SensorPort.S3,true);
    private final ColorSensor cs = new ColorSensor(SensorPort.S4);

    private final NXTRegulatedMotor M3 = Motor.C;
    private final MotorPort MP1 = MotorPort.A, MP2 = MotorPort.B;
    private final int LEFT = -1,RIGHT=1,FRONT=0,BACK=1;
    private int green = -1, black1F = 1024,black1B = 1024, white1F = -1, white1B = -1;
    private int power = 100, black2F = 1024,black2B = 1024, white2F = -1, white2B = -1;


    public int light(int end) {
	return ((end == FRONT)?sensorF:sensorB).readNormalizedValue();
    }

    public void grap(boolean f) {
	M3.rotateTo(-270,f);
    }
    public void release(boolean f) {
	M3.rotateTo(0,f);
    }
    public static void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3); //3 stop
	else if (value == -1) m.controlMotor(value,4); //4 float
    }

    public void calibrate() throws Exception {
	grap(true);
	controlMotor(MP1,power);
	controlMotor(MP2,power);
	green = light(FRONT);
	while (light(FRONT) < green+40);
	Thread.sleep(100);
	white1F = light(FRONT); // 1 FRONT WHITE
	while (light(FRONT) > white1F-40);
	while (light(FRONT) < white1F-20) // 1 FRONT BLACK
	    black1F = (light(FRONT) < black1F)?light(FRONT):black1F;
	white2F = light(BACK); // 2 FRONT WHITE
	while (light(BACK) > white2F-40);
	while (light(BACK) < white2F-20) // 2 FRONT BLACK
	    black2F = (light(BACK) < black2F)?light(BACK):black2F;
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
	while (MP1.getTachoCount()*direction<320);
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
    public int determineColor() {
	ColorSensor.Color raw = cs.getRawColor();
	int red = raw.getRed();
	int green = raw.getGreen();
	int blue = raw.getBlue();
	if (valval-green > 80)
	    return 0; // Black
	else if (red-green > 10)
	    return 1; // Red
	else if (blue-green > 10) 
	    return 2; // Blue
	else
	    return -1;
    }

    public void followP(int direction) throws Exception {
	float Kp = 1f;
	int Tp = power;
	int offset = (int) (white1F/8+black1F/8*7);
	if (direction == BACK)
	    offset = (int) (white1B/4+black1B/4*3);
	
	while (!Button.ESCAPE.isDown()) {
	    int error = light(FRONT) - offset;
	    int Turn = (int) (Kp*error);
	    controlMotor(MP1,(Tp+Turn));
	    controlMotor(MP2,(Tp-Turn));
	    if (determineColor() > -1) {
		controlMotor(MP1,0);
		controlMotor(MP2,0);
		break;
	    }

	    if (light(FRONT) < offset)
		if (ru == 0 && light(FRONT) < offset - 40) 
		    break;
		else if (ru == 1 && light(FRONT) < offset - 10) 
		    break;
	}
	ru++;
    }

    public int abs(int i){return (i<0)?-i:i;}
    public int sign(int i){return (i<0)?-1:1;}
    public void turnSolar() {
	grap(false);
	turn(LEFT);
	turn(LEFT);
	move(-315);
	release(false);
    }
    private Datalog dl = new Datalog();;
    int valval = -2;
    public void approach() {
	power=70;
	move(180);
	power=40;
	move(80);
	power=70;
    }
    public FirstDriver() throws Exception {
	//# Setup
	M3.setSpeed(720);
	cs.setFloodlight(false);
	cs.setFloodlight(Color.WHITE);
	ColorSensor.Color raw = cs.getRawColor();

	//new Thread(dl).start();
	//dl.close();

	//# Calibrate
	calibrate();
	move(-80);
	turn(LEFT);
	
	//# Go out into space
	followP(FRONT);
	valval = cs.getRawLightValue();
	move(260);

	power=70;
	turn(RIGHT);

	followP(BACK);
	move(285);
	turn(LEFT);
	move(-250);
	followP(FRONT);
	release(true);

	move(-350);
	followP(FRONT);
	//# Approach 
	approach();
	
	turnSolar();


	controlMotor(MP1,-80);
	controlMotor(MP2,-80);


	Thread.sleep(450);
	controlMotor(MP1,0);
	controlMotor(MP2,0);
	grap(false);

	controlMotor(MP1,100);
	controlMotor(MP2,100);
	Thread.sleep(500);
	release(true);
	Thread.sleep(1500);
    }
    public static void main (String[] aArg) throws Exception {
	new FirstDriver();
    }
}
