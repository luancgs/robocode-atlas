package luancgs;

import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngle;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * Atlas - a robot by @luancgs
 */
public class Atlas extends AdvancedRobot {
  static final double MAX_GUN_TURN = Double.POSITIVE_INFINITY;
  static final double MAX_IDLE_ROUNDS = 10;

  static final int STRATEGY_BATTLE_ROYALE = 1;
  static final int STRATEGY_LAST_STAND = 2;
  int strategy;
  boolean lastStand;
  double gunTurn;
  int idle;

  /**
   * run: Atlas's default behavior
   */
  public void run() {
    // Initialization of the robot should be put here
    setAdjustGunForRobotTurn(true);
    strategy = STRATEGY_BATTLE_ROYALE;
    gunTurn = MAX_GUN_TURN;
    idle = 0;

    // Set colors
    setBodyColor(new Color(85, 30, 145));
    setGunColor(new Color(220, 165, 90));
    setRadarColor(new Color(220, 165, 90));
    setScanColor(new Color(45, 220, 100));
    setBulletColor(new Color(220, 165, 90));

    // Main execution
    while (true) {
      if (getOthers() < 2) {
        strategy = STRATEGY_LAST_STAND;
        turnGunRightRadians(Double.POSITIVE_INFINITY);
      }
      getStrategyRun();
    }
  }

  /**
   * onScannedRobot: What to do when you see another robot
   */
  public void onScannedRobot(ScannedRobotEvent e) {
    getStrategyScanned(e);
  }

  /**
   * onHitByBullet: What to do when you're hit by a bullet
   */
  public void onHitByBullet(HitByBulletEvent e) {
    // back(10);
  }

  /**
   * onHitWall: What to do when you hit a wall
   */
  public void onHitWall(HitWallEvent e) {
    setTurnRight(e.getBearing());
    setBack(100);
    execute();
  }

  /**
   * onWin: What to do when you win the round
   */
  public void onWin(WinEvent e) {
    for (int i = 0; i < 30; i++) {
      turnRight(25);
      turnLeft(25);
    }
  }

  // **********************
  // Strategy Definitions
  // **********************

  private void getStrategyRun() {
    switch (strategy) {
      case STRATEGY_BATTLE_ROYALE:
        battleRoyaleRun();
        break;
      case STRATEGY_LAST_STAND:
        lastStandRun();
        break;
      default:
        battleRoyaleRun();
    }
  }

  private void getStrategyScanned(ScannedRobotEvent e) {
    switch (strategy) {
      case STRATEGY_BATTLE_ROYALE:
        battleRoyaleScanned(e);
        break;
      case STRATEGY_LAST_STAND:
        lastStandScanned(e);
        break;
      default:
        battleRoyaleScanned(e);
    }
  }

  private void battleRoyaleRun() {
    if (idle > MAX_IDLE_ROUNDS) {
      gunTurn = MAX_GUN_TURN;
    }

    setTurnGunRightRadians(gunTurn);
    execute();
    idle++;
  }

  private void lastStandRun() {
    if (idle > MAX_IDLE_ROUNDS) {
      gunTurn = MAX_GUN_TURN;
    }

    scan();
    idle++;
  }

  private void battleRoyaleScanned(ScannedRobotEvent e) {
    idle = 0;
    gunTurn = normalRelativeAngle(e.getBearingRadians() + (getHeadingRadians() - getGunHeadingRadians()));
    setTurnGunRightRadians(gunTurn);

    if (e.getDistance() > 200) {
      setTurnRight(e.getBearing());
      setAhead(e.getDistance() - 170);
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
        setBack(30);
      } else {
        setAhead(30);
      }
    }
    scan();
  }

  private void lastStandScanned(ScannedRobotEvent e) {
    idle = 0;
    gunTurn = getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians();
    setTurnGunRightRadians(normalRelativeAngle(gunTurn));

    setTurnRight(e.getBearing());
    setAhead(e.getDistance() - 80);

    if (e.getDistance() < 100) {
      fire(3);
    }
  }
}