package gdil.commande.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CommandeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Commande getCommandeSample1() {
        return new Commande().id(1L).clientId(1L).produitId(1L);
    }

    public static Commande getCommandeSample2() {
        return new Commande().id(2L).clientId(2L).produitId(2L);
    }

    public static Commande getCommandeRandomSampleGenerator() {
        return new Commande().id(longCount.incrementAndGet()).clientId(longCount.incrementAndGet()).produitId(longCount.incrementAndGet());
    }
}
