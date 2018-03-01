package org.team1540.robot2018;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Waypoint;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;
import org.opencv.core.Mat;
import org.team1540.base.adjustables.AdjustableManager;
import org.team1540.base.power.PowerManager;
import org.team1540.base.util.SimpleCommand;
import org.team1540.robot2018.commands.TankDrive;
import org.team1540.robot2018.commands.auto.AutonomousProfiling;
import org.team1540.robot2018.commands.auto.AutonomousProfiling.TrajectorySegment;
import org.team1540.robot2018.commands.auto.DriveBackward;
import org.team1540.robot2018.commands.elevator.JoystickElevator;
import org.team1540.robot2018.commands.elevator.MoveElevatorToPosition;
import org.team1540.robot2018.commands.groups.FrontScale;
import org.team1540.robot2018.commands.groups.GroundPosition;
import org.team1540.robot2018.commands.groups.IntakeSequence;
import org.team1540.robot2018.commands.intake.EjectCube;
import org.team1540.robot2018.commands.intake.OpenArms;
import org.team1540.robot2018.commands.wrist.JoystickWrist;
import org.team1540.robot2018.subsystems.ClimberWinch;
import org.team1540.robot2018.subsystems.DriveTrain;
import org.team1540.robot2018.subsystems.Elevator;
import org.team1540.robot2018.subsystems.Intake;
import org.team1540.robot2018.subsystems.IntakeArms;
import org.team1540.robot2018.subsystems.Wrist;

public class Robot extends IterativeRobot {
  public static final DriveTrain drivetrain = new DriveTrain();
  public static final Intake intake = new Intake();
  public static final IntakeArms intakeArms = new IntakeArms();
  public static final Elevator elevator = new Elevator();
  public static final Wrist wrist = new Wrist();
  public static final ClimberWinch winch = new ClimberWinch();

  private Command emergencyDriveCommand = new TankDrive();


  private SendableChooser<String> autoPosition = new SendableChooser<>();
  private SendableChooser<Boolean> driveMode = new SendableChooser<>();

  @Override
  public void robotInit() {
    AdjustableManager.getInstance().add(new Tuning());
    // AdjustableManager.getInstance().add(drivetrain);
    PowerManager.getInstance().setRunning(false);

    autoPosition.addDefault("Middle", "Middle");
    autoPosition.addObject("Left", "Left");
    autoPosition.addObject("Right", "Right");
    autoPosition.addObject("Stupid", "Stupid");

    driveMode.addDefault("PID Drive", false);
    driveMode.addObject("Manual Override", true);
    SmartDashboard.putData("[Drivetrain] ***** DRIVE OVERRIDE *****", driveMode);

    // configure controls
    OI.autoIntakeButton.whenPressed(new IntakeSequence());
    OI.autoEjectButton.whenPressed(new EjectCube());
    OI.stopIntakeButton.whenPressed(new SimpleCommand("Stop intake", intake::stop, intake));

    OI.autoIntakeButton.whileHeld(new OpenArms());

    OI.elevatorExchangeButton.whenPressed(new MoveElevatorToPosition(Tuning.elevatorExchangePosition));

    OI.elevatorSwitchButton.whenPressed(new MoveElevatorToPosition(Tuning.elevatorFrontSwitchPosition));
    OI.elevatorRaiseButton.whenPressed(new MoveElevatorToPosition(Tuning.elevatorScalePosition));
    OI.elevatorFrontScaleButton.whenPressed(new FrontScale());
    OI.elevatorLowerButton.whenPressed(new GroundPosition());

    OI.enableElevatorAxisControlButton.whileHeld(new JoystickElevator());
    OI.enableWristAxisControlButton.whileHeld(new JoystickWrist());


    OI.winchInSlowButton.whileHeld(new SimpleCommand("Winch In Low", () -> winch.set(Tuning.winchInLowVel), winch));

    OI.winchInFastButton.whileHeld(new SimpleCommand("Winch In High", () -> winch.set(Tuning.winchInHighVel), winch));

    // configure SmartDashboard
    Command zeroWrist = new SimpleCommand("[Wrist] Zero Wrist", wrist::resetEncoder);
    zeroWrist.setRunWhenDisabled(true);
    SmartDashboard.putData(zeroWrist);

    Command zeroElevator = new SimpleCommand("[Elevator] Zero Elevator", elevator::resetEncoder);
    zeroElevator.setRunWhenDisabled(true);
    SmartDashboard.putData(zeroElevator);

    new Thread(() -> {
      UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(Tuning.camID);
      camera.setResolution(640, 480);

      CvSink cvSink = CameraServer.getInstance().getVideo();
      CvSource outputStream = CameraServer.getInstance().putVideo(
          "Camera " + Tuning.camID, 640, 480);

      Mat source = new Mat();
      Mat output = new Mat();

      while (!Thread.interrupted()) {
        cvSink.grabFrame(source);
        // Point pt1 = new Point(source.width() / 2 + Tuning.crosshairsSize, source.height() / 2);
        // Point pt2 = new Point(source.width() / 2 - Tuning.crosshairsSize, source.height() / 2);
        // Point pt3 = new Point(source.width() / 2, source.height() / 2 + Tuning.crosshairsSize);
        // Point pt4 = new Point(source.width() / 2, source.height() / 2 - Tuning.crosshairsSize);
        // Imgproc.line(source, pt1, pt2, new Scalar(0, 255, 0), Tuning.crosshairsThicccness);
        // Imgproc.line(source, pt3, pt4, new Scalar(0, 255, 0), Tuning.crosshairsThicccness);
        outputStream.putFrame(source);
      }
    }).start();
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void autonomousInit() {
    switch (autoPosition.getSelected()) {
      case "Left":
        new CommandGroup() {
          {
            if (MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.LEFT) {
              addSequential(new AutonomousProfiling(new TrajectorySegment(
                  new Waypoint(0, 0, 0),
                  new Waypoint(120, 50, 0), false)));
              addSequential(new EjectCube());
            } else {
              addSequential(new AutonomousProfiling(new TrajectorySegment(
                  new Waypoint(0, 0, 0),
                  new Waypoint(134, 0, 0), false))); // go straight
            }
          }
        }.start();
        break;

      case "Middle":
        new CommandGroup() {
          {
            if (MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.LEFT) {
              addSequential(new AutonomousProfiling(new TrajectorySegment(
                  new Waypoint(0, 0, 0),
                  new Waypoint(112, -98, 0), false)));
              addSequential(new EjectCube());
            } else if (MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.RIGHT) {
              addSequential(new AutonomousProfiling(new TrajectorySegment(
                  new Waypoint(0, 0, 0),
                  new Waypoint(106, 82, 0), false)));
              addSequential(new EjectCube());
            } else {
              DriverStation.reportError("Match data could not get owned switch side, reverting to base auto", false);
              addSequential(new DriveBackward(Tuning.stupidDriveTime));
            }
          }
        }.start();
        break;

      case "Right":
        new CommandGroup() {
          {
            addSequential(new AutonomousProfiling(new TrajectorySegment(
                new Waypoint(0, 0, 0),
                new Waypoint(134, 0, 0), false)));
            if (MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.RIGHT) {
              addSequential(new EjectCube());
            }
          }
        }.start();
        break;

      case "Stupid":
        new DriveBackward(Tuning.stupidDriveTime).start();
        break;
    }
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();

    // for drive override
    if (driveMode.getSelected()) {
      // oh no encoders broke
      emergencyDriveCommand.start();
    } else {
      emergencyDriveCommand.cancel();
    }
  }
}
