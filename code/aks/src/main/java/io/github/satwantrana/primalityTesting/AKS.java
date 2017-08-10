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
            int nn = n.mod(BigInteger.valueOf(r)).intValue();
            int cur = 1;
            boolean yes = true;
            int end = lgn*lgn;
            for (int j=1;j<=end;j++) {
                cur = (int) (cur*1L*nn)%r;
                if (cur == 1) {
                    yes = false; break;
                }
            }
            if (yes) return r;
        }
    }
    public static int calculateLogN(BigInteger n) {
        return n.bitLength();
    }
    public boolean primalityTest(BigInteger n) {
        if (n.compareTo(BigInteger.valueOf(2)) < 0) {
            return false;
        }

        if (n.compareTo(BigInteger.valueOf(2)) == 0) {
            return true;
        }
        if (!n.testBit(0)) {
            return false;
        }

        int lgn = calculateLogN(n);
        if (perfectPowerTest(n, lgn)) {
            return false;
        }

        int r = calculateR(n, lgn);
        System.out.println("S: " + n + " " + r + " " + lgn + " " + lgn*lgn);
        for (int i=2;i<=r;i++) {
            BigInteger[] dr = n.divideAndRemainder(BigInteger.valueOf(i));
            if (dr[1].compareTo(BigInteger.ZERO) == 0 && dr[0].compareTo(BigInteger.ONE) > 0) return false;
//            BigInteger g = n.gcd(BigInteger.valueOf(i));
//            if (g.compareTo(BigInteger.ONE) > 0 && g.compareTo(n) < 0) return false;
        }
        if (n.compareTo(BigInteger.valueOf(r)) <= 0) return true;

        int l = (int) (Math.sqrt(NumberTheory.phi(r))*lgn);
        System.out.println("S: " + n + " " + r + " " + lgn + " " + lgn*lgn + " " + l);
        for (int a=1; a<=l; a++) {
            Polynomial left = new Polynomial(n,r);
            left.setCoefficient(0,BigInteger.valueOf(a));
            left.setCoefficient(1,BigInteger.valueOf(1));
            left = left.exp(n);
            Polynomial right = new Polynomial(n,r);
            right.setCoefficient(0,BigInteger.valueOf(a));
            right.setCoefficient(n,BigInteger.valueOf(1));
            if (!left.equals(right)) {
                System.out.println("F: " + n + " " + a);
                return false;
            }
        }
        return true;
    }
}
