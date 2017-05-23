package io.github.satwantrana.primalityTesting;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.org.apache.xpath.internal.operations.Number;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.omg.PortableInterceptor.INACTIVE;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Timer;

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

    @Test
    public void SieveTest() {
        AKS aks = new AKS();
        int n = 100000;
        Boolean[] sieve = NumberTheory.sieve(n);
        for (int i=1;i<=n;i++) {
            long start = System.currentTimeMillis();
            assertEquals(sieve[i], aks.primalityTest(BigInteger.valueOf(i)));
            long end = System.currentTimeMillis();
            long dur = end - start;
            if (dur > 0) System.out.println("T: " + i + " " + dur);
        }
    }

    @Test
    public void LValueTest() {
        AKS aks = new AKS();
        int lower = (int)1, upper = (int)1e7;
        Boolean[] sieve  = NumberTheory.sieve(upper);
        for (int i=lower; i<=upper; i++) if (!sieve[i]) {
            BigInteger n = BigInteger.valueOf(i);
            int lgn = aks.calculateLogN(n);
            int r = aks.calculateR(n,lgn);
            Map<Integer, Integer> factors = NumberTheory.factorize(i);
            boolean toProcess = true;
            if (factors.size() == 1) toProcess = false;
            else for (int p: factors.keySet()) if (p <= r) {
                toProcess = false; break;
            }
            if (toProcess) assertEquals(false, aks.primalityTest(n));
        }
    }

    public boolean checkPolyEqn(BigInteger n, int r, int a) {
        Polynomial left = new Polynomial(n, r);
        left.setCoefficient(0, BigInteger.valueOf(a));
        left.setCoefficient(1, BigInteger.valueOf(1));
        left = left.exp(n);
        Polynomial right = new Polynomial(n, r);
        right.setCoefficient(0, BigInteger.valueOf(a));
        right.setCoefficient(n, BigInteger.valueOf(1));
        return left.equals(right);
    }

    @Test
    public void RValueTest() {
        AKS aks = new AKS();
        int upper = 1500;
        int lower = aks.calculateLogN(BigInteger.valueOf(upper).pow(3));
        lower = lower*lower + 1;
        Boolean[] sieve = NumberTheory.sieve(upper);
        System.out.println(lower);
        for (int i=lower;i<=upper;i++) if (sieve[i]) {
            for (int j=i+1;j<=upper;j++) if (sieve[j]) {
                for (int k=j+1;k<=upper;k++) if (sieve[k]) {
                    BigInteger n = BigInteger.valueOf((long) i * j * k);

                    int lgn = aks.calculateLogN(n);
                    int r = aks.calculateR(n,lgn);
                    System.out.println(n + " " + i + " " + j + " " + k + " " + lgn + " " + r);

                    Map<Integer, Integer> factors = NumberTheory.factorize(i*j*k);
                    boolean toProcess = true;
                    if (factors.size() == 1) toProcess = false;
                    else for (int p: factors.keySet()) if (p <= r) {
                        System.out.println("F: " + n + " " + p);
                        toProcess = false; break;
                    }
                    if (toProcess) assertEquals(false, aks.primalityTest(n));
                }
            }
        }
    }

    @Test
    public void IntrospectivityTest() {
        AKS aks = new AKS();
        int lower = (int)5, upper = (int)100;
        Boolean[] sieve  = NumberTheory.sieve(upper);
        for (int i=lower; i<=upper; i++) if (sieve[i]) {
            BigInteger n = BigInteger.valueOf(i);
            int lgn = aks.calculateLogN(n);
            int r = aks.calculateR(n,lgn);
            if (n.gcd(BigInteger.valueOf(r)).compareTo(BigInteger.ONE) > 0) continue;
            int phiR = NumberTheory.phi(r);
            int l = (int) (Math.sqrt(phiR)*lgn);
            BigInteger pwr = n.add(n.pow(phiR)).subtract(BigInteger.ONE);
            System.out.println(n + " " + r);
            for (int a=1; a<=l; a++) {
                Polynomial left = new Polynomial(n,r);
                left.setCoefficient(0,BigInteger.valueOf(a));
                left.setCoefficient(1,BigInteger.valueOf(1));
                left = left.exp(pwr);

                Polynomial right = new Polynomial(n,r);
                right.setCoefficient(0,BigInteger.valueOf(a));
                right.setCoefficient(pwr,BigInteger.valueOf(1));

                if (!left.equals(right)) {
                    assertTrue(false);
                }
            }
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
