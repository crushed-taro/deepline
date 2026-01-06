package crushedtaro.deeplinebackend.domain.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import crushedtaro.deeplinebackend.domain.notice.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

  Page<Notice> findAllByIsDeletedOrderByIsPinnedDescCreatedAtDesc(
      String isDeleted, Pageable pageable);
}
