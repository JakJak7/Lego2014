import lejos.nxt.*;
import lejos.robotics.Color;

public class Move {
    public final int LEFT=-1,RIGHT=1,UP=0,DOWN=2,
	tCOLOR=0,tFRONT=1,tBACK=2,tGRAY=3;

    private final LightSensor ls = new LightSensor(SensorPort.S1,true),
	ls2 = new LightSensor(SensorPort.S2,false);
    private final ColorSensor cs = new ColorSensor(SensorPort.S3);
    private final NXTRegulatedMotor M3 = Motor.C;
    private final MotorPort MP1 = MotorPort.A, MP2 = MotorPort.B;
    public int power = 100, colorMax = -1,
	lastColor =-1, offset_right =-1, offset_down =-1, offset_left=-1, offset_up =-1;
    
    public void turn(int direction,int speed) { // turn with tacho
	int buffer = power;
	setPower(speed);
	MP1.resetTachoCount();
	controlMotor(power*direction,-power*direction);
	while (MP1.getTachoCount()*direction<319);
	controlMotor(0,0);
	setPower(buffer);
    }

    public void turn(int direction,int speed,int length) { // turn with tacho
	int buffer = power;
	setPower(speed);
	MP1.resetTachoCount();
	controlMotor(power*direction,-power*direction);
	while (MP1.getTachoCount()*direction<length);
	controlMotor(0,0);
	setPower(buffer);
    }

    public void turn(int direction) {turn(direction,power);}

    public void move(int i) {
	MP1.resetTachoCount();
	int d = (i<0)?-1:1;
	controlMotor(power*d,power*d);
	while (d*MP1.getTachoCount()<i*d);
	controlMotor(0,0);
    }
    public void pass() {
	int p = power;
	setPower(100);
	move(300);
	setPower(p);
    }
    public void setPower(int power) {this.power = power;}
    public int getColor() {return lastColor;}

    private int getOffset(int direction) {
	if (direction == UP)
	    return offset_up;
	else if (direction == DOWN)
	    return offset_down;
	else if (direction == LEFT)
	    return offset_left;
	else if (direction == RIGHT)
	    return offset_right;
	else
	    return 404;
    }
    public void followP(int direction, int terminate){
	float Kp = 1.0f;
	int Tp = 40;

	int offset = getOffset(direction);
	int terminateCount = 0;
	boolean overunder = false;
	int overunderstate = 0;
	boolean dal = true;
	while (!Button.ESCAPE.isDown()) {
	    //# Pcontrol
	    int error = light() - offset;
	    int Turn = (int) (Kp*error);
	    controlMotor((Tp+Turn),(Tp-Turn));

	    //# Over/Under/Over
	    if (!overunder)
		if (overunderstate == 0) {
		    if (light() > offset)
			overunderstate = 1;
		    else if (light() < offset)
			overunderstate = 2;
		}
		else if (overunderstate == 1 && (light() < offset))
		    overunderstate = 2;
		else if (overunderstate == 2 && (light() > offset)) {
		    overunder = true;
		    Tp=power;
		}

	    //# Termineringer
	    // color
	    if (terminate == tCOLOR) {
		lastColor = determineColor(direction);
		Tp=60;
		if (lastColor > -1) {
		    if (terminateCount < 10) {
			terminateCount++;
			continue;
		    }
		    controlMotor(0,0);
		    break;
		} terminateCount=0;
	    }
	    
	    else if (terminate == tFRONT)
		if (light() < offset - 30 && overunder) {
		    if (terminateCount > 7)  break;
		    terminateCount++;
		    continue;
		}
		else terminateCount = 0;

	    else if (terminate == 5)
		if (light() < offset - 10) {
		    if (terminateCount > 4)  break;
		    terminateCount++;
		    continue;
		}
		else terminateCount = 0;



	    else if (terminate == tGRAY && overunder) {
		if (light() > offset) 
		    terminateCount++;
		else
		    terminateCount=0;


		if (terminateCount >= 30)  {
		    controlMotor(0,0);
		    break;
		}

	    }
	}

    }
    int kaka = 0;
    Datalog dl;
    private void approach() {
	move(230);
    }
    public void grapSolar() {
	setPower(40);
	release(false);
	approach();
	grab(true);
	move(100);
    }
    public void turnsolar() {
	int buffer = power;
	setPower(70);
	MP1.resetTachoCount();
	controlMotor(power*LEFT,-power*LEFT);
	while (MP1.getTachoCount()*LEFT<620);
	controlMotor(0,0);
	setPower(buffer);
    }

    public void turnSolar() {
	grapSolar();
	turnsolar();
	sleep(50);
	move(-230);
	release(false);
	move(-200);
	grab(false);    
    }

    public int calibratenext(int broek) {
	int white = light();
	while (light() > white-30);
	int black = light();
	while (light() < white-20)
	    black = (light() < black)?light():black;
	return (white/broek+black/broek*(broek-1));
    }
    private int ambientlight = 0;

    public void calibrate() {
	grab(true);
	hardvalue();
	controlMotor(power,power);
	int green = light();
	while (light() < green+40);
	sleep(100);
	calibratenext(-1);
	move(100);
	turn(RIGHT);
	align(UP,RIGHT);
	setPower(100);
	followP(UP,tFRONT);	
    }
    public void calibrate_() {
	grab(true);
	controlMotor(power,power);
	ambientlight= ls2.readNormalizedValue();
	int green = light();
	setPower(70);
	while (light() < green+40);
	sleep(100);
	
	offset_left = calibratenext(2);
	turn(LEFT);
	sleep(200);
	controlMotor(power,power);
	offset_down = calibratenext(2);
	turn(LEFT);
	sleep(200);
	controlMotor(-power,-power);
	
	offset_right = calibratenext(2);
	move(10);
	turn(LEFT);
	sleep(200);
	controlMotor(-power,-power);
	
	offset_up = calibratenext(2);
	move(300);

	turn(RIGHT,70,200);
	controlMotor(power,power);

	//hardvalue();
	calibratenext(-1);
	move(200);
	align(UP,LEFT);
	setPower(100);
	followP(UP,tFRONT);
	colorMax = cs.getRawLightValue();	

	LCD.drawInt(offset_up,0,0);
	LCD.drawInt(offset_down,0,1);
	LCD.drawInt(offset_left,0,2);
	LCD.drawInt(offset_right,0,3);
	LCD.drawInt(colorMax,0,4);

    }
    public void hardvalue() {
	offset_up = 390;
	offset_down =390;
	offset_left = 390;
	offset_right =390;
	colorMax = 251;

    }
    public void align(int direction, int side) {
	int buffer = power;
	setPower(40);
	controlMotor(power*side,-power*side);
	int offset = getOffset(direction);
	int count = 0;
	while (light() >= offset-15);

	if (side != LEFT)
	    controlMotor(-power*side,power*side);
	while (light() <= offset+5);
	controlMotor(0,0);
	setPower(buffer);
    }

    private GrabReleaseTimer GrabReleaseTimer;
    public Move() {
	//# Setup
	M3.setSpeed(720);
	cs.setFloodlight(Color.WHITE);
	GrabReleaseTimer= new GrabReleaseTimer(M3);
	GrabReleaseTimer.start();
    }
    
    public void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3); //3 stop
	else if (value == -1) m.controlMotor(value,4); //4 float
    }
    public void sleep(int i) {try {Thread.sleep(i);}catch (Exception e){}}
    private int light() {return ls.readNormalizedValue()-(
	    ls2.readNormalizedValue()-ambientlight);}
    public void controlMotor(int value1, int value2) {
	controlMotor(MP1,value1);
	controlMotor(MP2,value2);
    }

    private int determineColor(int direction) {
	ColorSensor.Color raw = cs.getRawColor();
	int red = raw.getRed();
	int green = raw.getGreen();
	int blue = raw.getBlue();
	if (red-green > 20)
	    return 1; // Red

	if (blue-green > 20) 
	    return 2; // Blue
	if (colorMax-green > 120)
	    return 0; // Black

	return -1;
    }

    public void grab(boolean f) {
	M3.rotateTo(-270,f);
    }
    public void release(boolean f) {
	//M3.setSpeed(300);
	M3.rotateTo(0,f);
	M3.setSpeed(720);
    }
    public void releasetimer() {
	GrabReleaseTimer.release();
    }

}
class GrabReleaseTimer extends Thread {
    private NXTRegulatedMotor m;
    private boolean runner = true,release=false;;
    public GrabReleaseTimer(NXTRegulatedMotor m) {
	this.m = m;
    }
    public void run() {
	while (runner) {
	    if (release) {
		try {
		    Thread.sleep(600);
		    m.rotateTo(0);
		    release = false;
		}
		catch (Exception e) {}
	    }
	}

    }
    public void release() {release=true;}
    public void close() {runner=false;}
}