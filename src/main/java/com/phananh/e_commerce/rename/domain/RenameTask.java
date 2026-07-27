package com.phananh.e_commerce.rename.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.phananh.e_commerce.core.domain.model.entity.BaseEntity;



@Entity
@Table(name = "rename_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RenameTask extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false)
    private String publicId;

    @Column(name = "target_folder", nullable = false)
    private String targetFolder;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount = 0;

    @Column(name = "last_error", length = 500)
    private String lastError;
}