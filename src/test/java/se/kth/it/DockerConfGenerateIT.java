package se.kth.it;

import static com.soebes.itf.extension.assertj.MavenITAssertions.assertThat;

import com.soebes.itf.jupiter.extension.MavenGoal;
import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenRepository;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.maven.MavenExecutionResult;

@MavenJupiterExtension
@MavenGoal("validate")
@MavenRepository
class DockerConfGenerateIT {

  @MavenTest
  void success(MavenExecutionResult result) {
    assertThat(result)
        .isSuccessful();
  }

  @MavenTest
  void failure_missing_image_name(MavenExecutionResult result) {
    assertThat(result)
        .isFailure();
  }
}
