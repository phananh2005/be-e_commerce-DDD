package com.phananh.e_commerce.rename.application;

import com.phananh.e_commerce.rename.domain.RenameTask;
import com.phananh.e_commerce.rename.infrastructure.RenameTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RenameTaskCreator {
    private final RenameTaskRepository repository;

    public void createTask(String tempPublicId, String targetFolder, String entityType, Long entityId) {
        RenameTask task = new RenameTask();
        task.setPublicId(tempPublicId);
        task.setTargetFolder(targetFolder);
        task.setEntityType(entityType);
        task.setEntityId(entityId);
        repository.save(task);
    }
}
