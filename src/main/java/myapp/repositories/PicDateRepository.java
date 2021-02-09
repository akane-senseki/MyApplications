package myapp.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.models.PicData;
import myapp.models.User;

@Repository
public interface PicDateRepository extends JpaRepository<PicData, Integer>{
    public List<PicData> findByUserAndDeleteFlag(User user , Integer deleteFlag);
    public List<PicData> findByDeleteFlagAndReleaseFlag(Integer deleteFlag , Integer releaseFlag);

}
