package myapp.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.models.Pc_Entity_Like;

@Repository
public interface PcLikeRepository extends JpaRepository<Pc_Entity_Like, Integer>{


}