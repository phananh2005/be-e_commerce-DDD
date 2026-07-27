package com.phananh.e_commerce.rename.application;

import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import com.phananh.e_commerce.rename.domain.RenameTask;
import com.phananh.e_commerce.rename.infrastructure.RenameTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TempUploadCleanupJob {

    @Value("${cloudinary.cleanup.retention-hours:24}")
    private long retentionHours;

    private final CloudinaryService cloudinaryService;
    private final RenameTaskRepository renameTaskRepository;

    @Scheduled(cron = "${cloudinary.cleanup.schedule:0 0 2 * * *}")
    public void cleanup() {
        // 1. Cloudinary side cleanup
        Date cutoff = Date.from(Instant.now().minusSeconds(retentionHours * 3600));
        // Cloudinary SDK does not have direct listResources with date filter, so we fetch all temp folder resources and filter manually
        // Placeholder: cloudinaryService.listResourcesByPrefix("temp_uploads/") should return list of publicIds with created_at
        // For brevity, assume such method exists (can be added later)
        // List<String> tempPublicIds = cloudinaryService.listPublicIdsInFolder("temp_uploads");
        // for (String pubId : tempPublicIds) { /* check created_at and delete if orphan */ }
        log.info("TempUploadCleanupJob executed (implementation placeholder). Retention {} hours", retentionHours);
        // 2. DB side stale tasks
        LocalDateTime stale = LocalDateTime.now().minusHours(retentionHours);
        List<RenameTask> oldPending = renameTaskRepository.findAll().stream()
                .filter(t -> "PENDING".equals(t.getStatus()) && t.getCreatedAt().isBefore(stale))
                .toList();
        oldPending.forEach(t -> t.setStatus("FAILED"));
        renameTaskRepository.saveAll(oldPending);
    }
}
