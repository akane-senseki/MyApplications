package myapp.controllers;

import java.util.List;
import java.util.Optional;

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
public class UserController {

    @Autowired
    HttpSession session; //セッション

    @Autowired
    SecurityData securityData;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(path = "/signin", method = RequestMethod.POST)
    @Transactional
    public ModelAndView userCreate(@ModelAttribute UserForm userForm, ModelAndView mv) {
        List<User> users = userRepository.findByDeleteFlag(0);

        if (userForm.getToken() != null && userForm.getToken().equals(session.getId())) {
            int check = 0;
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getMail().equals(userForm.getMail())) {
                    check = 1;
                }
            }
            if (check == 0) {
                User u = new User();

                u.setName(userForm.getName());
                u.setMail(userForm.getMail());
                u.setAdmin_flag(userForm.getAdmin_flag());
                u.setDeleteFlag(0);
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
                session.setAttribute("login_user_id", u.getId());
                session.setAttribute("token" , session.getId());

            } else {
                mv = new ModelAndView("redirect:/"); // リダイレクト
                session.setAttribute("error", "ご指定のメールアドレスは既に登録されています");
            }

        }else {
            mv = new ModelAndView("redirect:/"); // リダイレクト
            session.setAttribute("error", "登録に失敗しました");
        }

        return mv;

    }

    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    @Transactional
    public ModelAndView loginExec(@ModelAttribute UserForm userForm, ModelAndView mv) {
        Boolean check_result = false; //認証結果を格納する場所

        String mail = userForm.getMail();
        String plain_pass = userForm.getPassword();
        User u = null;

        if (mail != null && !mail.equals("") && plain_pass != null && !plain_pass.equals("")) {
            //64文字のパスワードに変換
            String password = EncryptUtil.getPasswordEncrypt(plain_pass, securityData.getPepper());

            //メールアドレスとパスワードが正しいかの確認。合っていたらuに格納する。
            try {
                u = userRepository.findByMailAndPasswordAndDeleteFlag(mail, password,0);
            } catch (NoResultException ex) {
            }

            if (u != null) {
                check_result = true;
            }

            if (!check_result) { //認証出来なかったら
                userForm.setToken(session.getId());
                userForm.setPassword(""); //入力したパスワードは繰り越さない

                mv.addObject("user", userForm);
                mv = new ModelAndView("redirect:/"); // リダイレクト
                session.setAttribute("error", "ログインに失敗しました");
            } else { //認証出来たら
                session.setAttribute("login_user", u);
                session.setAttribute("login_user_id", u.getId());
                session.setAttribute("token" , session.getId());
                session.setAttribute("flush", "ログインしました");
                mv = new ModelAndView("redirect:/"); // リダイレクト

            }

        }
        return mv;
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(ModelAndView mv) {
        session.removeAttribute("login_user");
        session.removeAttribute("token");
        session.setAttribute("flush", "ログアウトしました");

        mv = new ModelAndView("redirect:/");

        return mv;
    }

    @RequestMapping(path = "/user/destroy" , method = RequestMethod.POST)
    @Transactional
    public ModelAndView userDestroy(@ModelAttribute UserForm userForm, ModelAndView mv) {
        if(session.getAttribute("token") != null && session.getAttribute("token").equals(session.getId())) {

            Optional<User> opu = userRepository.findById((int)session.getAttribute("login_user_id"));
            User u = opu.orElse(null);

            u.setDeleteFlag(1);
            userRepository.save(u);
            session.removeAttribute("login_user");
            session.removeAttribute("token");
            session.setAttribute("flush", "削除しました");
            mv = new ModelAndView("redirect:/");

        }else {
            session.setAttribute("error", "削除に失敗しました");
            mv = new ModelAndView("redirect:/");
        }

        return mv;

    }

}
