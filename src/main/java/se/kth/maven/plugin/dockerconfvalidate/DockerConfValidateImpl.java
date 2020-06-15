package se.kth.maven.plugin.dockerconfvalidate;


import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

public class DockerConfValidateImpl {

  public String compareVersions(File dockerConfFile, String pomVersion)
      throws MojoFailureException, MojoExecutionException {

    if (!dockerConfFile.exists() || !dockerConfFile.isFile()) {
      throw new MojoExecutionException("docker.conf is missing in project directory");
    }
    final String[] pomVersionSplit = pomVersion.split("\\.");
    if (pomVersionSplit.length < 2) {
      throw new MojoExecutionException(
          format("POM version does not contain major and minor version (%s)", pomVersion));
    }
    final String pomMajorMinor = pomVersionSplit[0] + "." + pomVersionSplit[1];
    try {
      for (String s : Files.readAllLines(dockerConfFile.toPath())) {
        if (s.startsWith("IMAGE_VERSION")) {
          Pattern pattern = Pattern.compile("\\d+\\.\\d+");
          Matcher matcher = pattern.matcher(s);
          if (matcher.find()) {
            if (pomMajorMinor.equals(matcher.group())) {
              return format("POM version (%s) and docker.conf version (%s) match", pomMajorMinor, matcher.group());
            } else {
              throw new MojoExecutionException(format("POM version (%s) and docker.conf version (%s) do not match",
                  pomMajorMinor, matcher.group()));
            }
          } else {
            throw new MojoExecutionException(
                format("Property IMAGE_VERSION in docker.conf is not correctly formatted (%s)", s));
          }
        }
      }
      throw new MojoExecutionException("Could not find property IMAGE_VERSION in docker.conf");
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to read version from docker.conf", e);
    }
  }
}
