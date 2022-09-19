package unittests.tele.framework;

import unittests.tele.TeleUnitTest;
import util.condition.Decision;
import util.condition.DecisionList;

import static global.General.log;


public class DecisionTest extends TeleUnitTest {
    /**
     * Tests Decisions and DecisionLists
     */

    /**
     * Test Decision
     */
    private enum TestDecision implements Decision {
        OPTION1,
        OPTION2,
        OPTION3
    }

    /**
     * Current Option
     */
    private TestDecision option = TestDecision.OPTION1;

    /**
     * DecisionLists to test
     */
    private final DecisionList showColor = new DecisionList(() -> option)
            .addOption(TestDecision.OPTION1, () -> log.show("Option1: RED"))
            .addOption(TestDecision.OPTION2, () -> log.show("Option2: GREEN"))
            .addOption(TestDecision.OPTION3, () -> log.show("Option3: BLUE"));
    private final DecisionList changeColor = new DecisionList(() -> option)
            .addOption(TestDecision.OPTION1, () -> option = TestDecision.OPTION2)
            .addOption(TestDecision.OPTION2, () -> option = TestDecision.OPTION3)
            .addOption(TestDecision.OPTION3, () -> option = TestDecision.OPTION1);


    /**
     * Cycle through different decision options
     */
    @Override
    protected void loop() {
        log.show("Should cycle between RED, GREEN, and BLUE");
        if(timer.seconds() > 1){
            timer.reset();
            showColor.check();
            changeColor.check();
        }
    }
}
