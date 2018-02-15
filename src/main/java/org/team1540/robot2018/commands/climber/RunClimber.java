package org.team1540.robot2018.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import org.team1540.robot2018.Robot;
import org.team1540.robot2018.Tuning;

public class RunClimber extends Command {
  private double speed;

  public RunClimber(double speed) {
    this.speed = speed;
    requires(Robot.winch);
    requires(Robot.tape);
  }
  @Override
  protected void execute() {
    Robot.winch.set(speed * Tuning.winchMultiplier);
    Robot.tape.set(speed * Tuning.tapeMeasureMultiplier);
  }

  @Override
  protected void end() {
    Robot.tape.set(0);
    Robot.winch.set(0);
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
