package luancgs;
import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * Atlas - a robot by @luancgs
 */
public class Atlas extends AdvancedRobot
{
	double gunTurn;
	/**
	 * run: Atlas's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		gunTurn = 10;
		
		// Set colors
		setBodyColor(new Color(128, 128, 50));
		setGunColor(new Color(50, 50, 20));
		setRadarColor(new Color(200, 200, 70));
		setScanColor(Color.white);
		setBulletColor(Color.blue);

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			setTurnGunRight(gunTurn);
			this.changeColorByEnergy();
			execute();
		}
	}
	
	/**
	 * changeColorByEnergy: changes robot color based on how much energy it has left
	 */
	private void changeColorByEnergy() {
		double energy = getEnergy();
		if(energy > 70) {
			setColors(Color.blue, Color.blue, Color.blue);
		} else if(energy > 40) {
			setColors(Color.yellow, Color.yellow, Color.yellow);
		} else {
			setColors(Color.red, Color.red, Color.red);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		gunTurn = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		setTurnGunRight(gunTurn);
		
		if (e.getDistance() > 150) {
			fire(1);
			setTurnRight(e.getBearing());
			setAhead(e.getDistance() - 140);
			return;
		} else {
			fire(3);
			setTurnRight(e.getBearing() - 90);
			setAhead(100);
			scan();
			setBack(100);
		}

		if (e.getDistance() < 100) {
			if (e.getBearing() > -90 && e.getBearing() <= 90) {
				back(30);
			} else {
				ahead(30);
			}
		}
		scan();
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		setTurnRight(e.getBearing() + 90);
		setAhead(60);
	}	
}
