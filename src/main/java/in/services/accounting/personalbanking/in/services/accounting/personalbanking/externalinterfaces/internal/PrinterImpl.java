package in.services.accounting.personalbanking.in.services.accounting.personalbanking.externalinterfaces.internal;

import java.util.concurrent.atomic.AtomicLong;

import in.services.accounting.personalbanking.in.services.accounting.personalbanking.externalinterfaces.Printer;

/**
 * Dummy printer implementation class (stub) for verification of statement printing.
 */
public class PrinterImpl implements Printer
{
    private AtomicLong counter = new AtomicLong(0);

    public AtomicLong getCounter()
    {
        return counter;
    }

    /**
     * Print statement in console and increment counter (counter are used in junit for verification purpose).
     *
     * @param message
     */
    public void print(String message)
    {
        System.out.println(message);
        counter.incrementAndGet();
    }
}
