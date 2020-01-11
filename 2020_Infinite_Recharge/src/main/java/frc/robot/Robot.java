/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.interfaces.Gyro;
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

  //Drive Train----------------------------------------------------------------------------------------------------------------------------
  
  private WPI_TalonSRX _frontRightMotor = new WPI_TalonSRX(1);
  private WPI_TalonSRX _frontLeftMotor = new WPI_TalonSRX(3);

  private VictorSPX _backRightMotor = new VictorSPX(2);
  private VictorSPX _backLeftMotor = new VictorSPX(4);

  DifferentialDrive driveFront = new DifferentialDrive(_frontRightMotor, _frontLeftMotor);

 //Controls-----------------------------------------------------------------------------------------------------------------------------
  
 private Joystick _joystick = new Joystick(0); 

  //up climb motor------------------------------------------------------------------------------------------------------------------
 
  private VictorSPX _upClimbMotor = new VictorSPX(5);
  
  //down climb motor---------------------------------------------------------------------------------------------------------------
  
  private VictorSPX _downbClimbMotor = new VictorSPX(6);

  //Buttons/Integers----------------------------------------------------------------------------------------------------------------

  private Integer _manLiftUp = 11;
  private Integer _manLiftDown = 12;
  
  //Gyro--------------------------------------------------------

  private Gyro _gyro;

  //Digital Switches--------------------------------------------------------------------------------------------------------------------

  private DigitalInput _bottomSwitch = new DigitalInput(1);
  private DigitalInput _topSwitch = new DigitalInput(2);

  //Pneumatics
  private Compressor _compressor = new Compressor(0);
  private DoubleSolenoid _tansSolnoid = new DoubleSolenoid(0, 1);

  //Drive Joystick Button Values
  private Integer _transSolnoidVal = 1;

  //Toggles
    //Pneumatics
    private Toggle _transSolnoidTog = new Toggle();



  //Toggle--------------------------------------------------------------------------------------------------------------------------------------

  private Toggle _upClimbTog = new Toggle();
  private Toggle _downClimbTog = new Toggle();



  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
    
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
   
    //gyro------------------------------------------------------------------------

    _gyro.getAngle();
    
    //Slaves-------------------------------------------------------------------------------------------------------------

    _backLeftMotor.follow(_frontLeftMotor); 
    _backRightMotor.follow(_frontRightMotor);

    //Pneumatics
    _compressor.enabled
    _transSolnoid.set(Value.kReverse);

    

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
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    //Drive_Train
    driveFront.arcadeDrive(_joystick.getY(), _joystick.getX());

  if(_upClimbTog.toggleHeld(_joystick, _manLiftUp) && _topSwitch.get()){

    _upClimbMotor.set(ControlMode.PercentOutput, 0.5);

  }else{
    _upClimbMotor.set(ControlMode.PercentOutput, 0);
  }
  if(_downbClimbMotor.toggleHeld(_joystick, _manLiftDown) && _bottomSwitch.get()){

    _downbClimbMotor.set(ControlMode.PercentOutput, -0.5);

  }else{
    _downbClimbMotor.set(ControlMode.PercentOutput, 0);
  }

    // Pneumatics Controls
    if(_transSolnoidTog.togglePressed(_joystick, _transSolnoidVal)){
      _transSolnoid.set(DoubleSolenoid.Value.kForward);
  
    }else{
      _transSolnoid.set(DoubleSolenoid.Value.kReverse);
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
