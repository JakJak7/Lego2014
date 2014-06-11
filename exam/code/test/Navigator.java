import lejos.nxt.*;

public class Navigator {
    private Move m;
    private int cPos = 0, cC = 0;
    private int direction = 1; 

    public Navigator(Move m) {
	this.m = m;
    }

    public void turnSolar() {
	m.turnSolar();
    }
    public void switche(){
	m.switche();
	direction = 1;
    }
    public void passSolar() {
	m.pass();
    }
    private void gotoGrid(int i){
	if (i == 1) {
	    m.followP(m.LIGHT,0);
	    m.setColorSensorMaxValue();
	    m.setPower(40);
	    m.followP(m.LIGHT,4);
	    m.turn(m.RIGHT);
	    m.setPower(70);
	    m.followP(m.SHADOW,2);
	    m.move(50);
	    m.setPower(40);
	    m.followP(m.SHADOW,4);
	    m.turn(m.LEFT);
	    //m.align(m.LEFT,true);
	    m.setPower(70);
	    m.move(-250);
	}
    }
    private void lala() {
	m.followP(m.LIGHT,1);
	m.move(-200);
	m.followP(m.LIGHT,1);
    }
    private void lala2() {
	m.followP(m.SHADOW,1);
	m.move(-200);
	m.followP(m.SHADOW,1);
    }
    public void navigateTo(int pos){
	gotoGrid(1);
	lala();
	Sound.beep();
	m.pass();
	lala();
	Sound.beep();
	m.pass();
	lala();
	Sound.beep();
	turnSolar();
	
	m.followP(m.SHADOW,1);

	m.pass();
	lala2();
	Sound.beep();
	m.pass();
	lala2();
	Sound.beep();
	turnSolar();

	lala();
	m.pass();
	lala();
	Sound.beep();
	turnSolar();

	lala2();
	m.pass();
	m.followP(m.SHADOW,2);
	Sound.buzz();
	/*	m.pass();
	m.followP(m.LIGHT,2);
	Sound.beep();
	m.setPower(40);
	m.followP(m.LIGHT,4);
	m.turn(m.RIGHT);
	/*
	cC = pos;
	if (cPos == 0 && pos == 1)//at home..
	    gotoGrid(1);

	if (pos == 1) {
	    lala();
	}	
	else if (pos == 2 || pos == 3) {
	    m.pass();
	    lala();
	}
	*/
    }
}