package myapp.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.models.PicData;
import myapp.models.User;

@Repository
public interface PicDateRepository extends JpaRepository<PicData, Integer>{
    public List<PicData> findByUserAndDeleteFlag(User user , Integer deleteFlag);
    public List<PicData> findByDeleteFlagAndReleaseFlag(Integer deleteFlag , Integer releaseFlag);

    public Page<PicData> findByUserAndDeleteFlag(User user , Integer deleteFlag, Pageable pageable);
    public Page<PicData> findByDeleteFlagAndReleaseFlag(Integer deleteFlag , Integer releaseFlag, Pageable pageable);

    public Long countByUserAndDeleteFlag(User user , Integer deleteFlag);
    public Long countByDeleteFlagAndReleaseFlag(Integer deleteFlag, Integer releaseFlag);
}
