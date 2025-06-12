package robotparts.hardware;

import automodules.stage.Stage;
import robotparts.RobotPart;
import robotparts.electronics.ElectronicType;
import robotparts.electronics.continuous.CMotor;

public class Mahiya extends RobotPart {
    public CMotor mahiyamotor1;
    public CMotor mahiyamotor2;
    public CMotor mahiyamotor3;
    public CMotor mahiyamotor4;
    @Override
    public void init() {
        mahiyamotor1 = create("m1", ElectronicType.CMOTOR_REVERSE_FLOAT);
        mahiyamotor2 = create("m2", ElectronicType.CMOTOR_REVERSE_FLOAT);
        mahiyamotor3 = create("m3", ElectronicType.CMOTOR_REVERSE_FLOAT);
        mahiyamotor4 = create("m4", ElectronicType.CMOTOR_REVERSE_FLOAT);
    }

    @Override
    public void move(double intakePower) {
        mahiyamotor1.setPower(intakePower);
        mahiyamotor4.setPower(intakePower);
    }

    public Stage moveTime(double p, double t) { return super.moveTime(p, t); }
}




