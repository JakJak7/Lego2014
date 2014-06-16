import lejos.nxt.*;
import lejos.robotics.Color;

public class Move {
    public final int LEFT=-1,RIGHT=1,LIGHT=0,SHADOW=1,FRONT=0,BACK=1,
	tCOLOR=0,tFRONT=1,tBACK=2,tGRAY = 3;

    private final LightSensor sensorF = new LightSensor(SensorPort.S1,true),
	sensorB = new LightSensor(SensorPort.S2,true);
    private final ColorSensor cs1 = new ColorSensor(SensorPort.S3),
	cs2 = new ColorSensor(SensorPort.S4);
    private final NXTRegulatedMotor M3 = Motor.C;
    private final MotorPort MP1 = MotorPort.A, MP2 = MotorPort.B;
    private int power = 100, colorSensorMaxValue1 = -1, colorSensorMaxValue2 = -1,
	lastColor =-1,
	offsetFrontLight=-1,offsetFrontShadow=-1,offsetBackLight=-1,offsetBackShadow=-1;

    public void turn(int direction,int speed) { // turn with tacho
	int buffer = power;
	setPower(speed);
	MP1.resetTachoCount();
	controlMotor(power*direction,-power*direction);
	while (MP1.getTachoCount()*direction<319);
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
    public void pass() {move(330);}
    public void setPower(int power) {this.power = power;}
    public int getColor() {
	return lastColor;
    }
    public boolean dummy = true;
    public void followP(int direction, int terminate){
	float Kp = 1f;
	int Tp = power;

	//	offsetFrontLight,offsetFrontShadow,offseBackLight,offseBackShadow;
	int offset = offsetFrontLight;
	if (direction == SHADOW)
	    offset = offsetFrontShadow;
	
	int bb = 0;
	int colorterminate = 0;
	boolean overunder = false;
	int overunderstate = 0;
	int lightvalueback=-1;
	while (!Button.ESCAPE.isDown()) {
	    //# Pcontrol
	    int error = light(FRONT) - offset;
	    int Turn = (int) (Kp*error);
	    controlMotor((Tp+Turn),(Tp-Turn));

	    //# Over/Under/Over
	    if (!overunder)
		if (overunderstate == 0) {
		    if (light(FRONT) > offset)
			overunderstate = 1;
		    else if (light(FRONT) < offset)
			overunderstate = 2;
		}
		else if (overunderstate == 1 && (light(FRONT) < offset))
		    overunderstate = 2;
		else if (overunderstate == 2 && (light(FRONT) > offset))
		    overunder = true;

	    //# Termineringer
	    // color
	    lastColor = determineColor();
	    if (terminate == tCOLOR && lastColor > -1) {
		if (colorterminate < 10) {
		    colorterminate++;
		    continue;
		}
		controlMotor(0,0);
		break;
	    } colorterminate=0;
	    
	    if (terminate == tFRONT && overunder)
		if (light(FRONT) < offset - 25) {
		    if (bb > 3)  break;
		    bb++;
		    continue;
		}
		else bb = 0;


	    if (terminate == tGRAY && overunder && light(FRONT) > offset + 5) 
		break;
	}
    }
    private void approach() {
	move(230);
    }
    public void turnSolar() {
	setPower(40);
	release(false);
	approach();
	move(100);
	grab(true);
	//align(RIGHT,true);
	turn(LEFT);
	turn(LEFT);
	//align2(RIGHT,true);
	move(-180);
	release(false);
	move(-200);
	grab(false);
    }

    /*    public void align(int direction, boolean b) {
	int pp = 30;
	controlMotor(pp*direction,-pp*direction);
	while(light(FRONT) > ordinaroffset);
	controlMotor(-pp*direction,pp*direction);
	while(light(FRONT) < ordinaroffset);
	controlMotor(0,0);
	if (b) 
	    sleep(200);
    }
    */
    public void setColorSensorMaxValue() {
	colorSensorMaxValue1 = cs1.getRawLightValue();
	colorSensorMaxValue2 = cs2.getRawLightValue();
    }
    public void revertColor() {
	if (lastColor == 1)
	    lastColor = 2;
	else if(lastColor == 2)
	    lastColor = 1;
    }

    public void calibrate() {
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

	int broek=5;
	offsetFrontLight  = (int) (white1L/broek+black1L/broek*(broek-1));
	offsetFrontShadow = (int) (white1S+black1S)/2;
	offsetBackLight    = (int) (white2L+black2L)/2;
	offsetBackShadow   = (int) (white2S+black2S)/2;

	move(-80);
	turn(LEFT);
	align(LIGHT,LEFT);
    }
    public void align(int light, int direction) {
	int buffer = power;
	setPower(40);
	controlMotor(-power*direction,power*direction);
	int offset = (light == LIGHT)?offsetFrontLight:offsetFrontShadow;
	while (light(FRONT) > offset);
	controlMotor(0,0);
	setPower(buffer);
    }

    public void switche() {}

    public Move() {
	//# Setup
	M3.setSpeed(720);
	cs1.setFloodlight(Color.WHITE);
	cs2.setFloodlight(Color.WHITE);
    }
    
    public void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3); //3 stop
	else if (value == -1) m.controlMotor(value,4); //4 float
    }
    public void sleep(int i) {try {Thread.sleep(i);}catch (Exception e){}}
    private int light(int end) {return ((end == LIGHT)?sensorF:sensorB).readNormalizedValue();}
    public void controlMotor(int value1, int value2) {
	controlMotor(MP1,value1);
	controlMotor(MP2,value2);
    }

    private int determineColor() {
	ColorSensor.Color raw = cs1.getRawColor();
	int red = raw.getRed();
	int green = raw.getGreen();
	int blue = raw.getBlue();
	if (red-green > 5)
	    return 1; // Red
	else if (blue-green > 5) 
	    return 2; // Blue
	else if (colorSensorMaxValue1-green > 120)
	    return 0; // Black
	else
	    return -1;
    }

    private void grab(boolean f) {
	M3.rotateTo(-270,f);
    }
    private void release(boolean f) {
	//M3.setSpeed(300);
	M3.rotateTo(0,f);
	M3.setSpeed(720);
    }


}