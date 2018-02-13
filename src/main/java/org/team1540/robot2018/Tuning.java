package org.team1540.robot2018;

import org.team1540.base.adjustables.Tunable;

public class Tuning {
  public static final double MAX_ELEVATOR_DEVIATION = 200;
  public static final double MAX_WRIST_DEVIATION = 200;

  @Tunable("Dead Zone")
  public static double deadZone = 0.1;

  @Tunable("Auto Intake Spike Current")
  public static double IntakeSpikeCurrent = 5.0;

  @Tunable("Auto Intake Spike Length")
  public static double IntakeSpikeLength = 1.0;

  @Tunable("Auto Intake Speed Motor A")
  public static double IntakeSpeedA = 1;

  @Tunable("Auto Intake Speed Motor B")
  public static double IntakeSpeedB = 0.4;

  @Tunable("Eject Seconds")
  public static double EjectTime = 1.0;

  @Tunable("Eject Speed Motor A")
  public static double EjectSpeedA = -1;

  @Tunable("Eject Speed Motor B")
  public static double EjectSpeedB = -1;


  @Tunable("Elevator P")
  public static double elevatorP = 1;

  @Tunable("Elevator I")
  public static double elevatorI = 1;

  @Tunable("Elevator D")
  public static double elevatorD = 1;

  @Tunable("Elevator Tolerance")
  public static double elevatorTolerance;

  @Tunable("Elevator Up Setpoint")
  public static double elevatorUpLimit = 0;

  @Tunable("Elevator Down Setpoint")
  public static double elevatorDownLimit = 0;

  @Tunable("Elevator Bounce Back")
  public static double elevatorBounceBack = 10;

  @Tunable("Elevator Multiplier")
  public static double elevatorMult = 0.1;
  @Tunable("Elevator Ground Position")
  public static double elevatorGroundPosition = 0;
  @Tunable("Elevator Front Switch Position")
  public static double elevatorFrontSwitchPosition = 0;
  @Tunable("Elevator Back Switch Position")
  public static double elevatorBackSwitchPosition = 0;
  @Tunable("Elevator Scale Position")
  public static double elevatorScalePosition = 0;

  @Tunable("Wrist P")
  public static double wristP = 1;

  @Tunable("Wrist I")
  public static double wristI = 1;

  @Tunable("Wrist D")
  public static double wristD = 1;

  @Tunable("Wrist Stop Tolerance")
  public static double wristTolerance;

  @Tunable("Wrist Up Setpoint")
  public static double wristUpLimit = 0;

  @Tunable("Wrist Down Setpoint")
  public static double wristDownLimit = 0;

  @Tunable("Wrist Bounce Back")
  public static double wristBounceBack = 10;

  @Tunable("Wrist Multiplier")
  public static double wristMult = 0.1;

  @Tunable("Wrist Out Position")
  public static double wristOutPosition = 0;
  @Tunable("Wrist Back Position")
  public static double wristBackPosition = 0;
  @Tunable("Wrist Transit Position")
  public static double wristTransitPosition = 0;

  @Tunable("Winch In Speed")
  public static double winchInSpeed = 0.2;

  @Tunable("Winch Out Speed")
  public static double winchOutSpeed = -0.2; // 0.75

  @Tunable("Tape In Speed")
  public static double tapeInSpeed = 0.5;

  @Tunable("Tape Out Speed")
  public static double tapeOutSpeed = -0.25;

  @Tunable("Tape Measure Multiplier")
  public static double tapeMeasureMultiplier = 1;

  @Tunable("Winch Multiplier")
  public static double winchMultiplier = 1;

  @Tunable("Climber In Speed")
  public static double climberInSpeed = 0.5;


  @Tunable("Manual Elevator Up Speed")
  public static double manualElevatorUpSpeed = 1;

  @Tunable("Manual Elevator Down Speed")
  public static double manualElevatorDownSpeed = -0.5;


  @Tunable("Standard Deadzone")
  public static double standardDeadzone = 0.1;
}
