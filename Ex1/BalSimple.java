package Ex1;

import java.util.concurrent.Semaphore;

public class BalSimple {
    private String message;
    private  Semaphore sDepot, sRetrait;
    
    public BalSimple() {
        sDepot = new Semaphore(1, true);
        sRetrait = new Semaphore(0, true);
    }

    public void deposeRequete(String message) throws InterruptedException {
        sDepot.acquire();
        this.message = message;
        sRetrait.release();
    }

    public String retireRequete() throws InterruptedException {
        sRetrait.acquire();
        String message = this.message;
        sDepot.release();
        return message;
    }
}