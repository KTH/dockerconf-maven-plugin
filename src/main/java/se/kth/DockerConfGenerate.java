package se.kth;


import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
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

@Mojo(name = "validate", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class DockerConfGenerate extends AbstractMojo {

  @Parameter(defaultValue = "${pom.version}", readonly = true)
  private String pomVersion;

  @Parameter(defaultValue = "${basedir}", readonly = true)
  private File directory;

  @Parameter
  private String imageName;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    Path p = directory.toPath().resolve("docker.conf");
    if (p.toFile().exists() && p.toFile().isFile()) {
      getLog().info("docker.conf already exists, file will be overwritten");
    }

    if (imageName == null || "".equals(imageName)) {
      throw new MojoExecutionException("Missing configuration parameter 'imageName' in plugin configuration");
    }

    Pattern pattern = Pattern.compile("(\\d*\\.\\d)");
    Matcher matcher = pattern.matcher(pomVersion);
    String dockerConfVersion = matcher.group();

    String fileContent =
        "IMAGE_NAME=\"" + imageName + "\"\n" +
        "IMAGE_VERSION=\"" + dockerConfVersion + "\"\n";

    try {
      Files.write(p, fileContent.getBytes(UTF_8));
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to write docker.conf", e);
    }
  }

  public void setPomVersion(String pomVersion) {
    this.pomVersion = pomVersion;
  }

  public void setDirectory(File directory) {
    this.directory = directory;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

}