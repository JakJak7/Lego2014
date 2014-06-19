import lejos.nxt.*;

public class Navigator {
    private Move m;
    private int direction=1, grid=-1, solar=-1, reservePanels = 0;

    public Navigator(Move m) {this.m = m;}

    public void turnSolar() {
	m.turnSolar();
	m.move(200);
	direction*=-1;

    }
    public void replace(){
	//# Grap and RUN
	m.move(-10);
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
	    m.align(m.DOWN,m.RIGHT); // DOWN? .. left??
	    m.setPower(80);
	    m.followP(m.LEFT,m.tFRONT);
	    m.move(200);
	    m.turn(m.LEFT);
	    m.align(m.DOWN,m.RIGHT);
	}
	else if (grid == 2){
	    m.move(90);
	}
    	else if (grid == 3){
	    m.move(200);
	    m.turn(m.LEFT,70);
       	    m.align(m.RIGHT,m.RIGHT);
	    m.setPower(80);
	    m.followP(m.RIGHT,m.tFRONT);
	    m.move(200);
	    m.turn(m.RIGHT);
	    m.align(m.DOWN,m.RIGHT);	    
	}

	// Get home and replace
	m.setPower(100);
	m.followP(m.DOWN,m.tFRONT);
	m.setPower(50);
	m.move(250);
	m.turn(m.RIGHT);
	m.followP(m.LEFT,m.tFRONT);
	m.move(-300);
	m.followP(m.LEFT,5);
	m.controlMotor(0,0);
	
	m.setPower(50);
	m.release(true);
	m.move(250);
       	m.move(-200);
	//if first..
	if (reservePanels == 0) {
	    m.grab(false);
	    m.move(200);
	    m.release(false);
	    m.grapSolar();
	    m.setPower(70);
	    m.move(-800);
	    m.turn(m.RIGHT,70);
	}
	else if (reservePanels == 1) {
	    m.setPower(40);
	    //m.grab(true);
	    m.move(-400);
	    m.turn(m.RIGHT,70);
	    m.move(400);
	    m.turn(m.LEFT,70);
	    m.move(400);
	    m.grapSolar();
	    m.move(-800);
	    m.turn(m.RIGHT,70);
	}

	
	reservePanels++;
	m.setPower(100);
	m.align(m.UP,m.RIGHT);
	m.followP(m.UP,m.tFRONT);
	gotoGrid(grid);
	
	for (int i=solar;i>0;i--) {
	    m.followP(m.UP,m.tCOLOR);
	    m.pass();
	}
	m.setPower(40);
	m.releasetimer();
	m.followP(m.UP,m.tGRAY);
	m.move(230);
	m.move(-200);
	m.grab(false);
	m.followP(m.UP,m.tCOLOR);
    }

    public void gotoGrid(int i){
	if(i==1) {
	    m.move(180);
	    m.turn(m.RIGHT,70);
	    m.align(m.RIGHT,m.RIGHT);
	    m.setPower(70);
	    m.followP(m.RIGHT,5);
	    m.move(250);
	    m.turn(m.LEFT,70);
	    m.align(m.UP,m.RIGHT);
	    m.move(-100);
	}
	/* bedste tilfælde...
	  else if((grid == 2 && i==3)) {
	    m.move(150);
	    m.followP(m.DOWN,m.tFRONT);
	    m.move(280);
	    m.turn(m.LEFT,70);
	    m.move(600);
	    return;
	}
	*/
	else if((grid == 1 && i==2) || (grid == 2 && i==3)) {
	    m.move(550);
	    m.turn(m.RIGHT,70);
	    m.align(m.UP,m.RIGHT);
	    m.move(-100);
	}
	else if((grid == 2 && i==2)) {
	    m.move(90);
	}
	else if(grid == 3 && i==3) {
	    m.move(180);
	    m.turn(m.LEFT,70);
	    m.align(m.LEFT,m.RIGHT);
	    m.move(500);
	    m.turn(m.RIGHT,70);
	    m.align(m.UP,m.RIGHT);
	    m.move(-100);
	}
	else if(grid == 3 && i==4) {
	    m.setPower(90);
	    m.move(400);
	    m.turn(m.RIGHT,70);
	    m.align(m.DOWN,m.RIGHT);
	    m.followP(m.DOWN,m.tFRONT);
	    m.move(280);
	    m.turn(m.LEFT,70);
	    m.move(600);
	}
	grid = i;
    }

    public void fixRow() {
	direction = 1;
	m.followP(m.UP,m.tCOLOR);
	m.move(-350);
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
	solar=2;
	if (s[2] == 2)
	    turnSolar();

	int endposition = 0;
	if (direction == -1) {
	    m.followP(m.UP,m.tCOLOR);
	    if (s[0] == 2) {
		m.pass();
		m.followP(m.UP,m.tCOLOR);
		turnSolar();
		if (s[1] == 2) {
		    m.followP(m.UP,m.tCOLOR);
		    turnSolar();
		    endposition = 1;
		}
		else
		    endposition = 2;
	    }
	    else if (s[1] == 2) {
		turnSolar();
		endposition = 3;
	    }
	    else  {
		m.followP(m.DOWN,m.tCOLOR);
		m.pass();
		endposition=1;
	    }

	}
	else if (direction == 1) {
	    if (s[1] == 2) {
		m.move(-700);
		m.followP(m.UP,m.tCOLOR);
		turnSolar();
		if (s[0] == 2) {
		    m.followP(m.DOWN,m.tCOLOR);
		    turnSolar();
		    endposition = 2;
		}
		else
		    endposition = 1;
	    }
	    else if (s[0] == 2) {
		m.move(-1500);
		m.followP(m.UP,m.tCOLOR);
		turnSolar();
		endposition=4;
	    }
	    else
		m.move(-1400);
	}

	if(endposition==1) {
	    m.followP(m.DOWN,m.tCOLOR);
	    m.pass();
	    m.followP(m.DOWN,5);
	    m.move(280);
	}
	else if(endposition==2)
	    m.move(-400);
	else if(endposition==3)
	    m.move(-1200);
	else if(endposition==4) {
	    m.followP(m.DOWN,5);
	    m.move(280);
	}

	if (grid == 1 || grid == 2) {
	    if (direction == 1)
		m.turn(m.LEFT,70);
	    else
		m.turn(m.RIGHT,70);
	    m.align(m.LEFT,m.RIGHT);
	}
	else  {
	    if (direction == 1)
		m.turn(m.RIGHT,70);
	    else
		m.turn(m.LEFT,70);
	    m.align(m.RIGHT,m.RIGHT);
	}
    }
}