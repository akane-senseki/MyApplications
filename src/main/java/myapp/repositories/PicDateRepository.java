package myapp.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.models.Pic_Data;
import myapp.models.User;

@Repository
public interface PicDateRepository extends JpaRepository<Pic_Data, Integer>{
    public List<Pic_Data> findByUserAndDeleteFlag(User user , Integer deleteFlag);
    public List<Pic_Data> findByDeleteFlagAndReleaseFlag(Integer deleteFlag , Integer releaseFlag);

}
