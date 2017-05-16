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


public class Building extends Environment {

	public static final int INJURED  = 16;
	public static final int GSize=10;
	
	private BuildingModel model;
    private BuildingView  view;
	
	public static final Term    s1 = Literal.parseLiteral("next(security_1)");
	public static final Term    s2 = Literal.parseLiteral("next(security_2)");
    public static final Term    grabInj = Literal.parseLiteral("grag(injured)");
    public static final Term    dropInj = Literal.parseLiteral("drop(injured)");
    public static final Term    healInj = Literal.parseLiteral("heal(injured)");
	
	//public static final Literal inj1 = Literal.parseLiteral("injured(?)");
    //public static final Literal inj2 = Literal.parseLiteral("injured(?)");

    private Logger logger = Logger.getLogger("SmartBuilding.mas2j."+Building.class.getName());

    /** Called before the MAS execution with the args informed in .mas2j */

    @Override

    public void init(String[] args) {
        super.init(args);
		model = new BuildingModel();
		view = new BuildingView(model);
		model.setView(view);
		//updatePercepts();
    }
	
    @Override
    public boolean executeAction(String agName, Structure action) {

        //logger.info("executing: "+action+", but not implemented!");
		
		try {
            if (action.equals(s1)) {
                model.nextSlot(1);
            }else if (action.equals(s2)){
				model.nextSlot(2);	
			}else if (action.getFunctor().equals("move_towards")) {
                int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                model.moveTowards(x,y);
			}else if (action.equals(grabInj)){
                // model.grabInj();
            }else if (action.equals(dropInj)){
                // model.dropInj();
            }else if (action.equals(healInj)){
                // model.healInj();
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
		Location r5Loc = model.getAgPos(4);
        Location r6Loc = model.getAgPos(5);
        
        Literal pos1 = Literal.parseLiteral("pos(r1," + r1Loc.x + "," + r1Loc.y + ")");
        Literal pos2 = Literal.parseLiteral("pos(r2," + r2Loc.x + "," + r2Loc.y + ")");
		Literal pos3 = Literal.parseLiteral("pos(r3," + r3Loc.x + "," + r3Loc.y + ")");
        Literal pos4 = Literal.parseLiteral("pos(r4," + r4Loc.x + "," + r4Loc.y + ")");
		Literal pos5 = Literal.parseLiteral("pos(r5," + r5Loc.x + "," + r5Loc.y + ")");
        Literal pos6 = Literal.parseLiteral("pos(r6," + r6Loc.x + "," + r6Loc.y + ")");

        addPercept(pos1);
        addPercept(pos2);
		addPercept(pos3);
        addPercept(pos4);
		addPercept(pos5);
        addPercept(pos6);
    }

    /** Called before the end of MAS execution */

    @Override
    public void stop() {
        super.stop();
    }
	
	class BuildingModel extends GridWorldModel {

		boolean param_1_hasGarb = false;
		boolean param_2_hasGarb = false;
		boolean param_3_hasGarb = false;
		
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
				setAgPos(4, 9, 1);
				setAgPos(5, 9, 2);
				// Injured
				setAgPos(INJURED, 5, 5);
				setAgPos(INJURED, 6, 4);
            } catch (Exception e) {
                e.printStackTrace();
            }
			add(INJURED, 5, 5);
			add(INJURED, 0, 1);
			add(INJURED, 6, 8);
			add(INJURED, 7, 2);
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
			setAgPos(4, getAgPos(4));
			setAgPos(5, getAgPos(5));
		}
		
		void moveTowards(int x, int y) throws Exception {
            // I am using here only paramedic_1 with ID 3
			Location par1 = getAgPos(3);
			Location par2 = getAgPos(4);
			Location par3 = getAgPos(5);
			
			if(par1.x < x)
				par1.x++;
			else if (par1.x > x)
                par1.x--;
            if (par1.y < y)
                par1.y++;
            else if (par1.y > y)
                par1.y--;
			setAgPos(3, par1);
			setAgPos(4, getAgPos(4));
			setAgPos(5, getAgPos(5));
        }
		
		void grabInj() {
            // TODO
        }
        void dropInj() {
            // TODO
        }
        void healInj() {
            // TODO
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
				default: label="P"; break;
			}
			label=label+(id+1);
			c = Color.blue;
            if (id == 0) {
                c = Color.yellow;
                /*if (((MarsModel)model).r1HasGarb) {
                    label += " - G";
                    c = Color.orange;
                }*/
            }
			if (id == 3 || id ==4 || id == 5) {
                c = Color.red;
                /*if (((MarsModel)model).r1HasGarb) {
                    label += " - G";
                    c = Color.orange;
                }*/
            }
			super.drawAgent(g, x, y, c, -1);
           /* if (id == 0) { 
                g.setColor(Color.black);
            } else if (id == 1) {
                g.setColor(Color.white);                
            }*/
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
}


