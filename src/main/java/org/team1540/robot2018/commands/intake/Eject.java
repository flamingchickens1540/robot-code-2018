package org.team1540.robot2018.commands.intake;

import edu.wpi.first.wpilibj.command.TimedCommand;
import org.team1540.robot2018.Robot;
import org.team1540.robot2018.Tuning;

public class Eject extends TimedCommand {

  private double speed;
  private double time;

  public Eject(double speed) {
    super(Tuning.intakeEjectTime);
    this.speed = speed;
    requires(Robot.intake);
  }

  public Eject(double speed, double time) {
    super(time);
    this.speed = speed;
    requires(Robot.intake);
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    Robot.intake.set(speed);
  }

  @Override
  protected void end() {
    Robot.intake.holdCube();
  }
}
