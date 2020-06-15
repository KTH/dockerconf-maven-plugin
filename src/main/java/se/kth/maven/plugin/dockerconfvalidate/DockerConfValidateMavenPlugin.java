package se.kth.maven.plugin.dockerconfvalidate;


import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name="validate", defaultPhase=LifecyclePhase.VALIDATE)
public class DockerConfValidateMavenPlugin extends AbstractMojo {

  @Parameter(defaultValue = "${project.version}", readonly = true)
  private String pomVersion;

  @Parameter(defaultValue = "${project.basedir}")
  private File directory;

  @Override
  public void execute() throws MojoFailureException, MojoExecutionException {
    File dockerConfFile = directory.toPath().resolve("docker.conf").toFile();
    final DockerConfValidateImpl validate = new DockerConfValidateImpl();
    final String result = validate.compareVersions(dockerConfFile, pomVersion);
    getLog().info(result);
  }
}
