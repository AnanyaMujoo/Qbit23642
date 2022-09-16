package elements;

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

    }
}
