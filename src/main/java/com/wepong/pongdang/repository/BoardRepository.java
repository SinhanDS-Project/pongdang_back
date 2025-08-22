package com.wepong.pongdang.repository;

import com.wepong.pongdang.entity.BoardEntity;
import com.wepong.pongdang.entity.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, String> {
    Page<BoardEntity> findByCategory(Category category, Pageable pageable);

    int countByCategory(Category category);

    List<BoardEntity> findByCategory(Category category);
}