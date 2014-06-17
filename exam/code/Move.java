import lejos.nxt.*;
import lejos.robotics.Color;

public class Move {
    public final int LEFT=-1,RIGHT=1,UP=0,DOWN=2,
	tCOLOR=0,tFRONT=1,tBACK=2,tGRAY=3;

    private final LightSensor ls = new LightSensor(SensorPort.S1,true);
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
	while (MP1.getTachoCount()*direction<280);
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
    public void pass() {move(220);}
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
	    
	    if (terminate == tFRONT)
		if (light() < offset - 15 && overunder) {
		    if (terminateCount > 4)  break;
		    terminateCount++;
		    continue;
		}
		else terminateCount = 0;

	    if (terminate == 5 && overunder)
		if (light() < offset - 20) {
		    if (terminateCount > 4)  break;
		    terminateCount++;
		    continue;
		}
		else terminateCount = 0;


	    if (terminate == tGRAY && overunder && light() > offset + 5) 
		break;
	}
    }
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
	while (MP1.getTachoCount()*LEFT<700);
	controlMotor(0,0);
	setPower(buffer);
    }

    public void turnSolar() {
	grapSolar();
	turnsolar();
	sleep(50);
	move(-180);
	release(false);
	move(-200);
	grab(false);    
}
/*
    public void calibrate_old() {
	int maxblack = 1024;
	grab(true);
	controlMotor(power,power);
	int green = light(FRONT);
	while (light(FRONT) < green+40);
	sleep(100);
	int white1L = light(FRONT); // 1 LIGHT WHITE
	int black1L = maxblack;
	while (light(FRONT) > white1L-30);
	while (light(FRONT) < white1L-20) // 1 LIGHT BLACK
	    black1L = (light(FRONT) < black1L)?light(FRONT):black1L;
	int white2S = light(BACK); // 2 LIGHT WHITE
	int black2S = maxblack;
	while (light(BACK) > white2S-40);
	while (light(BACK) < white2S-20) // 2 LIGHT BLACK
	    black2S = (light(BACK) < black2S)?light(BACK):black2S;
	turn(LEFT);turn(LEFT);
	int white1S = light(FRONT); // 1 WHITE SHADOW
	int white2L = light(BACK); // 2 WHITE SHADOW
	controlMotor(-power,-power);
	int black1S = maxblack;
	while (light(FRONT) > white1S-30);
	while (light(FRONT) < white1S-20) // 1 BLACK SHADOW
	    black1S = (light(FRONT) < black1S)?light(FRONT):black1S;
	controlMotor(power,power);
	int black2L = maxblack;
	while (light(BACK) > white2L-40);
	while (light(BACK) < white2L-20) // 2 BLACK SHADOW
	    black2L = (light(BACK) < black2L)?light(BACK):black2L;

	int broek=10;
	offsetFrontLight  = (int) (white1L/broek+black1L/broek*(broek-1));
	offsetFrontShadow = (int) (white1S+black1S)/2;
	offsetBackLight    = (int) (white2L+black2L)/2;
	offsetBackShadow   = (int) (white2S+black2S)/2;

	move(-80);
	turn(LEFT);
	align(LIGHT,LEFT);
    }
    */
  
    private int calibratenext(int broek) {
	int white = light();
	while (light() > white-30);
	int black = light();
	while (light() < white-20)
	    black = (light() < black)?light():black;
	return (white/broek+black/broek*(broek-1));
    }
    
    public void calibrate() {
	grab(true);
	controlMotor(power,power);
	int green = light();
	while (light() < green+40);
	sleep(100);

	offset_left = calibratenext(5);
	turn(LEFT);
	sleep(100);
	controlMotor(power,power);
	offset_down = calibratenext(2);
	turn(LEFT);
	sleep(100);
	controlMotor(-power,-power);
	
	offset_right = calibratenext(2);
	move(10);
	turn(LEFT);
	sleep(100);
	controlMotor(-power,-power);
	
	offset_up = calibratenext(2);
	move(300);

	turn(RIGHT,70,180);
	controlMotor(power,power);
	/*	controlMotor(0,0);
	LCD.drawInt(offset_left,0,0,2);
	while (true) {
	    LCD.drawInt(light(),0,0,1);
	    }*/
	calibratenext(-1);
	move(30);
	controlMotor(0,0);
	align(UP,LEFT);
	setPower(100);
	followP(UP,tFRONT);
	colorMax = cs.getRawLightValue();	
    }

    public void align(int direction, int side) {
	int buffer = power;
	setPower(40);
	controlMotor(power*side,-power*side);
	int offset = getOffset(direction);
	while (light() >= offset) {
	    LCD.drawInt(offset,0,0,1);
	    LCD.drawInt(light(),0,0,2);
	}
	if (side != LEFT)
	    controlMotor(-power*side,power*side);
	while (light() <= offset) {
	    LCD.drawInt(offset,0,0,3);
	    LCD.drawInt(light(),0,0,4);
	}

	controlMotor(0,0);
	setPower(buffer);
    }


    public Move() {
	//# Setup
	M3.setSpeed(720);
	cs.setFloodlight(Color.WHITE);
	//cs2.setFloodlight(Color.WHITE);
    }
    
    public void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3); //3 stop
	else if (value == -1) m.controlMotor(value,4); //4 float
    }
    public void sleep(int i) {try {Thread.sleep(i);}catch (Exception e){}}
    private int light() {return ls.readNormalizedValue();}
    public void controlMotor(int value1, int value2) {
	controlMotor(MP1,value1);
	controlMotor(MP2,value2);
    }

    private int determineColor(int direction) {
	ColorSensor.Color raw = cs.getRawColor();
	int red = raw.getRed();
	int green = raw.getGreen();
	int blue = raw.getBlue();
	if (red-green > 5)
	    return 1; // Red
	else if (blue-green > 5) 
	    return 2; // Blue
	else if (colorMax-green > 100)
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


}