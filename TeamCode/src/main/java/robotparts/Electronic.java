package robotparts;

import org.checkerframework.checker.units.qual.A;

import util.Access;
import util.condition.Status;

public class Electronic {
    /**
     * Naming conventions for electronics
     * C -> continuous
     * P -> positional
     * O -> output
     * I -> input
     */


    /**
     * Status represents the current status of the electronic part
     * NOTE: This is volatile to prevent thread conflicts
     */
    protected volatile Status status = Status.IDLE;
    /**
     * Access represents the access the user has to use the electronic
     */
    protected Access access = new Access();

    /**
     * Gets the current status
     * @return status
     */
    public synchronized Status getStatus(){
        return status;
    }

    /**
     * Sets the current status
     * @param status
     */
    public synchronized void setStatus(Status status){
        this.status = status;
    }

    /**
     * Does the electronic have access?
     * @return isAllowed
     */
    public synchronized boolean isAllowed(){
        return access.isAllowed();
    }

    /**
     * Halt the electronic (Used to stop motors)
     */
    public void halt(){}
}
