package myapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myapp.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    public Long countByMailEquals(String mail);
    //メールアドレスが一致する件数を調べる(0じゃなければエラーをバリデーションで返す)

    public User findByMailAndPasswordAndDeleteFlag(String mail, String password , Integer deleteFlag);
    //検索→メールとパスワードが一致するもの

    public List<User> findByDeleteFlag(Integer deleteFlag);

}
