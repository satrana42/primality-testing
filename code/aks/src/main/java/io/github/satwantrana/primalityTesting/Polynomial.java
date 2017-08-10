package io.github.satwantrana.primalityTesting;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by satwant on 12/2/17.
 */
public class Polynomial {
    private int r;
    private BigInteger n;
    private ArrayList<BigInteger> poly;

    public Polynomial(BigInteger n, int r) {
        this.n = n;
        this.r = r;
        poly = new ArrayList<BigInteger>(r);
        for (int i=0;i<r;i++) poly.add(BigInteger.ZERO);
    }
    public Polynomial(Polynomial init) {
        this.n = init.getN();
        this.r = init.getR();
        poly = new ArrayList<BigInteger>(r);
        for (int i=0;i<r;i++) poly.add(init.getCoefficient(i));
    }

    public int getR() {
        return r;
    }
    public BigInteger getN() {
        return n;
    }

    public BigInteger getCoefficient(int i) {
        return poly.get(i);
    }
    public BigInteger getCoefficient(BigInteger i) {
        return getCoefficient(i.mod(BigInteger.valueOf(r)).intValue());
    }

    public void setCoefficient(int i, BigInteger val) {
        i = i%r;
        val = val.mod(n);
        if (val.compareTo(BigInteger.ZERO) < 0) val = val.add(n);
        poly.set(i, val);
    }
    public void setCoefficient(BigInteger i, BigInteger val) {
        setCoefficient(i.mod(BigInteger.valueOf(r)).intValue(), val);
    }

    public void addCoefficient(int i, BigInteger inc) {
        BigInteger cur = poly.get(i);
        cur = cur.add(inc);
        cur = cur.mod(n);
        if (cur.compareTo(BigInteger.ZERO) < 0) cur = cur.add(n);
        poly.set(i,cur);
    }
    public void addCoefficient(BigInteger i, BigInteger inc) {
        addCoefficient(i.mod(BigInteger.valueOf(r)).intValue(), inc);
    }
    public void subCoefficient(int i, BigInteger dec) {
        addCoefficient(i,dec.negate());
    }
    public void mulCoefficient(int i, BigInteger fac) {
        BigInteger cur = poly.get(i);
        cur = cur.multiply(fac);
        cur = cur.mod(n);
        if (cur.compareTo(BigInteger.ZERO) < 0) cur = cur.add(n);
        poly.set(i,cur);
    }

    public Polynomial add(Polynomial other) {
        Polynomial res = new Polynomial(this);
        for (int i=0;i<r;i++) res.addCoefficient(i,other.getCoefficient(i));
        return res;
    }
    public Polynomial sub(Polynomial other) {
        Polynomial res = new Polynomial(this);
        for (int i=0;i<r;i++) res.subCoefficient(i,other.getCoefficient(i));
        return res;
    }

    public Polynomial mul(Polynomial other) {
        if (r <= 10) return mulNaive(other);
        Polynomial temp = mulKaratsuba(other);
        Polynomial res = new Polynomial(n, r);
        int s = (r + 1) / 2;
        for (int i = 0; i < 4 * s; i++) res.addCoefficient(i % r, temp.getCoefficient(i));
        return res;
    }

    private Polynomial mulNaive(Polynomial other) {
        Polynomial res = new Polynomial(n,r);
        for (int i=0;i<r;i++)
            for (int j=0;j<r;j++) {
                BigInteger inc = getCoefficient(i).multiply(other.getCoefficient(j));
                inc = inc.mod(n);
//                if (inc.compareTo(BigInteger.ZERO) < 0) inc = inc.add(n);
                res.addCoefficient((i + j) % r, inc);
            }
        return res;
    }
    private Polynomial mulKaratsuba(Polynomial other) {
        int s = (r+1)/2;
        Polynomial res = new Polynomial(n, 4 * s);
        if (r <= 2) {
            for (int i=0;i<r;i++)
                for (int j=0;j<r;j++) {
                    BigInteger inc = getCoefficient(i).multiply(other.getCoefficient(j));
                    inc = inc.mod(n);
//                    if (inc.compareTo(BigInteger.ZERO) < 0) inc = inc.add(n);
                    res.addCoefficient(i+j, inc);
                }
        } else {
            Polynomial A = new Polynomial(n, s);
            Polynomial B = new Polynomial(n, s);
            Polynomial C = new Polynomial(n, s);
            Polynomial D = new Polynomial(n, s);
            for (int i = 0; i < r; i++) {
                if (i < s) {
                    A.setCoefficient(i, getCoefficient(i));
                    C.setCoefficient(i, other.getCoefficient(i));
                } else {
                    B.setCoefficient(i - s, getCoefficient(i));
                    D.setCoefficient(i - s, other.getCoefficient(i));
                }
            }
            Polynomial t1 = B.mulKaratsuba(D);
            for (int i = 0; i < 2 * s; i++) res.addCoefficient(2 * s + i, t1.getCoefficient(i));
            Polynomial t2 = A.mulKaratsuba(C);
            for (int i = 0; i < 2 * s; i++) res.addCoefficient(i, t2.getCoefficient(i));
            Polynomial t3 = A.add(B).mulKaratsuba(C.add(D)).sub(t1).sub(t2);
            for (int i = 0; i < 2 * s; i++) res.addCoefficient(s + i, t3.getCoefficient(i));
        }
        return res;
    }

    public Polynomial exp(BigInteger e) {
        Polynomial res = new Polynomial(n, r);
        Polynomial base = new Polynomial(this);
        res.setCoefficient(0, BigInteger.ONE);
        while (e.compareTo(BigInteger.ZERO) > 0) {
            if (e.testBit(0)) res = res.mul(base);
            base = base.mul(base);
            e = e.divide(BigInteger.valueOf(2));
        }
        return res;
    }

    public boolean equals(Polynomial other) {
        for (int i=0;i<r;i++) if (!getCoefficient(i).equals(other.getCoefficient(i))) return false;
        return true;
    }
}
