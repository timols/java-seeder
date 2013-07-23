package com.github.timols.seeder;

import org.junit.Test;
import org.reflections.Reflections;

import static org.junit.Assert.fail;

public class SeederTest {

    @Test
    public void testSeed() throws Exception {
        String packageName = "com.example.seed";
        Seeder seeder = new Seeder(packageName, new Reflections(packageName));
        try {
            seeder.seed();
        } catch (Exception e) {
            fail("Seeding should succeed");
        }
    }
}
