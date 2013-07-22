package com.github.timols.seeder;

import org.junit.Test;

import static org.junit.Assert.fail;

public class SeederTest {

    @Test
    public void testSeed() throws Exception {
        Seeder seeder = new Seeder("com.example.seed");
        try {
            seeder.seed();
        } catch (Exception e) {
            fail("Seeding should succeed");
        }
    }
}
