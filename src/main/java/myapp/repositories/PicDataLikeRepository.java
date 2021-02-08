package myapp.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.models.Pic_Data_Like;

@Repository
public interface PicDataLikeRepository extends JpaRepository<Pic_Data_Like, Integer>{

}