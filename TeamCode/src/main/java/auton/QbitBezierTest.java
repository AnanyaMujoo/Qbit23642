package auton;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import geometry.framework.Point;


@Autonomous(name = "QbitBezierTest", group = "Autonomous")
public class QbitBezierTest extends AutoFramework {


    @Override
    public void initialize() {
        setConfig(mecanumNonstopConfig);


    }

    @Override
    public void define() {
       // to create cubic bezier curves https://www.desmos.com/calculator/ebdtbxgbq0
      addBezierWaypoints(0.4,1,new Point(0,0),new Point(5,10), new Point(50,10), new Point(50,50),0,10);
      addBezierSegments(0.4,1,new Point(50,50),new Point(55,60), new Point(100,60), new Point(100,100),50,60);
    }


    @Override
    public void postProcess() {
    }
}
