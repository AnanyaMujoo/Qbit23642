//package robotparts.hardware;
//
//import robotparts.RobotPart;
//import robotparts.electronics.ElectronicType;
//import robotparts.electronics.positional.PMotor;
//import robotparts.electronics.positional.PServo;
//
//public class Drone extends RobotPart {
//    public PServo drone;
//
//    @Override
//    public void init() {
//        drone = create("drone", ElectronicType.PSERVO_REVERSE);
//        drone.setPosition("start", 0.54);
//        drone.setPosition("release", 0.65);
//        moveDroneStart();
//    }
//    public void moveDroneStart(){ drone.moveToPosition("start"); }
//    public void moveDroneRelease(){ drone.moveToPosition("release"); }
//
//}