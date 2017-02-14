package io.github.satwantrana.primalityTesting;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * Created by satwant on 14/2/17.
 */
public class PolynomialSpec {
    @Test
    public void InitTest() {
        BigInteger n = BigInteger.valueOf(5);
        int r = 7;
        Polynomial p = new Polynomial(n, r);
        p.setCoefficient(0,BigInteger.valueOf(-1));
        p.setCoefficient(1, BigInteger.valueOf(6));

        assertEquals(BigInteger.valueOf(4), p.getCoefficient(0));
        assertEquals(BigInteger.valueOf(1), p.getCoefficient(1));
        for (int i=2;i<r;i++) {
            assertEquals(BigInteger.ZERO, p.getCoefficient(i));
        }
    }

    @Test
    public void MulTest() {
        BigInteger n = BigInteger.valueOf(5);
        int r = 3;
        Polynomial p = new Polynomial(n, r);
        p.setCoefficient(0, BigInteger.ONE);
        p.setCoefficient(2, BigInteger.ONE);

        p = p.mul(p);

        assertEquals(BigInteger.ONE, p.getCoefficient(0));
        assertEquals(BigInteger.ONE, p.getCoefficient(1));
        assertEquals(BigInteger.valueOf(2), p.getCoefficient(2));
    }

    @Test
    public void ExpTest() {
        BigInteger n = BigInteger.valueOf(5);
        int r = 1000;
        for (int a=0; a<n.intValue(); a++) {
            Polynomial p = new Polynomial(n, r);
            p.setCoefficient(0, BigInteger.valueOf(a));
            p.setCoefficient(1, BigInteger.ONE);

            p = p.exp(n);

            for (int i = 0; i < r; i++) {
                BigInteger coeff = p.getCoefficient(i);
                if (i == 0) assertEquals(BigInteger.valueOf(a), coeff);
                else if(i == n.intValue()) assertEquals(BigInteger.ONE, coeff);
                else assertEquals(BigInteger.ZERO, coeff);
            }
        }
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(PolynomialSpec.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}
