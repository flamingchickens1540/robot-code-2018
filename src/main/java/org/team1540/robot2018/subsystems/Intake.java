package org.team1540.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team1540.base.wrappers.ChickenVictor;
import org.team1540.robot2018.Robot;
import org.team1540.robot2018.RobotMap;
import org.team1540.robot2018.commands.intake.HoldIntake;

public class Intake extends Subsystem {
  PowerDistributionPanel pdp = new PowerDistributionPanel();

  private ChickenVictor intake_1 = new ChickenVictor(RobotMap.intake_1);
  private ChickenVictor intake_2 = new ChickenVictor(RobotMap.intake_2);
  private double priority = 10;

  public Intake() {
    intake_1.setInverted(true);
    intake_2.setInverted(true);
  }

  public double getCurrent() {
    return pdp.getCurrent(10) + pdp.getCurrent(11) - Robot.wrist.getCurrent();
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new HoldIntake());
  }

  public void set(double value) {
    intake_1.set(ControlMode.PercentOutput, value);
    intake_2.set(ControlMode.PercentOutput, value);
  }

  public void set(double aValue, double bValue) {
    intake_1.set(ControlMode.PercentOutput, aValue);
    intake_2.set(ControlMode.PercentOutput, bValue);
  }

  public void stop() {
    intake_1.set(ControlMode.PercentOutput, 0);
    intake_2.set(ControlMode.PercentOutput, 0);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("1 current", getCurrent());
  }
}