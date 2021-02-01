package myapp.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.models.Pc_Entity;

@Repository
public interface PicDateRepository extends JpaRepository<Pc_Entity, Integer>{

}
