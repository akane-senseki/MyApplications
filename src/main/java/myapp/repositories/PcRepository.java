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
    public List<Pc_Entity> findByUser(User user);
    public Page<Pc_Entity> findByDeleteFlag(Integer deleteFlag, Pageable pageable);
    public Page<Pc_Entity> findByUser(User user, Pageable pageable);




}

//JpaRepositoryはデータ検索を行う為の仕組み。
//public interface 名前 extends JpaRepository <エンティティ , IDタイプ> IDタイプは基本型の場合はラッパークラスを指定する。

//POSTメソッドではデータをDBに保存する役割りも持つ。