package com.github.timols.seeder.plugins;

import com.github.timols.seeder.Seeder;
import com.google.common.collect.Sets;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

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
        Seeder seeder = new Seeder(stepsPackage, getReflections(stepsPackage));
        try {
            seeder.seed();
        } catch (Exception e) {
            getLog().error(e);
        }
    }

    public void setStepsPackage(String stepsPackage) {
        this.stepsPackage = stepsPackage;
    }

    private MavenProject getProject() {
        return (MavenProject) getPluginContext().get("project");
    }

    private Reflections getReflections(String stepPackageName) throws MojoExecutionException {
        ConfigurationBuilder configuration = new ConfigurationBuilder()
                .addUrls(Sets.newHashSet(buildOutputDirectoryUrl()))
                .addClassLoader(buildProjectClassLoader());

        if (StringUtils.isNotBlank(stepPackageName)) {
            FilterBuilder filterBuilder = new FilterBuilder();
            filterBuilder.include(FilterBuilder.prefix(stepPackageName));
            configuration.filterInputsBy(filterBuilder);
        }

        return new Reflections(configuration);
    }

    private URLClassLoader buildProjectClassLoader() throws MojoExecutionException {
        getLog().debug("Adding Artifacts to ClassLoader");
        List<URL> urls = new ArrayList<>();

        for (Artifact artifact : getProject().getArtifacts()) {
            try {
                urls.add((artifact).getFile().toURI().toURL());
            } catch (MalformedURLException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
        }

        urls.add(buildOutputDirectoryUrl());

        getLog().debug("Artifact URLS:\n" + urls.toString().replace(",", "\n"));

        return new URLClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
    }

    private URL buildOutputDirectoryUrl() throws MojoExecutionException {
        try {
            File outputDirectoryFile = new File(getProject().getBuild().getOutputDirectory() + "/");
            return outputDirectoryFile.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
