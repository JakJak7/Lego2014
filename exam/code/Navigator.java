import lejos.nxt.*;

public class Navigator {
    private Move m;
    private int direction=1, grid=-1, solar=-1;

    public Navigator(Move m) {this.m = m;}

    public void turnSolar() {
	m.turnSolar();
	m.move(200);
	m.align(m.LIGHT,m.LEFT);
	if (solar != 0) {
	    m.followP(m.LIGHT,m.tCOLOR);
	    m.move(-400);
	    m.followP(m.LIGHT,m.tCOLOR);
	}
	else
	    m.followP(m.LIGHT,m.tFRONT);
	m.pass();
	m.followP(m.LIGHT,m.tFRONT);
	m.pass();
	m.followP(m.LIGHT,m.tFRONT);
	m.controlMotor(0,0);
	m.sleep(300);
	direction*=-1;
    }
    public void replace(){
	Sound.buzz();
	m.grapSolar();

	if (direction == 1) {
	    m.turn(m.LEFT);
	    m.turn(m.LEFT);
	}
	direction = 1;
    }

    public void gotoGrid(int i){
	if (i == 1) {
	    m.setPower(100);
	    m.followP(m.LIGHT,m.tFRONT);
	    m.setColorSensorMaxValue();
	    m.move(250);
	    m.turn(m.RIGHT,70);
	    m.align(m.SHADOW,m.LEFT);
	    m.setPower(70);
	    m.followP(m.SHADOW,m.tFRONT);
	    Sound.beep();
	    m.move(250);
	    m.turn(m.LEFT,70);
	    m.align(m.LIGHT,m.LEFT);
	    m.move(-250);
	    m.followP(m.LIGHT,m.tCOLOR);
	    m.move(-400);
	}
    }

    public void fixRow() {
	//1. Drive to the end of the row and remember the states of the solarpannels
	m.followP(m.LIGHT,m.tCOLOR);
	int[] s = new int[3];
	for (solar = 0;solar<=2;solar++) {
	    s[solar] = m.getColor();
	    if (solar < 2) {
		m.pass();
		m.followP(m.LIGHT,m.tCOLOR);
	    }	    
	}

	if (s[2] == 1)
	    m.move(-500);
	else if (s[2] == 2)
	    turnSolar();
	else
	    replace();
		
    }
}