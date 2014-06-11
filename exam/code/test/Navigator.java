import lejos.nxt.*;

public class Navigator {
    private Move m;
    private int cPos = 0, cC = 0;
    private int direction = 1; 

    public Navigator(Move m) {
	this.m = m;
    }
    private void gotoend() {
	for (int i=0;i<cC;i++) {
	    passSolar();
	    if (i==1) 
		m.followP(m.FRONT,2);
	    else
		m.followP(m.FRONT,1);
	}
    }
    public void turnSolar() {
	m.turnSolar();
	gotoend();
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
	    m.followP(m.LIGHT,4);
	    m.turn(m.RIGHT);
	    m.setPower(100);
	    m.followP(m.SHADOW,2);
	    m.move(70);
	    m.followP(m.SHADOW,4);
	    m.turn(m.LEFT);
	    m.move(-250);
	    
	}
    }
    public void navigateTo(int pos){
	cC = pos;
	if (cPos == 0 && pos == 1)//at home..
	    gotoGrid(1);

	if (pos == 1) {
	    m.followP(m.LIGHT,1);
	}	
	else if (pos == 2 || pos == 3) {
	    m.setPower(60);
	    if (direction == -1)
		m.move(-850);
	    m.setPower(38);
	    m.followP(m.LIGHT,1);
	    if (direction == -1)
		m.revertColor();
	}
	cPos = pos;

    }
}