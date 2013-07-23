# Introduction

Seeder is a first step to implementing a similar library in java to ruby's [Seed Fu](https://github.com/mbleigh/seed-fu).

It is currently only bound to Guice for dependency injection and is ORM agnostic. You only need to define a series of steps
to seed the database using java as opposed to sql. This is a perfect opportunity to use libraries like [Faker](https://github.com/DiUS/java-faker).

## Getting Started

  1. Add your maven plugin dependency
```xml
<build>
    ...
    <pluginManagement>
        <plugins>
            <plugin>
                <groupId>com.github.timols</groupId>
                <artifactId>seeder</artifactId>
                <version>0.1.0</version>
            </plugin>
        </plugins>
    </pluginManagement>
    ...
    <plugins>
        <plugin>
            <artifactId>seeder</artifactId>
            <configuration>
               <stepsPackage>YOUR STEPS PACKAGE, e.g. com.myapp.steps</stepsPackage>
            </configuration>
        </plugin>
    </plugins>
</build>
```

  2. Define a module in `YOUR STEPS PACKAGE` that sets up all of your Guice bindings
  3. In the same package as the module, create classes that implement the `SeedStep` interface and do your actual work
  4. Annotate your step classes with the `@Step(number = "STEP NUMBER")` annotation to define the order in which the steps should run.
    1. It may be helpful to name your classes either entirely or with the prefix `StepN` where N is the step number. This isn't necessary but makes it easier to keep track of order using filenames.
  5. Run `mvn seeder:seed` from the root of the project to perform the steps

You can see an example implementation [here](src/test/java/com/example).

## Troubleshooting

You might come across errors about missing classes, such as `A required class was missing while executing com.github.timols:seeder:0.1.0:seed: com/foo/FooClass;`. To resolve this issue, add
the library that contains the relevant type as a plugin dependency. I.e.

```xml
<plugin>
    <artifactId>seeder</artifactId>
    <configuration>
       <stepsPackage>YOUR STEPS PACKAGE, e.g. com.myapp.steps</stepsPackage>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>com.foo</groupId>
            <artifactId>bar</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
</plugin>
```

## Contributing

* Check out the latest master to make sure the feature hasn't been implemented or the bug hasn't been fixed yet
* Check out the issue tracker to make sure someone already hasn't requested it and/or contributed it
* Fork the project
* Start a feature/bugfix branch
* Commit and push until you are happy with your contribution
* Make sure to add tests for it. This is important so I don't break it in a future version unintentionally.
* Please try not to mess with the version, or history. If you want to have your own version, or is otherwise necessary, that is fine,
  but please isolate to its own commit so I can cherry-pick around it.

## Copyright

Copyright (c) 2013 Tim Olshansky. See LICENSE for further details.
