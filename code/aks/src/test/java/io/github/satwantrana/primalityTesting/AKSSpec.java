package io.github.satwantrana.primalityTesting;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by satwant on 14/2/17.
 */
public class AKSSpec {
    @Test
    public void SimplePrimalityTest() {
        AKS aks = new AKS();
        Integer[] primes = {2, 3, 5, 7, 11, 13};
        Integer[] composites = {4, 6, 8, 9, 10, 12, 14, 15};

        for (int prime: primes) {
            System.out.println("Testing prime: " + prime);
            assertTrue(aks.primalityTest(BigInteger.valueOf(prime)));
        }
        for (int composite: composites) {
            System.out.println("Testing composite: " + composite);
            assertFalse(aks.primalityTest(BigInteger.valueOf(composite)));
        }
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(AKSSpec.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }

}
