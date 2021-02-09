package myapp.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.models.PcEntity;
import myapp.models.User;

@Repository
public interface PcRepository extends JpaRepository<PcEntity, Integer>{
    public List<PcEntity> findByUserAndDeleteFlag(User user , Integer deleteFlag);
    public List<PcEntity> findByDeleteFlagAndReleaseFlag(Integer deleteFlag, Integer releaseFlag);

    public Page<PcEntity> findByUserAndDeleteFlag(User user, Integer deleteFlag, Pageable pageable);
    public Page<PcEntity> findByDeleteFlagAndReleaseFlag(Integer deleteFlag, Integer releaseFlag , Pageable pageable);




}