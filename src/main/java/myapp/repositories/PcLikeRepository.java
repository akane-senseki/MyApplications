package myapp.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.models.PcEntity;
import myapp.models.PcEntityLike;
import myapp.models.User;

@Repository
public interface PcLikeRepository extends JpaRepository<PcEntityLike, Integer>{
    public PcEntityLike findByUserAndPcEntity(User user , PcEntity pcentity);
    public List<PcEntityLike> findByUser(User user);

}