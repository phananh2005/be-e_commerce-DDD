package com.phananh.e_commerce.rename.application;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import com.phananh.e_commerce.rename.domain.RenameTask;
import com.phananh.e_commerce.rename.infrastructure.RenameTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RenameTaskProcessor {

    private final RenameTaskRepository repository;
    private final CloudinaryService cloudinaryService;
    private final EntityUrlUpdater urlUpdater;

    @Value("${cloudinary.rename.max-retry:5}")
    private int maxRetry;

    @Scheduled(fixedDelayString = "${cloudinary.rename.poll-interval:PT1M}")
    public void poll() {
        List<RenameTask> pending = repository.findByStatus("PENDING", org.springframework.data.domain.PageRequest.ofSize(50));
        pending.forEach(this::process);
    }

    private void process(RenameTask task) {
        task.setAttemptCount(task.getAttemptCount() + 1);
        try {
            String targetPublicId = task.getTargetFolder() + "/" + extractFileName(task.getPublicId());
            cloudinaryService.renameResource(task.getPublicId(), targetPublicId);
            String newUrl = buildUrl(targetPublicId);
            urlUpdater.updateUrl(task.getEntityType(), task.getEntityId(), newUrl);
            task.setStatus("SUCCESS");
        } catch (Exception ex) {
            task.setLastError(ex.getMessage());
            if (task.getAttemptCount() >= maxRetry) {
                task.setStatus("FAILED");
                log.error("RenameTask id={} failed after {} attempts", task.getId(), task.getAttemptCount(), ex);
            }
        }
        repository.save(task);
    }

    private String extractFileName(String publicId) {
        int slash = publicId.lastIndexOf('/');
        return slash >= 0 ? publicId.substring(slash + 1) : publicId;
    }

    private String buildUrl(String publicId) {
        // cloudName injected via Cloudinary bean; constructing standard URL
        return String.format("https://res.cloudinary.com/%s/image/upload/%s", "${cloudinary.cloud_name}", publicId);
    }
}
