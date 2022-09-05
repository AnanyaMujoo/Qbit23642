package unittests.tele.hardware.Tank;

import unittests.tele.TeleUnitTest;
import static global.General.*;

public class TankCarouselTest extends TeleUnitTest {
    /**
     * Tests carousel
     */
    @Override
    public void loop() {
        showConfig(bot.tankCarousel);
        /**
         * Carousel should move
         */
        log.show("Use right trigger");
        bot.tankCarousel.move(gamepad1.right_trigger);
    }
}
