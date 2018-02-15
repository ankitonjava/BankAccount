package in.services.accounting.personalbanking.in.services.accounting.personalbanking.externalinterfaces.internal;

import java.util.concurrent.atomic.AtomicLong;

import in.services.accounting.personalbanking.in.services.accounting.personalbanking.externalinterfaces.Printer;

public class PrinterImpl implements Printer
{
    private AtomicLong counter = new AtomicLong(0);

    public AtomicLong getCounter()
    {
        return counter;
    }

    public void print(String message)
    {
        System.out.println(message);
        counter.incrementAndGet();
    }
}
