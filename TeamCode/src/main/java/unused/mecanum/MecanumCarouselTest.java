package unused.mecanum;

import static global.General.gamepad1;

import unittests.tele.TeleUnitTest;

public class MecanumCarouselTest extends TeleUnitTest {
    @Override
    public void init() {
//        gph1.link(Button.A, MecanumAutoModules.SpinCarousel);
    }

    @Override
    protected void loop() {
        mecanumCarousel.move(-gamepad1.right_stick_y);
    }
}
