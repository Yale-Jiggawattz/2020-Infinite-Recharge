/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.*;

/**
 * Add your docs here.
 */
public class Toggle {
    public boolean toggleOn;
    public boolean togglePressed;
    public boolean toggleHeld; 

    public Toggle()
    {
        toggleOn = false;
        togglePressed = false;
    }

    public boolean togglePressed(Joystick _stick, int button)    //for a toggle button
    {
        if(_stick.getRawButtonPressed(button)){
            if(!togglePressed){
                toggleOn = !toggleOn; 
                togglePressed = true;

            }
        }else{
            togglePressed = false; 
        }
        return toggleOn;
    } 

public boolean toggleHeld(Joystick _stick, int button)      //for a button being held
{
    if(_stick.getRawButton(button)){
        if(!togglePressed){
            toggleOn = !toggleOn;
            togglePressed = true;
        }
    }else{
        togglePressed = false;
    }
    return togglePressed;
}
}
