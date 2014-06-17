import lejos.nxt.*;

public class Navigator {
    private Move m;
    private int direction=1, grid=-1, solar=-1;

    public Navigator(Move m) {this.m = m;}

    public void turnSolar() {
	m.turnSolar();
	m.move(200);
	
	if (solar != 0) 
	    m.followP(m.UP,m.tCOLOR);
	else
	    m.followP(m.UP,m.tFRONT);
	m.pass();
	m.followP(m.UP,m.tCOLOR);
	m.pass();
	m.followP(m.UP,m.tFRONT);
	m.controlMotor(0,0);
	m.sleep(300);

	direction*=-1;
    }
    public void replace(){
	//# Grap and RUN
	m.grapSolar();
	m.turnsolar();
	m.align(m.DOWN,m.RIGHT);
	for (int i=solar;i>=0;i--)
	    if (i == 0)
		m.followP(m.DOWN,5);
	    else {
		m.followP(m.DOWN,m.tCOLOR);
		m.pass();
	    }
	if (grid == 1) {
	    m.move(200);
	    m.turn(m.RIGHT);
	    m.setPower(100);
	    m.followP(m.LEFT,m.tFRONT);
	    m.move(200);
	    m.turn(m.LEFT);
	}
	else if (grid == 2);
    	else if (grid == 3);//m.turn(m.LEFT);

	// Get home and replace
	m.setPower(100);
	m.followP(m.DOWN,m.tFRONT);
	m.setPower(50);
	m.move(220);
	m.turn(m.RIGHT);
	m.followP(m.LEFT,m.tFRONT);
	m.controlMotor(0,0);
	Sound.beep();
	//if first..
	int reservePanels = 0;
	if (reservePanels == 0) {
	    m.setPower(40);
	    m.move(70);
	    m.offset_left-=10;
	    m.followP(m.LEFT,m.tCOLOR);
	    m.offset_left+=10;
	    m.release(false);
	    m.move(-200);
	    m.grab(false);
	    m.move(200);
	    m.release(false);

	    m.grapSolar();
	    m.setPower(70);
	    m.move(-950);
	}
	else; //2 og 3..
	m.turn(m.RIGHT,70);
	m.setPower(100);
	m.align(m.UP,m.LEFT);
	m.followP(m.UP,m.tFRONT);
	gotoGrid(grid);
	
	for (int i=solar;i>0;i--) {
	    m.followP(m.UP,m.tCOLOR);
	    m.pass();
	}

	m.followP(m.UP,m.tGRAY);
	m.move(430);
	m.release(false);
	m.move(-200);
	m.grab(false);
	m.followP(m.UP,m.tCOLOR);
    }

    public void gotoGrid(int i){
	if(i==1) {
	    m.move(200);
	    m.turn(m.RIGHT,70);
	    m.setPower(70);
	    m.followP(m.RIGHT,m.tFRONT);
	    m.move(250);
	    m.turn(m.LEFT,70);
	    m.align(m.UP,m.LEFT);
	    m.move(-150);
	}
	else if(i==2)
	    m.move(30);
	else if(i==3);
	grid = i;
    }

    public void fixRow() {
	m.followP(m.UP,m.tCOLOR);
	m.move(-400);
	//1. Drive to the end of the row and remember the states of the solarpannels
	// unless it's inactive then replace and mark it as working
	m.followP(m.UP,m.tCOLOR);
	int[] s = new int[3];
	for (solar = 0;solar<=2;solar++) {
	    s[solar] = m.getColor();
	    if (s[solar] == 0) {
		m.controlMotor(0,0);
		Sound.buzz();
		replace();
		s[solar] = 1;
	    }
	    if (solar < 2) {
		m.pass();
		m.followP(m.UP,m.tCOLOR);
	    }
	}

	if (s[2] == 2)
	    turnSolar();
	else
	    m.move(-500);

	if (direction == -1) {
	    if (s[0] == 2) {
		// GOTO 0, and turn ..
		if (s[1] == 2) {
		    //move forward and turn 1
		}
	    }
	    else if (s[1] == 2) {
		// GOTO 2, and finish
	    }
	    //move backwards out of grid 1
	}
	else if (direction == 1) {
	    if (s[1] == 2) {
		//move backwards and turn 1
		if (s[0] == 2) {
		    //move backwards and turn 0
		}
	    }
	    else if (s[0] == 2) {
		//move backwards and turn 1
	    }
	    //move backwards out of grid 1
	}
    }
}