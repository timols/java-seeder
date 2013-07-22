package com.example.seed;

import com.google.inject.Inject;
import com.github.timols.seeder.annotation.Step;
import com.github.timols.seeder.step.SeedStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Step(number = "1")
public class Step1_FirstStep implements SeedStep {

    private static final Logger logger = LoggerFactory.getLogger(SeedStep.class);
    private final PersistenceService persistenceService;

    @Inject
    public Step1_FirstStep(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void perform() {
        logger.info("PERFORMING FIRST STEP!");
        Object newObject = persistenceService.find();
        logger.debug("Object Hash: " + newObject.hashCode());
    }

}
