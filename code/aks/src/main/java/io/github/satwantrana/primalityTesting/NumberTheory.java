package io.github.satwantrana.primalityTesting;

/**
 * Created by satwant on 12/2/17.
 */
public class NumberTheory {
    static int phi(int n) {
        int res = 1;
        for (int i=2;i*i<=n;i++) {
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
}
