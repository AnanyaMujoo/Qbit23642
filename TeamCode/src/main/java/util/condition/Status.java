package util.condition;

public enum Status {
    /**
     * What is the status?
     */


    /**
     * Waiting for commands or is ready to be used?
     */
    IDLE,

    /**
     * Being used or is about to be used?
     */
    ACTIVE,

    /**
     * Not allowed to be used anymore?
     */
    DISABLED
}
