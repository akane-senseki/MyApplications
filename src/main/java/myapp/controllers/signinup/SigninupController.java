package myapp.controllers.signinup;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import myapp.config.SecurityData;
import myapp.forms.UserForm;
import myapp.models.User;
import myapp.repositories.UserRepository;
import myapp.util.EncryptUtil;

@Controller
public class SigninupController {

    @Autowired
    HttpSession session; //セッション

    @Autowired
    SecurityData securityData;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(path = "/signin", method = RequestMethod.POST)
    @Transactional // メソッド開始時にトランザクションを開始、終了時にコミットする
    public ModelAndView userCreate(@ModelAttribute UserForm userForm, ModelAndView mv) {

        System.out.println("サインインアップcontroller通過");
        System.out.println(userForm.getToken());

        if (userForm.getToken() != null && userForm.getToken().equals(session.getId())) {

            User u = new User();

            u.setName(userForm.getName());
            u.setMail(userForm.getMail());
            u.setAdmin_flag(userForm.getAdmin_flag());
            u.setDelete_flag(0);
            u.setPassword(
                    EncryptUtil.getPasswordEncrypt(
                            userForm.getPassword(),
                            securityData.getPepper()));

            userForm.setToken(session.getId());
            mv.addObject("user", userForm);
            mv = new ModelAndView("redirect:/"); // リダイレクト

            userRepository.save(u); //DBに保存
            session.setAttribute("flush", "登録が完了しました");
            session.setAttribute("login_user", u);

        } else {
            mv = new ModelAndView("redirect:/"); // リダイレクト
            session.setAttribute("flush", "登録に失敗しました");
        }
        return mv;

    }

    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    @Transactional
    public ModelAndView loginExec(@ModelAttribute UserForm userForm, ModelAndView mv) {
        System.out.println("signupコントローラー通過");
        Boolean check_result = false; //認証結果を格納する場所

        String mail = userForm.getMail();
        String plain_pass = userForm.getPassword();

        User u = null;

        if (mail != null && !mail.equals("") && plain_pass != null && !plain_pass.equals("")) {
            //64文字のパスワードに変換
            String password = EncryptUtil.getPasswordEncrypt(plain_pass, securityData.getPepper());

            //メールアドレスとパスワードが正しいかの確認。合っていたらuに格納する。
            try {
                u = userRepository.findByMailAndPassword(mail, password);
            } catch (NoResultException ex) {
            }

            if (u != null) {
                check_result = true;
            }

            if (!check_result) { //認証出来なかったら
                System.out.println("ログイン失敗");
                userForm.setToken(session.getId());
                userForm.setPassword(""); //入力したパスワードは繰り越さない

                mv.addObject("user", userForm);
                mv = new ModelAndView("redirect:/"); // リダイレクト
                session.setAttribute("flush", "ログインに失敗しました");
            } else { //認証出来たら
                System.out.println("ログイン成功");
                session.setAttribute("login_user", u);
                session.setAttribute("flush", "ログインしました");
                mv = new ModelAndView("redirect:/"); // リダイレクト

            }

        }
        return mv;
    }

    @RequestMapping(path = "/logout" , method = RequestMethod.GET)
    public ModelAndView logout(ModelAndView mv) {
        session.removeAttribute("login_user");
        session.setAttribute("flush", "ログアウトしました");

        mv = new ModelAndView("redirect:/");

        return mv;
    }

}
