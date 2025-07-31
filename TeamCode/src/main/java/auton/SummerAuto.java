package auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import autoutil.AutoFramework;

@Autonomous(name = "SummerAuto", group = "auto")
public class SummerAuto extends AutoFramework {

    @Override
    public void initialize() {
                wait(0.5);
    }

    @Override
    public void define() {
            whileTime(() -> {
                drive.move(0.5, 0, 0.5);
            }, 3);
//        whileTime(() -> {
//            drive.move(0, 0, -0.5);
//        }, 0.7);
//        whileTime(() -> {
//            drive.move(0, 0, 0);
//        }, 0);
//        whileTime(() -> {
//            drive.move(0.5, 0, 0);
//        }, 0.7);
//        whileTime(() -> {
//            drive.move(0.5, 0, -0.5);
//        }, 4);
//        whileTime(() -> {
//            drive.move(0.5, 0, 0);
//        }, 0.7);
//        whileTime(() -> {
//            drive.move(0.4, 0, -0.7);
//        }, 3);
//        whileTime(() -> {
//            drive.move(0.7, 0, 0);
//        }, 3);


        };}