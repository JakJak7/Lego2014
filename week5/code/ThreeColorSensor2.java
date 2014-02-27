import lejos.nxt.*;

public class ThreeColorSensor2 {

    private LightSensor ls; 
    private int blackLightValue;
    private int whiteLightValue;
    private int greenLightValue;
    private int blackGreenThreshold;
    private int greenWhiteThreshold;
               
    public ThreeColorSensor2(SensorPort p) {
	ls = new LightSensor(p); 
	// Use the light sensor as a reflection sensor
	ls.setFloodlight(true);
    }

    private int read(String color){
	   
	int lightValue=0;
	   
	while (Button.ENTER.isDown());
	   
	LCD.clear();
	LCD.drawString("Press ENTER", 0, 0);
	LCD.drawString("to callibrate", 0, 1);
	LCD.drawString(color, 0, 2);
	while( !Button.ENTER.isPressed() ){
	    lightValue = ls.readValue();
	    LCD.drawInt(lightValue, 4, 10, 2);
	    LCD.refresh();
	}
	return lightValue;
    }
   
    public void calibrate() {
	//	blackLightValue = read("black");
	//      whiteLightValue = read("white");
	greenLightValue = read("green");
	// The threshold is calculated as the median between 
	// the two readings over the two types of surfaces
	//blackWhiteThreshold = (blackLightValue+whiteLightValue)/2;
	set(blackLightValue,whiteLightValue,greenLightValue);
    }
    public void set(int black, int white, int green) {
	blackGreenThreshold = green - 2;
	greenWhiteThreshold = green + 2;
    }
    public boolean green() {
	return (light() <= greenLightValue+2 && light() >= greenLightValue-2);
    }

    public boolean black() {
	return (ls.readValue() < blackGreenThreshold);
    }
   
    public boolean white() {
	return (ls.readValue() > greenWhiteThreshold);
    }
   
    public int light() {
	return ls.readValue();
    }
   
}
