package myapp.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.models.PicData;
import myapp.models.PicDataLike;
import myapp.models.User;

@Repository
public interface PicDataLikeRepository extends JpaRepository<PicDataLike, Integer>{
    public PicDataLike findByUserAndPcEntity(User user , PicData picdata);
    public List<PicDataLike> findByUser(User user);
}