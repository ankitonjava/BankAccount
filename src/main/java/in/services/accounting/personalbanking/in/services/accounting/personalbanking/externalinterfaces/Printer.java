
package in.services.accounting.personalbanking.in.services.accounting.personalbanking.externalinterfaces;

import java.util.concurrent.atomic.AtomicLong;

public interface Printer
{
    /**
     * Counter for verification purpose. Counter will return number of times print function is called.
     *
     * @return
     */
    AtomicLong getCounter();

    /**
     * Print message
     *
     * @param message
     */
    void print(String message);
}
