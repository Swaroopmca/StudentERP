package com.erp.repository;

import com.erp.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByTargetRole(Notice.TargetRole targetRole);
    
    List<Notice> findByPostedById(Long userId);
    
    @Query("SELECT n FROM Notice n ORDER BY n.postedAt DESC")
    List<Notice> findAllOrderByPostedAtDesc();
    
    @Query("SELECT n FROM Notice n WHERE n.targetRole = :role OR n.targetRole = 'ALL' ORDER BY n.postedAt DESC")
    List<Notice> findNoticesForRole(@Param("role") Notice.TargetRole role);
    
    @Query(value = "SELECT * FROM notices ORDER BY posted_at DESC LIMIT :limit", nativeQuery = true)
    List<Notice> findLatestNotices(@Param("limit") int limit);
    
    @Query("SELECT n FROM Notice n WHERE n.title LIKE %:keyword% OR n.content LIKE %:keyword%")
    List<Notice> searchNotices(@Param("keyword") String keyword);
}