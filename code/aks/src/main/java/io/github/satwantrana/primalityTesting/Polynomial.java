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
        for (int i=0;i<r;i++) poly.set(i,BigInteger.ZERO);
    }
    public Polynomial(Polynomial init) {
        this.n = init.getN();
        this.r = init.getR();
        poly = new ArrayList<BigInteger>(r);
        for (int i=0;i<r;i++) poly.set(i,init.getCoefficient(i));
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
        poly.set(i, val);
    }
    public void setCoefficient(BigInteger i, BigInteger val) {
        setCoefficient(i.mod(BigInteger.valueOf(r)).intValue(), val);
    }

    public void addCoefficient(int i, BigInteger inc) {
        BigInteger cur = poly.get(i);
        cur = cur.add(inc);
        cur = cur.mod(n);
        if (cur.compareTo(n) < 0) cur = cur.add(n);
        poly.set(i,cur);
    }
    public void subCoefficient(int i, BigInteger dec) {
        addCoefficient(i,dec.negate());
    }
    public void mulCoefficient(int i, BigInteger fac) {
        BigInteger cur = poly.get(i);
        cur = cur.multiply(fac);
        cur = cur.mod(n);
        if (cur.compareTo(n) < 0) cur = cur.add(n);
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
        return mulNaive(other);
    }
    private Polynomial mulNaive(Polynomial other) {
        Polynomial res = new Polynomial(n,r);
        for (int i=0;i<r;i++)
            for (int j=0;j<r;j++) {
                BigInteger inc = getCoefficient(i).multiply(other.getCoefficient(j));
                inc.mod(n);
                res.addCoefficient((i + j) % r, inc);
            }
        return res;
    }

    public Polynomial exp(BigInteger e) {
        Polynomial res = new Polynomial(n, r);
        Polynomial base = new Polynomial(this);
        res.setCoefficient(0, BigInteger.ONE);
        while (e.compareTo(BigInteger.ZERO) > 0) {
            if (e.testBit(0)) res.mul(base);
            base.mul(base);
            e = e.divide(BigInteger.valueOf(2));
        }
        return res;
    }

    public boolean isEqual(Polynomial other) {
        for (int i=0;i<r;i++) if (getCoefficient(i) != other.getCoefficient(i)) return false;
        return true;
    }
}