package com.erp.service;

import com.erp.model.Notice;
import com.erp.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Notice getNoticeById(Long id) {
        return noticeRepository.findById(id).orElse(null);
    }

    public Notice saveNotice(Notice notice) {
        if (notice.getPostedAt() == null) {
            notice.setPostedAt(LocalDateTime.now());
        }
        return noticeRepository.save(notice);
    }

    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    public List<Notice> getNoticesForRole(Notice.TargetRole role) {
        if (role == Notice.TargetRole.ALL) {
            return noticeRepository.findByTargetRole(Notice.TargetRole.ALL);
        } else {
            List<Notice> allNotices = new ArrayList<>(noticeRepository.findByTargetRole(Notice.TargetRole.ALL));
            allNotices.addAll(noticeRepository.findByTargetRole(role));
            allNotices.sort((n1, n2) -> n2.getPostedAt().compareTo(n1.getPostedAt()));
            return allNotices;
        }
    }

    public List<Notice> getLatestNotices(int limit) {
        return noticeRepository.findLatestNotices(limit);
    }

    public List<Notice> getNoticesByUser(Long userId) {
        return noticeRepository.findByPostedById(userId);
    }

    public Notice updateNotice(Long id, Notice noticeDetails) {
        Notice notice = getNoticeById(id);
        if (notice != null) {
            notice.setTitle(noticeDetails.getTitle());
            notice.setContent(noticeDetails.getContent());
            notice.setTargetRole(noticeDetails.getTargetRole());
            return noticeRepository.save(notice);
        }
        return null;
    }
}