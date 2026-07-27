package com.phananh.e_commerce.rename.infrastructure;

import com.phananh.e_commerce.rename.domain.RenameTask;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RenameTaskRepository extends JpaRepository<RenameTask, Long> {

    List<RenameTask> findByStatus(String status, Pageable pageable);

    boolean existsByPublicId(String publicId);
}