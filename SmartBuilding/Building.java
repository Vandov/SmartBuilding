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

	public static final int GSize=10;
	private BuildingModel model;
    private BuildingView  view;
	
    private Logger logger = Logger.getLogger("SmartBuilding.mas2j."+Building.class.getName());

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);
		model = new BuildingModel();
		view = new BuildingView(model);
        //addPercept(ASSyntax.parseLiteral("percept(demo)"));
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        logger.info("executing: "+action+", but not implemented!");
        if (true) { // you may improve this condition
             informAgsEnvironmentChanged();
        }
        return true; // the action was executed with success 
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
                setAgPos(1, 0, 0);
				setAgPos(2, 9, 9);
				// Paramedic
				setAgPos(3, 9, 1);
				setAgPos(4, 8, 0);
				setAgPos(5, 9, 0);
            } catch (Exception e) {
                e.printStackTrace();
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
                //case Building.GARB: drawGarb(g, x, y);  break;
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

    }
}

