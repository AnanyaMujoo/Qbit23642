package unittests.tele.framework;

import java.util.ArrayList;
import java.util.Arrays;

import elements.FieldSide;
import unittests.tele.TeleUnitTest;

import static global.General.fieldSide;
import static global.General.log;
import static global.General.storage;

public class StorageTest extends TeleUnitTest {
    /**
     * Tests storage by adding a set of items
     */

    @Override
    protected void start() {
        /**
         * Items to be stored
         */
        fieldSide = FieldSide.RED;
        storage.addItem("St", "Test");
        storage.addItem("In", 1);
        storage.addItem("Fl", 1.0f);
        storage.addItem("Do", 2.0d);
        storage.addItem("Bo", true);
        storage.addItem("FieldSide", fieldSide);
        ArrayList<Double> input = new ArrayList<>();
        input.add(1.0);
        input.add(2.0);
        input.add(3.0);
        ArrayList<Double> output = new ArrayList<>();
        output.add(2.0);
        output.add(4.0);
        output.add(6.0);
        storage.addData("Data", input, output);

        storage.saveItems();
    }

    @Override
    protected void loop() {
        /**
         * Show the values of the items to be stored
         * Should match with the values in parenthesis
         */
        log.showAndRecord("Number of items (8)", storage.numItems());
        log.showAndRecord("St (Test)", storage.getItem("St").getValue());
        log.showAndRecord("In (1)", storage.getItem("In").getValue());
        log.showAndRecord("Fl (1.0)", storage.getItem("Fl").getValue());
        log.showAndRecord("Do (2.0)", storage.getItem("Do").getValue());
        log.showAndRecord("Bo (true)", storage.getItem("Bo").getValue());
        log.showAndRecord("FieldSide (Right)", FieldSide.create((String) storage.getItem("FieldSide").getValue()));
        log.showAndRecord("FieldColor (Red)", fieldSide.name());
        log.showAndRecord("DataInput {1.0,2.0,3.0}", storage.getData("Data").getInput().getValue());
        log.showAndRecord("DataOutput {2.0,4.0,6.0}", storage.getData("Data").getOutput().getValue());
    }

    @Override
    public void stop() {
        /**
         * Empty the storage list
         */
        storage.emptyItems();
    }
}
