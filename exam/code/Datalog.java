import lejos.nxt.*;
import java.io.*;

public class Datalog implements Runnable {
    private LightSensor sample = new LightSensor(SensorPort.S1,true);
    public String extra = "";
    private File f;
    private FileOutputStream fos;
    public void writeS(String s) {
	try {
	    for(int i=0; i<s.length(); i++)
		fos.write((byte)s.charAt(i));
	}
	catch (Exception e) {}
    }
    private boolean runner = true;
    public void run() {
	int i =0;
	while (!Button.ESCAPE.isDown() && runner) {
	    writeS(i+"\t"+sample.readNormalizedValue()+extra+"\n");
	    i++;
	    try {
		Thread.sleep(10);
	    }
	    catch(Exception e){}
	}
	if (!runner)
	    runner = true;

    }
    public Datalog() throws Exception {
	f = new File("Datalog.txt");
	if (f.exists()) f.delete();
	f.createNewFile();
	fos = new FileOutputStream(f);
	writeS("sample\tvalue\n");	
    }
    public void close() throws Exception {
	runner = false;
	while (!runner);
	fos.close();
    }
}

