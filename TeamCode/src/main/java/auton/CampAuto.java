package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;
import robotparts.hardware.CampDrive;

@Autonomous(name = "QbitAutoSamp", group = "auto")
public class CampAuto extends Auto{


    @Override
    public void initAuto() {
        moveTime(0.4,0.4,0.1,2);


    }

    @Override
    public void runAuto() {

    }

    
    public void moveTime (double f, double s, double r, double t){
        whileTime(() -> {CampDrive.move(f,s,r);}, t);

    }
}
