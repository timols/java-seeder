package com.example.seed;

import com.google.inject.AbstractModule;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PersistenceService.class).to(PersistenceServiceImpl.class);
    }
}
