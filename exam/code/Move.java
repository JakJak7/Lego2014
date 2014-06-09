import lejos.nxt.*;
import lejos.robotics.Color;

public class Move {
    private final LightSensor sensorF = new LightSensor(SensorPort.S1,true);
    private final LightSensor sensorB = new LightSensor(SensorPort.S2,true);
    private final ColorSensor cs = new ColorSensor(SensorPort.S3);
    private final ColorSensor cs2 = new ColorSensor(SensorPort.S4);

    private final MotorPort MP1 = MotorPort.A, MP2 = MotorPort.B;
    private final int LEFT = -1,RIGHT=1,FRONT=0,BACK=1;
    private int green = -1, black1F = 1024,black1B = 1024, white1F = -1, white1B = -1;
    private int power = 100, black2F = 1024,black2B = 1024, white2F = -1, white2B = -1;

    public void setPower(int power) {
	this.power = power;
    }

    public int light(int end) {
	return ((end == FRONT)?sensorF:sensorB).readNormalizedValue();
    }
    public void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3); //3 stop
	else if (value == -1) m.controlMotor(value,4); //4 float
    }

    public void controlMotor(int value1, int value2) {
	controlMotor(MP1,value1);
	controlMotor(MP2,value2);
    }

    public void breakMotors() {
	controlMotor(MP1,0);
	controlMotor(MP2,0);
    }
    public void smallturn(int direction) {
	MP1.resetTachoCount();
	controlMotor(MP1,power*direction);
	controlMotor(MP2,-power*direction);
	while (MP1.getTachoCount()*direction<50);
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
    public int determineColor() {
	ColorSensor.Color raw = cs.getRawColor();
	int red = raw.getRed();
	int green = raw.getGreen();
	int blue = raw.getBlue();
	if (red-green > 5)
	    return 1; // Red
	else if (blue-green > 5) 
	    return 2; // Blue
	else if (valval-green > 120)
	    return 0; // Black
	else
	    return -1;
    }

    public void revertColor() {
	if (lastColor == 1)
	    lastColor = 2;
	else if(lastColor == 2)
	    lastColor = 1;
    }
    public int getColor() {
	return lastColor;
    }
    private int lastColor;
    public int accelerate(int from, int to, int interval) {
	if (from >= to)
	    return to;

	return MP1.getTachoCount()/interval;


    }
    public void followP(int direction, int ru) throws Exception {
	float Kp = 1f;
	int Tp = power;
	MP1.resetTachoCount();


	
	int offset = (int) (white1F/8+black1F/8*7);
	if (direction == BACK)
	    offset = (int) (white1B/4+black1B/4*3);
	
	int bb = 0;
	while (!Button.ESCAPE.isDown()) {
	    int error = light(FRONT) - offset;
	    int Turn = (int) (Kp*error);
	    Tp = accelerate(Tp,power,5);
	    controlMotor(MP1,(Tp+Turn));
	    controlMotor(MP2,(Tp-Turn));
	    lastColor = determineColor();
	    if (ru == 1 && lastColor > -1) {
		controlMotor(MP1,0);
		controlMotor(MP2,0);
		break;
	    }

	    if (light(FRONT) < offset)
		if (ru == 0 && light(FRONT) < offset - 30) 
		    break;
		else if (ru == 2 && light(FRONT) < offset - 10) 
		    break;
	    if (bb == 0 && light(FRONT) > offset)
		bb = 1;
	    else if (bb == 1 && light(FRONT) < offset)
		bb = 2;
	    
	    if (bb == 2 && ru == 3 && light(FRONT) > offset + 5) 
		break;

	}
    }
    private final NXTRegulatedMotor M3 = Motor.C;

    public void grab(boolean f) {
	M3.rotateTo(-270,f);
    }
    public void release(boolean f) {
	//M3.setSpeed(300);
	M3.rotateTo(0,f);
	M3.setSpeed(720);
    }

    public void turnSolar() throws Exception {
	release(false);
	approach();
	grab(false);
	turn(LEFT);
	turn(LEFT);
	move(-330);
	release(false);
	move(-200);
	grab(true);
	move(-300);
	power=35;
	followP(FRONT,1);
    }
    
    private int valval = -2;
    private int valval2 = -2;

    public void setValval() {
	valval = cs.getRawLightValue();
	valval2 = cs2.getRawLightValue();
    }

    public void approach() {
	power=50;
	move(160);
	power=40;
	move(90);
	power=70;
    }

    public void calibrate() throws Exception {
	grab(true);
	controlMotor(MP1,power);
	controlMotor(MP2,power);
	green = light(FRONT);
	while (light(FRONT) < green+40);
	Thread.sleep(100);
	white1F = light(FRONT); // 1 FRONT WHITE
	while (light(FRONT) > white1F-30);
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
	while (light(FRONT) > white1B-30);
	while (light(FRONT) < white1B-20) // 1 BLACK BACK
	    black1B = (light(FRONT) < black1B)?light(FRONT):black1B;
	controlMotor(MP1,power);
	controlMotor(MP2,power);
	while (light(BACK) > white2B-40);
	while (light(BACK) < white2B-20) // 2 BLACK BACK
	    black2B = (light(BACK) < black2B)?light(BACK):black2B;
	move(-80);
	turn(LEFT);
    }
    public void turn(int direction) {
	MP1.resetTachoCount();
	controlMotor(MP1,power*direction);
	controlMotor(MP2,-power*direction);
	while (MP1.getTachoCount()*direction<319);
	controlMotor(MP1,0);
	controlMotor(MP2,0);
    }

    public void pass() {
	move(300);
    }
    public void switche() throws Exception{
	power=40;
	release(false);
	approach();
	grab(false);
	turn(LEFT);
	turn(LEFT);
	move(-100);
	followP(FRONT,1);
	pass();
	power=70;
	followP(FRONT,2);
	move(285);
	turn(RIGHT);
	followP(FRONT,2);
	move(285);
	turn(LEFT);
	power=100;
	followP(FRONT,0);
	power=50;
	move(285);
	turn(LEFT);
	followP(BACK,2);
	release(false);
	move(-400);
	turn(LEFT);
	turn(LEFT);
	smallturn(LEFT);
	followP(FRONT,1);
	approach();
	grab(false);
	move(-400);
	//followP_BACK(BACK,1);
	turn(RIGHT);
	power=100;
	release(true);
	followP(FRONT,0);
	grab(true);
	power=70;
	move(285);
	turn(RIGHT);
	followP(BACK,0);
	power=50;
	move(285);
	turn(LEFT);
	move(-250);
	followP(FRONT,1);
	pass();
	followP(FRONT,3);
	Sound.beep();
	controlMotor(MP1,0);
	controlMotor(MP2,0);
	move(273);
	release(false);
    }

    public Move() {
	//# Setup
	M3.setSpeed(720);
	cs.setFloodlight(Color.WHITE);
	cs2.setFloodlight(Color.WHITE);
	//ColorSensor.Color raw = cs.getRawColor();
	//ColorSensor.Color raw2 = cs2.getRawColor();
    }
}