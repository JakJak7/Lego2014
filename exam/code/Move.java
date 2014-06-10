import lejos.nxt.*;
import lejos.robotics.Color;

public class Move {
    public final int LEFT=-1,RIGHT=1,LIGHT=0,SHADOW=1,FRONT=0,BACK=1;

    private final LightSensor sensorF = new LightSensor(SensorPort.S1,true),
	sensorB = new LightSensor(SensorPort.S2,true);
    private final ColorSensor cs1 = new ColorSensor(SensorPort.S3),
	cs2 = new ColorSensor(SensorPort.S4);
    private final NXTRegulatedMotor M3 = Motor.C;
    private final MotorPort MP1 = MotorPort.A, MP2 = MotorPort.B;
    private int green = -1, black1F = 1024,black1B = 1024, white1F = -1, white1B = -1,
	power = 100, black2F = 1024,black2B = 1024, white2F = -1, white2B = -1,
	colorSensorMaxValue1 = -1, colorSensorMaxValue2 = -1,lastColor, ordinaroffset =-1;

    public void turn(int direction) { //turn with light sensor
	MP1.resetTachoCount();
	controlMotor(power*direction,-power*direction);
	while (light(FRONT) > ordinaroffset);
	controlMotor(0,0);
    }
    public void turn_old(int direction) { // turn with tacho
	MP1.resetTachoCount();
	controlMotor(power*direction,-power*direction);
	while (MP1.getTachoCount()*direction<324);
	controlMotor(0,0);
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
    public void pass() {move(300);}
    public void setPower(int power) {this.power = power;}
    public int getColor() {
	return lastColor;
    }
    public void followP(int direction, int terminate){
	float Kp = 1f;
	int Tp = power;

	int offset = ordinaroffset;
	if (direction == SHADOW)
	    offset = (int) (white1B/4+black1B/4*3);
	
	int bb = 0;
	while (!Button.ESCAPE.isDown()) {
	    int error = light(FRONT) - offset;
	    int Turn = (int) (Kp*error);
	    controlMotor(MP1,(Tp+Turn));
	    controlMotor(MP2,(Tp-Turn));
	    lastColor = determineColor();
	    if (terminate == 1 && lastColor > -1) {
		controlMotor(MP1,0);
		controlMotor(MP2,0);
		break;
	    }

	    if (terminate == 0 && light(FRONT) < offset - 40) 
		break;
	    else if (terminate == 2 && light(FRONT) < offset - 10) 
		break;
	    if (terminate == 3) {
		if (bb == 0 && light(FRONT) > offset)
		    bb = 1;
		else if (bb == 1 && light(FRONT) < offset)
		    bb = 2;
		if (bb == 2 && light(FRONT) > offset + 5) 
		    break;
	    }
	}
    }
    private void approach() {
	power=50;
	move(160);
	power=40;
	move(90);
	power=40;
    }
    public void turnSolar() {
	release(false);
	approach();
	move(100);
	grab(false);
	turn(LEFT);
	turn(LEFT);
	move(-195);
	release(false);
	move(-200);
	grab(false);
    }

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
	grab(true);
	controlMotor(MP1,power);
	controlMotor(MP2,power);
	green = light(FRONT);
	while (light(FRONT) < green+40);
	sleep(100);
	white1F = light(FRONT); // 1 LIGHT WHITE
	while (light(FRONT) > white1F-30);
	while (light(FRONT) < white1F-20) // 1 LIGHT BLACK
	    black1F = (light(FRONT) < black1F)?light(FRONT):black1F;
	white2F = light(BACK); // 2 LIGHT WHITE
	while (light(BACK) > white2F-40);
	while (light(BACK) < white2F-20) // 2 LIGHT BLACK
	    black2F = (light(BACK) < black2F)?light(BACK):black2F;
	turn(LEFT);
	turn(LEFT);
	white1B = light(FRONT); // 1 WHITE SHADOW
	white2B = light(BACK); // 2 WHITE SHADOW
	controlMotor(MP1,-power);
	controlMotor(MP2,-power);
	while (light(FRONT) > white1B-30);
	while (light(FRONT) < white1B-20) // 1 BLACK SHADOW
	    black1B = (light(FRONT) < black1B)?light(FRONT):black1B;
	controlMotor(MP1,power);
	controlMotor(MP2,power);
	while (light(BACK) > white2B-40);
	while (light(BACK) < white2B-20) // 2 BLACK SHADOW
	    black2B = (light(BACK) < black2B)?light(BACK):black2B;
	move(-80);
	turn(LEFT);
	ordinaroffset = (int) (white1F/8+black1F/8*7);
    }

    public void switche() {}

    public Move() {
	//# Setup
	M3.setSpeed(720);
	cs1.setFloodlight(Color.WHITE);
	cs2.setFloodlight(Color.WHITE);
    }
    
    private void controlMotor(MotorPort m, int value) {
	if      (value > 0 ) m.controlMotor(value,1);
	else if (value < 0 ) m.controlMotor(-1*value,2);
	else if (value == 0) m.controlMotor(value,3); //3 stop
	else if (value == -1) m.controlMotor(value,4); //4 float
    }
    private void sleep(int i) {try {Thread.sleep(i);}catch (Exception e){}}
    private int light(int end) {return ((end == LIGHT)?sensorF:sensorB).readNormalizedValue();}
    private void controlMotor(int value1, int value2) {
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