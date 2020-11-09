package se.kth.it;

import static com.soebes.itf.extension.assertj.MavenITAssertions.assertThat;

import com.soebes.itf.jupiter.extension.MavenGoal;
import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenRepository;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.maven.MavenExecutionResult;

@MavenJupiterExtension
@MavenGoal("process-classes")
@MavenRepository
class DockerConfGenerateIT {

  @MavenTest
  void success(MavenExecutionResult result) {
    String expectedDockerConf =
        "IMAGE_NAME=\"test\"\n" +
        "IMAGE_VERSION=\"3.1\"\n";

    assertThat(result)
        .isSuccessful()
        .project()
        .withFile("../docker.conf")
            .hasContent(expectedDockerConf);
  }

  @MavenTest
  void failure_missing_image_name(MavenExecutionResult result) {
    String expectedDockerConf =
        "IMAGE_NAME=\"failure_missing_image_name\"\n" +
        "IMAGE_VERSION=\"3.0\"\n";



    assertThat(result)
        .isFailure()
        .err()
            .error().contains("imageName");

//    assertThat(result)
//        .project()
//        .withFile("../docker.conf")
//            .hasContent(expectedDockerConf);
  }
}
