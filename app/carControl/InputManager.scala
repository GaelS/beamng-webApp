package carControl

import java.awt.AWTException
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

object InputManager{
  val robot = new Robot
  val grass = 18
  val concrete = 10


  def discoveryMode(fr : Int, fl : Int, rr : Int, rl : Int, speed : Double): Unit = {
    robot.keyPress(38)
  //If left side in grass, turn right
  if( fr == grass || rr == grass ){
    robot.keyPress(37)
  } else if( fl == grass || rl == grass ){
    robot.keyPress(39)
  } else if((fr == - 1 && rr == -1 && fl == -1 && rl == -1 && speed  < 0.5)){
    robot.keyPress(73)
    robot.keyRelease(73)
    robot.keyPress(38)

  } else {
    robot.keyRelease(37)
    robot.keyRelease(39);
  }
  //If right side in grass, turn left
  // else go straight

  }

}