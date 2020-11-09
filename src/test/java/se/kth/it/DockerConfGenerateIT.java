package se.kth.it;

import com.soebes.itf.extension.assertj.MavenITAssertions;
import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.maven.MavenExecutionResult;

@MavenJupiterExtension
class DockerConfGenerateIT {

  @MavenTest
  void success(MavenExecutionResult result) {
    MavenITAssertions.assertThat(result)
        .isSuccessful();
  }
}
