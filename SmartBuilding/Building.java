import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;


public class Building extends Environment {

	public static final int INJURED=16;
	public static final int GSize=10;

	public static final Term    s1 = Literal.parseLiteral("next(security_1)");
	public static final Term    s2 = Literal.parseLiteral("next(security_2)");
    public static final Term    healInj = Literal.parseLiteral("heal(injured)");
	public static final Term	look1 = Literal.parseLiteral("lookAround(security_1)");
	public static final Term	look2 = Literal.parseLiteral("lookAround(security_2)");
	public static final Term	work = Literal.parseLiteral("startWorking(paramedic_1)");

	public static Location StartPos_Param1;

    static Logger logger = Logger.getLogger(Building.class.getName());

	private BuildingModel model;
    private BuildingView  view;
	private List<Injured> injureds = new ArrayList<>();
    /** Called before the MAS execution with the args informed in .mas2j */

    @Override
    public void init(String[] args) {
		model = new BuildingModel();
		view = new BuildingView(model);
		model.setView(view);
		updatePercepts();
    }

    @Override
    public boolean executeAction(String agName, Structure action) {

        logger.info(agName + " doing: " + action);
		try {
            if (action.equals(s1)) {
                model.nextSlot(1);
            }else if (action.equals(s2)){
				model.nextSlot(2);
			}else if (action.getFunctor().equals("move_towards")) {
                int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                model.moveTowards(x,y);
            }else if (action.equals(healInj)){
				model.healInj();
            }else if (action.equals(look1)){
                model.lookAround(1);
            }else if (action.equals(look2)){
                model.lookAround(2);
            }else if(action.equals(work)){
				model.notifyParamedics();
			}
        } catch (Exception e) {
            e.printStackTrace();
        }

		updatePercepts();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
        informAgsEnvironmentChanged();
        return true;
    }

	void updatePercepts() {
        clearPercepts();

        Location r1Loc = model.getAgPos(0);
        Location r2Loc = model.getAgPos(1);
		Location r3Loc = model.getAgPos(2);
        Location r4Loc = model.getAgPos(3);

        Literal pos1 = Literal.parseLiteral("pos(doorman," + r1Loc.x + "," + r1Loc.y + ")");
        Literal pos2 = Literal.parseLiteral("pos(security_1," + r2Loc.x + "," + r2Loc.y + ")");
		Literal pos3 = Literal.parseLiteral("pos(security_2," + r3Loc.x + "," + r3Loc.y + ")");
        Literal pos4 = Literal.parseLiteral("pos(paramedic_1," + r4Loc.x + "," + r4Loc.y + ")");

        addPercept(pos1);
        addPercept(pos2);
		addPercept(pos3);
        addPercept(pos4);
    }

    /** Called before the end of MAS execution */

    @Override
    public void stop() {
        super.stop();
    }

	class BuildingModel extends GridWorldModel {

		private BuildingModel() {
			// Size of the map, num of agents to display
            super(GSize, GSize, 6);

			try {
				// Agent with ID 0, and pos(0,0)
				// Doorman
                setAgPos(0, 4, 9);
				// Security
                setAgPos(1, 2, 2);
				setAgPos(2, 7, 7);
				// Paramedic
				setAgPos(3, 9, 0);

				StartPos_Param1 = getAgPos(3);

            } catch (Exception e) {
                e.printStackTrace();
            }
			add(INJURED, 7, 7);
			add(INJURED, 0, 1);
			add(INJURED, 8, 3);
		}

		void nextSlot(int securityID) throws Exception {
			Location sec;
			if(securityID == 1){
				sec = getAgPos(1);
			}else {
				sec = getAgPos(2);
			}
			if(sec.x != 2 && sec.y == getHeight()-3){ // bottom side
				sec.x--;
			}else if (sec.x == getWidth()-3 && sec.y != getHeight()-3) { // right side
                sec.y++;
            }else if (sec.x == 2 && sec.y != 2){ // left side
				sec.y--;
			}else if(sec.x != getWidth()-3 && sec.y == 2){ // top side
				sec.x++;
			}else{
				return;
			}
			if(securityID ==1){
				setAgPos(1, sec);
				setAgPos(1, getAgPos(1)); // just to draw it in the view
			} else {
				setAgPos(2, sec);
				setAgPos(2, getAgPos(2)); // just to draw it in the view
			}
			setAgPos(0, getAgPos(0));
			setAgPos(3, getAgPos(3));
		}

		void lookAround(int id) throws Exception {
			if(id == 1){
				Location sec1 = getAgPos(1);
				for(int x = sec1.x-2; x < sec1.x+2; x++){
					for(int y = sec1.y-2; y < sec1.y+2; y++){
						if (model.hasObject(INJURED, x, y)){
							boolean found = false;
							for(int i = 0; i < injureds.size(); i++){
								if(injureds.get(i).x == x && injureds.get(i).y == y){
									found = true;
								}
							}
							if(!found){
								logger.info("injured found by security_1 at " + x + ":" + y);
								injureds.add(new Injured(x,y));
							}
						}
					}
				}
			}else if (id == 2){
				Location sec2 = getAgPos(2);
				for(int x = sec2.x-2; x < sec2.x+2; x++){
					for(int y = sec2.y-2; y < sec2.y+2; y++){
						if (model.hasObject(INJURED, x, y)){
							boolean found = false;
							for(int i = 0; i < injureds.size(); i++){
								if(injureds.get(i).x == x && injureds.get(i).y == y){
									found = true;
								}
							}
							if(!found){
								logger.info("injured found by security_2 at " + x + ":" + y);
								injureds.add(new Injured(x,y));
							}
						}
					}
				}
			}
		}

		void notifyParamedics() throws Exception{
			if(injureds.size()!=0){
				model.moveTowards(injureds.get(0).x, injureds.get(0).y);
				Location par = getAgPos(3);
				if (injureds.get(0).x == par.x && injureds.get(0).y == par.y){
					healInj();
				}
			}else{
				model.moveTowards(9, 0);
			}
		}

		void moveTowards(int x, int y) throws Exception {
            // I am using here only paramedic_1 with ID 3
			Location par1 = getAgPos(3);

			if(par1.x < x)
				par1.x++;
			else if (par1.x > x)
                par1.x--;
            if (par1.y < y)
                par1.y++;
            else if (par1.y > y)
                par1.y--;
			setAgPos(3, par1);
        }

        void healInj() {
			if(model.hasObject(INJURED, getAgPos(3))){
				remove(INJURED, getAgPos(3));
				injureds.remove(0);
			}
        }
    }

	class BuildingView extends GridWorldView {

        public BuildingView(BuildingModel model) {
            super(model, "Smart Building", 600);
            defaultFont = new Font("Arial", Font.BOLD, 18); // change default font
            setVisible(true);
            repaint();
        }

        /** draw application objects */
        @Override
        public void draw(Graphics g, int x, int y, int object) {
            switch (object) {
                case Building.INJURED: drawInjured(g, x, y);  break;
            }
        }

        @Override
        public void drawAgent(Graphics g, int x, int y, Color c, int id) {
			String label;
			switch(id){
				case 0: label="D"; break;
				case 1: label="S"; break;
				case 2: label="S"; break;
				case 3: label="P"; break;
				default: label="X"; break;
			}
			label=label+(id+1);
			c = Color.blue;
            if (id == 0) {
                c = Color.yellow;
            }
			if (id == 3) {
                c = Color.red;
            }
			super.drawAgent(g, x, y, c, -1);
			g.setColor(Color.black);
            super.drawString(g, x, y, defaultFont, label);
            repaint();
        }

		public void drawInjured(Graphics g, int x, int y) {
            super.drawObstacle(g, x, y);
            g.setColor(Color.white);
            drawString(g, x, y, defaultFont, "I");
        }
    }

	class Injured {
		public int x;
		public int y;

		public Injured(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
}
