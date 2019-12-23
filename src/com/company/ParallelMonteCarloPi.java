package com.company;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;


public class ParallelMonteCarloPi {

    private static final ThreadLocal<Random> LOCAL_RANDOM = new ThreadLocal<Random>() {
        protected Random initialValue() {
            return new Random();
        };
    };

    private static int CPU_COUNT;

    public ParallelMonteCarloPi(int ThreadsNumber){
        CPU_COUNT = ThreadsNumber;
    }


    public static final long[] apportion(final long samples) {
        int core = CPU_COUNT;
        final long[] portions = new long[core];
        long remaining = samples;

        while (core > 0) {
            final long part = (remaining - 1 + core) / core;
            core--;
            portions[core] = part;
            remaining -= part;
        }
        return portions;
    }

    public static final double sampleCircleArea(final long samples) {

        long[] counts = apportion(samples);

        long inside = Arrays.stream(counts).parallel().map(ParallelMonteCarloPi::samplePortion).sum();

        return (4.0 * inside) / samples;
    }


    private static final long samplePortion(final long samples) {
        final Random rand = LOCAL_RANDOM.get();
        long inside = 0;
        for (int i = 0; i < samples; i++) {
            if (isInside(rand)) {
                inside++;
            }
        }
        return inside;
    }

    private static final boolean isInside(final Random rand) {
        final double x = rand.nextDouble();
        final double y = rand.nextDouble();
        return x * x + y * y <= 1.0;
    }


    public static void main(String[] args) {


        new ParallelMonteCarloPi(12);

        int ITERATIONS = 1000_000_000;


        long start = System.currentTimeMillis();
        double calc = sampleCircleArea(ITERATIONS);
        long end = System.currentTimeMillis();


        System.out.print("PI is ");
        System.out.printf("%.5f", calc);
        System.out.println("\nTHREADS " + CPU_COUNT);
        System.out.println("ITERATIONS " +  ITERATIONS);
        System.out.println("TIME " + (end - start) + "ms");


    }

}
