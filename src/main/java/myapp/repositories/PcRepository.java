package myapp.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.models.Pc_Entity;
import myapp.models.User;

@Repository
public interface PcRepository extends JpaRepository<Pc_Entity, Integer>{
    public List<Pc_Entity> findByUserAndDeleteFlag(User user , Integer deleteFlag);
    public List<Pc_Entity> findByDeleteFlagAndReleaseFlag(Integer deleteFlag, Integer releaseFlag);

    public Page<Pc_Entity> findByUserAndDeleteFlag(User user, Integer deleteFlag, Pageable pageable);
    public Page<Pc_Entity> findByDeleteFlagAndReleaseFlag(Integer deleteFlag, Integer releaseFlag , Pageable pageable);




}