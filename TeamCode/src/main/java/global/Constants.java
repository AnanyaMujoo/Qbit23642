package global;

public class Constants {

    private Constants(){}
    /**
     * Refresh rate for threads, tested at 100 and 60 hz
     */
    public static final int THREAD_REFRESH_RATE = 60; //hz
    /**
     *  Minimum  refresh rate allowed before robot is considered to be lagging
     */
    public static final double MINIMUM_REFRESH_RATE = 30; //hz

    /**
     * Accuracies for creating points in arc and line generations in Autonomous
     */
    public static final double ANG_ACC_ARC = 0.05;
    public static final double LINE_ACC_PATH = 0.05;

    public static final double ORBITAL_TICK_PER_REV = 537.6;

    /**
     * Constant for converting lift height to ticks for the motor to rotate
     */
    public static final double LIFT_CM_TO_TICKS = ((ORBITAL_TICK_PER_REV)/(Math.PI*3))/3; //in ticks/cm , (ticks/rev)/(cm/rev), accounting for gear ratio
    /**
     * Constant for converting the angle of the turret in degrees to ticks for the motor to rotate
     */
    public static final double TURRET_ANGLE_DEG_TO_TICKS = (ORBITAL_TICK_PER_REV/360.0)*4; //(8192.0/360.0)*4.0; //ticks/deg, (ticks/deg)*ratio
    /**
     * Rest power for the lift to prevent gravity from bringing it down
     */
    public static final double LIFT_REST_POW = 0.07;
    /**
     * Turret Angles for the robot to move (note that 0 is facing the front of the robot)
     */
    public static final double BLUE_TURRET_TARGET_TELE_1 = 135; // 120
    public static final double BLUE_TURRET_TARGET_TELE_2 = 135;
    public static final double RED_TURRET_TARGET_TELE_1 = -BLUE_TURRET_TARGET_TELE_1;
    public static final double RED_TURRET_TARGET_TELE_2 = -BLUE_TURRET_TARGET_TELE_2;
    public static final double BLUE_TURRET_TARGET_AUTO = 85;
    public static final double RED_TURRET_TARGET_AUTO = -BLUE_TURRET_TARGET_AUTO;
}
