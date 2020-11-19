package se.kth.maven.plugin.it;

import static com.soebes.itf.extension.assertj.MavenITAssertions.assertThat;

import com.soebes.itf.jupiter.extension.MavenGoal;
import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenRepository;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.maven.MavenExecutionResult;

@MavenJupiterExtension
@MavenGoal("package")
@MavenRepository
class DockerConfValidateIT {

  @MavenTest
  void success(MavenExecutionResult result) {
    assertThat(result)
        .isSuccessful();
  }

  @MavenTest
  void failure(MavenExecutionResult result) {
    assertThat(result)
        .isFailure();
  }
}
