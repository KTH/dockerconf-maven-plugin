package se.kth;


import static java.lang.String.format;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

@Mojo(name = "validate", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class DockerConfValidate extends AbstractMojo {

  @Parameter(defaultValue = "${pom.version}", readonly = true)
  private String pomVersion;

  @Parameter(defaultValue = "${basedir}")
  private File directory;

  public void execute() throws MojoExecutionException, MojoFailureException {
    Path p = directory.toPath().resolve("docker.conf");
    if (!p.toFile().exists() || !p.toFile().isFile()) {
      throw new MojoFailureException("docker.conf is missing in project directory");
    }
    boolean found = false;
    try {
      for (String s : Files.readAllLines(p)) {
        if (s.startsWith("IMAGE_VERSION")) {
          found = true;
          Pattern pattern = Pattern.compile("\\d*\\.\\d");
          Matcher matcher = pattern.matcher(s);
          if (matcher.find() && pomVersion.startsWith(matcher.group())) {
            getLog().info(format("POM version (%s) and docker.conf version (%s) match", pomVersion, matcher.group()));
          } else {
            throw new MojoExecutionException(format("POM version (%s) and docker.conf version (%s) do not match",
                pomVersion, matcher.group()));
          }
          return;
        }
      }
      throw new MojoExecutionException("Cpuld not find property IMAGE_VERSION in docker.conf");
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to read version from docker.conf", e);
    }
  }
}
