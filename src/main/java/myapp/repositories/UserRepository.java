package myapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    public Long countByMailEquals(String mail);
    //メールアドレスが一致する件数を調べる(0じゃなければエラーをバリデーションで返す)

    public User findByMailAndPassword(String mail, String password);
    //検索→メールとパスワードが一致するもの

}

//JpaRepositoryはデータ検索を行う為の仕組み。
//public interface 名前 extends JpaRepository <エンティティ , IDタイプ> IDタイプは基本型の場合はラッパークラスを指定する。