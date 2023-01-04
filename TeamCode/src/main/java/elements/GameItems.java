package elements;

import global.Constants;
import util.condition.Decision;

public class GameItems {

    public static class Ball {
        /**
         * Radius and diameter of the balls
         */
        public static final double radius = 3.5; //cm
        public static final double diameter = 2*radius;
        /**
         * Weight of a ball
         */
        public static final double weight = 33.5; //g
    }

    public static class Box {
        /**
         * Width of the cube (or height since cube)
         */
        public static final double width = 5.1; //cm
        /**
         * Weights of different cubes
         */
        public static final double lightWeight = 50.5; //g
        public static final double mediumWeight = 93.1; //g
        public static final double heavyWeight = 135.4; //g
    }

    public static class Cone {
        public static final double height = 5.0 * Constants.INCH_TO_CM;
        public static final double baseWidth = 4.0 * Constants.INCH_TO_CM;
    }

    public static class Junction {
        public static final double highHeight = 33.5 * Constants.INCH_TO_CM; // 85 cm
        public static final double middleHeight = 23.5 * Constants.INCH_TO_CM; // 60 cm
        public static final double lowHeight = 13.5 * Constants.INCH_TO_CM; // 34 cm

    }
}
