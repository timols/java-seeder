package com.github.timols.seeder.plugins;

import com.github.timols.seeder.Seeder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Example pom.xml configuration:
 * {@code}
 * <build>
 *     <plugins>
 *         <plugin>
 *             <groupId>com.github.timols</groupId>
 *             <artifactId>seeder</artifactId>
 *             <version>1.0</version>
 *             <configuration>
 *                 <stepsPackage>com.example.seed</stepsPackage>
 *             </configuration>
 *         </plugin>
 *     </plugins>
 * </build>
 */
@Mojo(name = "seed")
public class SeederMojo extends AbstractMojo {

    @Parameter(property = "seed.stepsPackage", defaultValue = "com.github.timols.seeder.step")
    private String stepsPackage;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Seeder seeder = new Seeder(stepsPackage);
        try {
            seeder.seed();
        } catch (Exception e) {
            getLog().error(e);
        }
    }

    public void setStepsPackage(String stepsPackage) {
        this.stepsPackage = stepsPackage;
    }
}
