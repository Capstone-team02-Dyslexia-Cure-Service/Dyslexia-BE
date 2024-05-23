package com.capstone.dyslexia.domain.uuidFile.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.capstone.dyslexia.domain.aws.s3.AmazonS3Manager;
import com.capstone.dyslexia.domain.aws.s3.S3PhotoService;
import com.capstone.dyslexia.domain.member.application.MemberService;
import com.capstone.dyslexia.domain.member.domain.Member;
import com.capstone.dyslexia.domain.uuidFile.domain.UUIDFile;
import com.capstone.dyslexia.domain.uuidFile.domain.repository.UUIDFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UUIDFileService {

    private final UUIDFileRepository uuidFileRepository;

    private final AmazonS3Manager amazonS3Manager;

    @Transactional
    public UUIDFile saveFile(MultipartFile file, String path) {
        String uuid = UUID.randomUUID().toString();

        String fileS3Url = amazonS3Manager.uploadFile(path + uuid, file);

        UUIDFile uuidFile = UUIDFile.builder()
                .uuid(uuid)
                .fileUrl(fileS3Url)
                .build();

        uuidFileRepository.save(uuidFile);

        return uuidFile;
    }

    public UUIDFile savePronunciation(MultipartFile file) {
        return saveFile(file, amazonS3Manager.generatePronunciationKeyName());
    }

    public UUIDFile saveVideo(MultipartFile file) {
        return saveFile(file, amazonS3Manager.generateVideoKeyName());
    }

    public UUIDFile saveSubmission(Member member, MultipartFile file) {
        return saveFile(file, amazonS3Manager.generateSubmissionFileKeyName(member.getId()));
    }
}
