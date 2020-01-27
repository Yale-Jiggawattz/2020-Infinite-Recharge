/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer; 
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>(); 

//Might add Auton time values - Nate
//Might change down climg to pull up and up climb to reach -Nate

//Button Values----------------------------------------------------------------------------------------------------------------

  private Integer _transSolenoidInt = 2;

  private Integer _upClimbInt = 11;
  private Integer _downClimbInt = 12;

  private Integer _intakeInt = 3;
  private Integer _launchInt = 1;
  private Integer _reverseInt = 5;

//Encoder Values--------------------------------------------------------------------------------------------------------------------------

  private Integer _centerAutoSTG1 = 500;
  
  private Integer _leftAutoSTG1 = 500;
  private Integer _leftAutoSTG2 = 1000;

  private Integer _rightAutoSTG1 = 500;
  private Integer _rightAutoSTG2 = 1000;

//Motor Speeds

  private Double _upClimbMotorSTG1 = 0.5;
  private Double _upClimbMotorSTG2 = -0.5; 
  private Double _downClimbMotorSTG1 = -0.5; 

  private Double _launcherSpeed = 1.0;
  private Double _intakeSpeed = 0.5; 
  private Double _intakeRevSpeed = -0.5;
  private Double _beltSpeed = 0.5;
  private Double _beltRevSpeed = -0.5;

//Servo position

  private Double _servoLaunchPos = 0.5; 

//Toggle--------------------------------------------------------------------------------------------------------------------------------------
  
  private Toggle _transSolenoidTog = new Toggle();
  
  private Toggle _upClimbTog = new Toggle();
  private Toggle _downClimbTog = new Toggle();

  private Toggle _intakeTog = new Toggle();
  private Toggle _launchTog = new Toggle();
  private Toggle _reverseTog = new Toggle();

//Drive Train----------------------------------------------------------------------------------------------------------------------------
  
  private WPI_TalonSRX _frontRightMotor = new WPI_TalonSRX(1);
  private WPI_TalonSRX _frontLeftMotor = new WPI_TalonSRX(3);

  private VictorSPX _backRightMotor = new VictorSPX(4);
  private VictorSPX _backLeftMotor = new VictorSPX(2);

  private DifferentialDrive _drive = new DifferentialDrive(_frontRightMotor, _frontLeftMotor);

//Controls-----------------------------------------------------------------------------------------------------------------------------
  
  private Joystick _joystick = new Joystick(0); 

//Transmission

  private DoubleSolenoid _transSolenoid = new DoubleSolenoid(0, 1);

//Launcher-------------------------------------------------------------------------------------------------------------------------------

  private WPI_VictorSPX _intakeMotor = new WPI_VictorSPX(8);

  private WPI_VictorSPX _beltMotor = new WPI_VictorSPX(9);

  private WPI_VictorSPX _leftLaunchMotor = new WPI_VictorSPX(6);
  private WPI_VictorSPX _rightLaunchMotor = new WPI_VictorSPX(5);
  
  private Servo _launcherServo = new Servo(1); 
  
//Climb------------------------------------------------------------------------------------------------------------------
 
  private VictorSPX _upClimbMotor = new VictorSPX(7); 
  private VictorSPX _downClimbMotor = new VictorSPX(10); 

  private DigitalInput _bottomSwitch = new DigitalInput(1);
  private DigitalInput _topSwitch = new DigitalInput(2);
  
//Auton--------------------------------------------------------------------------------------------------------------------

  AHRS _gyro;
  double kP = 1; 
  double heading; 

  private Timer _autonTimer = new Timer();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
    
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    
//Slaves-------------------------------------------------------------------------------------------------------------

    _backLeftMotor.follow(_frontLeftMotor); 
    _backRightMotor.follow(_frontRightMotor);

//Pneumatics--------------------------------------------------------------------------------------------------------------------------------

    _transSolenoid.set(Value.kReverse);

  //Launch Servo
    
    _launcherServo.set(0);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    _autonTimer.start();
    _autonTimer.reset();

    _gyro.getAngle();
    
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:

      if (_autonTimer.get() < 15.0){

        _drive.arcadeDrive(.5, 0);

      
      }else{

        _drive.arcadeDrive(0, 0);
        
      }
        break;
        
      case kDefaultAuto:
      default:
      
      if (_autonTimer.get() < 15.0){

        _drive.tankDrive(.5 + kP * heading - _gyro.getAngle(), .5 - kP * heading - _gyro.getAngle());
      
      }else{

        _drive.arcadeDrive(0, 0);
        
      }
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {{

  //Drive_Train-----------------------------------------------------------------------------------------------------------------------

    _drive.arcadeDrive(_joystick.getY(), _joystick.getX());

  //Climber----------------------------------------------------------------------------------------------

    if(_upClimbTog.toggleHeld(_joystick, _upClimbInt) && _topSwitch.get()){

    _upClimbMotor.set(ControlMode.PercentOutput, _upClimbMotorSTG1);
    
  }else if(_downClimbTog.toggleHeld(_joystick, _downClimbInt) && _bottomSwitch.get()){

    _downClimbMotor.set(ControlMode.PercentOutput, _downClimbMotorSTG1);
    _upClimbMotor.set(ControlMode.PercentOutput, _upClimbMotorSTG2);

  }else{

    _downClimbMotor.set(ControlMode.PercentOutput, 0);
  
  }

  //Transmission--------------------------------------------------------------------------------------------------
    
    if(_transSolenoidTog.togglePressed(_joystick, _transSolenoidInt)){
      _transSolenoid.set(DoubleSolenoid.Value.kForward);
  
  }else{

      _transSolenoid.set(DoubleSolenoid.Value.kReverse);
    
    }
  }
    
  //Launcher
    
    if(_intakeTog.toggleHeld(_joystick, _intakeInt)){
      
      _intakeMotor.set(_intakeSpeed);
      _beltMotor.set(_beltSpeed);
    }
    else if(_launchTog.toggleHeld(_joystick, _launchInt)){
      
      _leftLaunchMotor.set(_launcherSpeed);
      _rightLaunchMotor.set(_launcherSpeed);
      _beltMotor.set(_beltSpeed);
      _launcherServo.set(_servoLaunchPos);
    }
    else if(_reverseTog.toggleHeld(_joystick, _reverseInt)){
      
      _beltMotor.set(_beltRevSpeed);
      _intakeMotor.set(_intakeRevSpeed);
    }
    else{
      
      _leftLaunchMotor.set(0);
      _rightLaunchMotor.set(0);
      _beltMotor.set(0);
      _intakeMotor.set(0);
      _launcherServo.set(0);
    }
  }
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}