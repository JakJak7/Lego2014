import lejos.nxt.*;

public class Navigator {
    private final int LEFT=-1,RIGHT=1,FRONT=0,BACK=1;
    private Move m;
    private int cPos = 0, cC = 0;
    private int direction = 1; 

    public Navigator(Move m) {
	this.m = m;
    }
    public void turnSolar() throws Exception  {
	m.turnSolar();
	if (direction == -1)
	    passSolar();
	direction*=-1;
    }
    public void switche() throws Exception {
	m.switche();
	direction = 1;
    }
    public void passSolar() {
	if (cC != 3 && cC != 6 && cC != 9)
	    m.pass();
    }
    public void gotoGrid(int i) throws Exception {
	if (i == 1) {
	    m.followP(FRONT,0);
	    m.setValval();
	    m.move(260);
	    m.setPower(70);
	    m.turn(RIGHT);
	    m.followP(BACK,2);
	    m.setPower(50);
	    m.move(285);
	    m.turn(LEFT);
	    m.move(-250);
	}
    }
    public void navigateTo(int pos) throws Exception {
	cC = pos;
	if (cPos == 0 && pos == 1)//at home..
	    gotoGrid(1);

	if (pos == 1) {
	    m.setPower(38);
	    m.followP(FRONT,1);
	}	
	else if (pos == 2 || pos == 3) {
	    m.setPower(60);
	    if (direction == -1)
		m.move(-850);
	    m.setPower(38);
	    m.followP(FRONT,1);
	    if (direction == -1)
		m.revertColor();
	}
	cPos = pos;

    }
}