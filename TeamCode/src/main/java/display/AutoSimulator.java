package display;

import auton.TerraAuto;
import autoutil.AutoFramework;
import geometry.position.Pose;

public class AutoSimulator extends Drawer{

    // TODO MAKE SIMULATION


    public static void main(String[] args) {
        drawWindow(new AutoSimulator(), "Auto Simulator");
    }

    @Override
    public void define() {
       setAuto(new TerraAuto(), new Pose(20,fieldSize/2.0,0));
       drawAuto();
    }




    private AutoFramework auto;
    private Pose startPose;

    private void setAuto(AutoFramework auto, Pose startPose) {
        this.auto = auto;
        this.startPose = startPose;
    }

    private void drawAuto(){
        auto.setup();
        if(auto.isFlipped()){ startPose.scaleX(-1); startPose.translate(fieldSize, 0); startPose.rotate(180);}
        drawField();
        drawOnField(auto.getAutoPlane(), startPose);
    }




}
