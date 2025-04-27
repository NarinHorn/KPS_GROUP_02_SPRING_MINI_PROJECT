package springkpsgroup02.kps.Config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    private String url = "http://localhost:9000";
    private String accessKey = "admin";
    private String secretKey = "admin123";

    @Bean
    public MinioClient minioClient() {

        MinioClient minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        return minioClient;
    }
}
