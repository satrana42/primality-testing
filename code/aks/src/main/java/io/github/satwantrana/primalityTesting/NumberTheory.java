package io.github.satwantrana.primalityTesting;

/**
 * Created by satwant on 12/2/17.
 */
public class NumberTheory {
    static int phi(int n) {
        int res = 1;
        for (int i=2;i<=n/i;i++) {
            int pwr = 1;
            while(n%i == 0) {
                n /= i; pwr *= i;
            }
            if (pwr > 1) {
                res *= pwr/i;
                res  *= i-1;
            }
        }
        if (n > 1) res *= n-1;
        return res;
    }

    static Boolean[] sieve(int n) {
        Boolean[] s = new Boolean[n+1];
        for (int i=0;i<=n;i++) s[i] = true;
        s[0] = s[1] = false;
        for (int i=2; i<=n; i++) {
            for (int j=i; j<=n/i; j++) s[j*i] = false;
        }
        return s;
    }
}
