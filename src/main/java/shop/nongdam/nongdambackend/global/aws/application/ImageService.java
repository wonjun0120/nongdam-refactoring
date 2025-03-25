package shop.nongdam.nongdambackend.global.aws.application;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.nongdam.nongdambackend.global.aws.exception.InvalidImageUploadException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.image-path}")
    private String imagePath;

    @Value("${aws.s3.url-format}")
    private String s3UrlFormat;

    private static final String IMAGE_EXTENSION = ".jpg";

    public String saveImage(MultipartFile image) {
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = uploadImage(image);
        }
        return imageUrl;
    }

    private String uploadImage(MultipartFile file) {
        String key = imagePath + UUID.randomUUID() + IMAGE_EXTENSION;
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
            return String.format(s3UrlFormat, bucketName, key);
        } catch (IOException e) {
            throw new InvalidImageUploadException();
        }
    }
}
