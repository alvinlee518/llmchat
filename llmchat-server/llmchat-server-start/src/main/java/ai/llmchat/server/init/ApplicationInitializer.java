package ai.llmchat.server.init;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.platform.MinioFileStorageClientFactory;
import org.dromara.x.file.storage.spring.SpringFileStorageProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApplicationInitializer implements ApplicationRunner {

	private final SpringFileStorageProperties springFileStorageProperties;

	public ApplicationInitializer(SpringFileStorageProperties springFileStorageProperties) {
		this.springFileStorageProperties = springFileStorageProperties;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		for (SpringFileStorageProperties.SpringMinioConfig minioConfig : springFileStorageProperties.getMinio()) {
			if (!minioConfig.getEnableStorage()) {
				continue;
			}
			try (MinioFileStorageClientFactory clientFactory = new MinioFileStorageClientFactory(minioConfig)) {
				try (MinioClient client = clientFactory.getClient()) {
					String bucketName = minioConfig.getBucketName();
					boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
					if (!exists) {
						client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
					}
					client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(String.format("""
							{
							    "Version": "2012-10-17",
							    "Statement": [
							        {
							            "Effect": "Allow",
							            "Principal": {
							                "AWS": [
							                    "*"
							                ]
							            },
							            "Action": [
							                "s3:GetBucketLocation",
							                "s3:GetObject"
							            ],
							            "Resource": [
							                "arn:aws:s3:::*"
							            ]
							        }
							    ]
							}""", bucketName)).build());
				}
			}
		}
	}

}
