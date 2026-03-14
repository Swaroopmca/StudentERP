package com.erp.controller;

import com.erp.model.Notice;
import com.erp.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@CrossOrigin(origins = "*")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @GetMapping
    public List<Notice> getAllNotices() {
        return noticeService.getAllNotices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNoticeById(@PathVariable Long id) {
        Notice notice = noticeService.getNoticeById(id);
        if (notice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notice);
    }

    @GetMapping("/target/{role}")
    public List<Notice> getNoticesForRole(@PathVariable String role) {
        try {
            Notice.TargetRole targetRole = Notice.TargetRole.valueOf(role.toUpperCase());
            return noticeService.getNoticesForRole(targetRole);
        } catch (IllegalArgumentException e) {
            return noticeService.getNoticesForRole(Notice.TargetRole.ALL);
        }
    }

    @GetMapping("/latest")
    public List<Notice> getLatestNotices(@RequestParam(defaultValue = "10") int limit) {
        return noticeService.getLatestNotices(limit);
    }

    @PostMapping
    public Notice createNotice(@RequestBody Notice notice, @SessionAttribute(value = "userId", required = false) Long userId) {
        if (userId != null) {
            com.erp.model.User user = new com.erp.model.User();
            user.setId(userId);
            notice.setPostedBy(user);
        }
        return noticeService.saveNotice(notice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notice> updateNotice(@PathVariable Long id, @RequestBody Notice notice) {
        Notice existingNotice = noticeService.getNoticeById(id);
        if (existingNotice == null) {
            return ResponseEntity.notFound().build();
        }
        notice.setId(id);
        return ResponseEntity.ok(noticeService.saveNotice(notice));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        Notice existingNotice = noticeService.getNoticeById(id);
        if (existingNotice == null) {
            return ResponseEntity.notFound().build();
        }
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }
}