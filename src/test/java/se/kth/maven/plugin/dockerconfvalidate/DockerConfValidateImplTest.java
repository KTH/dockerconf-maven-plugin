package se.kth.maven.plugin.dockerconfvalidate;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Test;

class DockerConfValidateImplTest {

  private DockerConfValidateImpl validate = new DockerConfValidateImpl();

  @Test
  public void validateOk() throws IOException, MojoFailureException, MojoExecutionException {
    File dockerConfFile = createTempFile(
        "IMAGE_NAME=\"maven-plugin-dockerconf-validate\"",
        "IMAGE_VERSION=\"3.50\"");
    assertThat(validate.compareVersions(dockerConfFile, "3.50"))
        .contains("(3.50)");
  }

  @Test
  public void validateDifferentVersions() throws IOException {
    File dockerConfFile = createTempFile(
        "IMAGE_NAME=\"maven-plugin-dockerconf-validate\"",
        "IMAGE_VERSION=\"3.5\"");
    assertThatThrownBy(() ->  validate.compareVersions(dockerConfFile, "3.50"))
        .isInstanceOf(MojoExecutionException.class)
        .hasMessageContainingAll("do not match", "(3.5)", "(3.50)");
  }

  @Test
  public void validatePomMajorVersionOnly() throws IOException {
    File dockerConfFile = createTempFile(
        "IMAGE_NAME=\"maven-plugin-dockerconf-validate\"",
        "IMAGE_VERSION=\"3.5\"");
    assertThatThrownBy(() ->  validate.compareVersions(dockerConfFile, "3"))
        .isInstanceOf(MojoExecutionException.class)
        .hasMessageContainingAll("minor version", "(3)");
  }

  @Test
  public void validateImageVersionWrongFormatInDockerConf() throws IOException{
    File dockerConfFile = createTempFile(
        "IMAGE_NAME=\"maven-plugin-dockerconf-validate\"",
        "IMAGE_VERSION=\"3.a50\"");
    assertThatThrownBy(() ->  validate.compareVersions(dockerConfFile, "3.50"))
        .isInstanceOf(MojoExecutionException.class)
        .hasMessageContainingAll("correctly formatted", "(IMAGE_VERSION=\"3.a50\")");
  }

  @Test
  public void validateMissingDockerConfFile() throws IOException, MojoFailureException, MojoExecutionException {
    final File nonExistingFile = new File("target/does_not_exist-" + UUID.randomUUID());
    assertThatThrownBy(() ->  validate.compareVersions(nonExistingFile, "3.5"))
        .isInstanceOf(MojoExecutionException.class)
        .hasMessageContainingAll("missing", "docker.conf");
  }

  private File createTempFile(String... lines) throws IOException {
    final Path dockerConf = Files.createTempFile("maven-plugin-dockerconf-validate-test-", "");
    Files.write(dockerConf, Arrays.asList(lines), UTF_8);
    return dockerConf.toFile();
  }
}
