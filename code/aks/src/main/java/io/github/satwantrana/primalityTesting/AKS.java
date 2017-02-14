package io.github.satwantrana.primalityTesting;

import java.math.BigInteger;

/**
 * Created by satwant on 12/2/17.
 */
public class AKS {
    public boolean perfectPowerTest(BigInteger n, int lgn) {
        for (int b=2;b<=lgn;b++) {
            BigInteger l = BigInteger.valueOf(2), r = n;
            while (l.compareTo(r) < 0) {
                BigInteger m = l.add(r).divide(BigInteger.valueOf(2));
                int res = m.pow(b).compareTo(n);
                if (res == 0) return true;
                else if (res < 0) l = m.add(BigInteger.ONE);
                else r = m.subtract(BigInteger.ONE);
            }
        }
        return false;
    }
    public int calculateR(BigInteger n, int lgn) {
        for (int r=2;;r++) {
            BigInteger cur = BigInteger.ONE;
            boolean yes = true;
            for (int j=1;j<=lgn*lgn;j++) {
                cur = cur.multiply(n);
                cur = cur.mod(BigInteger.valueOf(r));
                if (cur.compareTo(BigInteger.ONE) == 0) {
                    yes = false; break;
                }
            }
            if (yes) return r;
        }
    }
    public int calculateLogN(BigInteger n) {
        int ret = 1;
        while (n.compareTo(BigInteger.ZERO) > 0) {
            ret++;
            n = n.divide(BigInteger.valueOf(2));
        }
        return ret;
    }
    public boolean primalityTest(BigInteger n) {
        if (n.compareTo(BigInteger.valueOf(2)) < 0) {
            return false;
        }
        if (n.compareTo(BigInteger.valueOf(2)) == 0) return true;
        if (!n.testBit(0)) {
            return false;
        }

        int lgn = calculateLogN(n);
        if (perfectPowerTest(n, lgn)) {
            return false;
        }

        int r = calculateR(n, lgn);
        for (int i=2;i<=r;i++) {
            BigInteger g = n.gcd(BigInteger.valueOf(i));
            if (g.compareTo(BigInteger.ONE) > 0 && g.compareTo(n) < 0) {
                return false;
            }
        }
        if (n.compareTo(BigInteger.valueOf(r)) <= 0) return true;

        int l = (int) (Math.sqrt(NumberTheory.phi(r))*lgn);
        for (int i=0;i<=l;i++) {
            Polynomial left = new Polynomial(n,r);
            left.setCoefficient(0,BigInteger.valueOf(i));
            left.setCoefficient(1,BigInteger.valueOf(1));
            left = left.exp(n);
            Polynomial right = new Polynomial(n,r);
            right.setCoefficient(0,BigInteger.valueOf(i));
            right.setCoefficient(n,BigInteger.valueOf(1));
            if (!left.isEqual(right)) {
                return false;
            }
        }
        return true;
    }
}
