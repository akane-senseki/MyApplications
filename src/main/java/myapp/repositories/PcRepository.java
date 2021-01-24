package myapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.models.Pc_Entity;

@Repository
public interface PcRepository extends JpaRepository<Pc_Entity, Integer>{
}

//JpaRepositoryはデータ検索を行う為の仕組み。
//public interface 名前 extends JpaRepository <エンティティ , IDタイプ> IDタイプは基本型の場合はラッパークラスを指定する。

//POSTメソッドではデータをDBに保存する役割りも持つ。