package org.team1540.robot2018.commands.elevator;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import org.team1540.robot2018.Robot;
import org.team1540.robot2018.Tuning;
import org.team1540.robot2018.commands.wrist.MoveWristToPosition;

public class MoveElevatorToPosition extends CommandGroup {

  public MoveElevatorToPosition(double target) {
    addSequential(new ConditionalCommand(new MoveWristToPosition(Tuning.wristTransitPosition)) {
      @Override
      protected boolean condition() {
        return ((target > Tuning.elevatorObstaclePosition
            && target < Tuning.elevatorObstacleUpperPosition)
            || ((target > Tuning.elevatorObstacleUpperPosition
            && Robot.elevator.getPosition() < Tuning.elevatorObstacleUpperPosition)
            || (target < Tuning.elevatorObstaclePosition
            && Robot.elevator.getPosition() > Tuning.elevatorObstaclePosition)))
            && Robot.wrist.getPosition() < Tuning.wristTransitPosition;
      }
    });
    addSequential(new MoveElevator(target));
  }
}