package carControl

import java.awt.AWTException
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.event.KeyEvent

import business.dataBusiness
import models._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

object InputManager{
  val robot = new Robot
  val grass = 18
  val concrete = 10
  var inEditorMode = false
  var posInit = (-1000.0,-1000.0,-1000)
  val keys = Map( "-" -> KeyEvent.VK_SUBTRACT,"0" -> KeyEvent.VK_0, "1" -> KeyEvent.VK_1,"3" -> KeyEvent.VK_3, "2" -> KeyEvent.VK_2,"4" -> KeyEvent.VK_4, "5" -> KeyEvent.VK_5,
    "6" -> KeyEvent.VK_6, "7" -> KeyEvent.VK_7,"8" -> KeyEvent.VK_8, "9" -> KeyEvent.VK_9)

  var x = -1000
  var y = -1000
  var z = 0
  var cpt = 0

    def inputsFromNeuralNet(values : Seq[Double]): Unit ={
     if(values(0) > 0.5){
       robot.keyPress(KeyEvent.VK_UP)
     } else if(values(0) < 0.5){
       robot.keyRelease(KeyEvent.VK_UP)
     }
      if(values(1) > 0.5){
        robot.keyPress(KeyEvent.VK_DOWN)
      } else if(values(1) < 0.5){
        robot.keyRelease(KeyEvent.VK_DOWN)
      }
      if(values(2) > 0.5) {
        robot.keyPress(KeyEvent.VK_LEFT)
      } else if(values(2) < 0.5){
        robot.keyRelease(KeyEvent.VK_LEFT)
      }
      if(values(3) > 0.5){
        robot.keyPress(KeyEvent.VK_RIGHT)
      }else if(values(3) < 0.5){
        robot.keyRelease(KeyEvent.VK_RIGHT)
      }
      println(values)
   }

  def restart(): Unit ={
    robot.keyPress(73)
    robot.keyRelease(73)
  }
  def discoveryMode(data : DataInput, bool : Boolean): Unit ={

    //GET POS INIT
    if(posInit.equals((-1000,-1000,-1000))){
      posInit = (data.x,data.y,(Math.round(data.z) + 1).toInt)
      z = posInit._3
    }
    if(!inEditorMode){
      initEditorMode()
    }
    //Ready to set Position
    if(bool || z > 1000 || cpt == 60){
      x = x + 50
      cpt = 0
      //Reset z to initial
      z = data.z.toInt
      setPositionToGetGroundProperties(x,y,z)
    } else if( data.airspeed > 5 || cpt == 30) {
      println("zDown")
      z = z - 30
      cpt = cpt + 1
      setPositionToGetGroundProperties(x,y,z)
    } else if(data.airspeed > 2 && data.airspeed <= 5) {
      println("zUp")
      z = z + 2
      setPositionToGetGroundProperties(x,y,z)
      cpt = cpt +1
    }
    else { //Small movements
      Thread.sleep(500) //Wait until car stops
      cpt = cpt + 1
    }

    //Coordinates update to make a grid
    if(x > 1000) {
      z = posInit._3
      x = - 1000
      y = y +50
    }
    if (y > 1000){
      println("fin fin")
      Thread.sleep(500000)
    }
  }

  def doubleClick(): Unit ={
    robot.mousePress(InputEvent.BUTTON1_MASK)
    robot.mouseRelease(InputEvent.BUTTON1_MASK)
    robot.mousePress(InputEvent.BUTTON1_MASK)
    robot.mouseRelease(InputEvent.BUTTON1_MASK)
  }
  def initEditorMode(): Unit ={
    //GO to editor mode
    val screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    //println("Screen Widht: " + screenSize.getWidth());
    //println("Screen Height: " + screenSize.getHeight());

    //First Step to do before setting position
    //HandBrake On
    robot.keyPress(KeyEvent.VK_P)
      robot.keyRelease(KeyEvent.VK_P)
    //Go Editor Mode
    robot.keyPress(122)
      robot.keyRelease(122)
      robot.delay(5000)
    //Go search element in tree
      robot.mouseMove(screenSize.getWidth().toInt-40, 145)
      doubleClick()
      //Write Player and Enter
      val tab = Seq(KeyEvent.VK_T,KeyEvent.VK_H,KeyEvent.VK_E,KeyEvent.VK_P,KeyEvent.VK_L,KeyEvent.VK_A,KeyEvent.VK_Y,KeyEvent.VK_E,KeyEvent.VK_R,KeyEvent.VK_ENTER)
      tab.foreach(keycode=> {
        robot.keyPress(keycode)
        robot.keyRelease(keycode)
      })
      robot.mouseMove(screenSize.getWidth().toInt-125, 173)
      doubleClick()
      //GO POSITION
      robot.mouseMove(screenSize.getWidth().toInt-50, 520)
      cleanInputPosition()
      inEditorMode = !inEditorMode
  }
  def setPositionToGetGroundProperties(x : Double, y : Double, z: Double): Unit ={
      println(x + " "+ y + " "  +z)
      val xCoord = x.toInt.toString
      val yCoord = y.toInt.toString
      val zCoord = z.toInt.toString

    //ESSAI
    val screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    robot.mouseMove(screenSize.getWidth().toInt-250, 520)
    doubleClick()
    restart()
    robot.mouseMove(screenSize.getWidth().toInt-125, 173)
    doubleClick()
    robot.mouseMove(screenSize.getWidth().toInt-50, 520)
    cleanInputPosition()

    ///
      //For azerty KeyBoard
      robot.keyPress(KeyEvent.VK_SHIFT)
      //
      xCoord.foreach(key => {
        robot.keyPress(keys(key.toString))
        robot.keyRelease(keys(key.toString))
      })
      robot.keyPress(KeyEvent.VK_SPACE)
      robot.keyRelease(KeyEvent.VK_SPACE)
      yCoord.foreach(key => {
        robot.keyPress(keys(key.toString))
        robot.keyRelease(keys(key.toString))
      })

      robot.keyPress(KeyEvent.VK_SPACE)
      robot.keyRelease(KeyEvent.VK_SPACE)

      zCoord.foreach(key => {
        robot.keyPress(keys(key.toString))
        robot.keyRelease(keys(key.toString))
      })
      //For azerty KeyBoard
      robot.keyRelease(KeyEvent.VK_SHIFT)
      //Press Enter
      robot.keyPress(KeyEvent.VK_ENTER)
      robot.keyRelease(KeyEvent.VK_ENTER)

      //Clean input position
      robot.mouseMove(screenSize.getWidth().toInt-50, 520)
      doubleClick()
      cleanInputPosition()
      println("fin")
  }
  def cleanInputPosition(): Unit ={
    doubleClick()
    //Clean Init Position
    robot.keyPress(KeyEvent.VK_END)
    (1 to 100).foreach(e => {robot.keyPress(KeyEvent.VK_BACK_SPACE);robot.keyRelease(KeyEvent.VK_BACK_SPACE);})
  }
}