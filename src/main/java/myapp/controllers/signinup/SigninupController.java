package myapp.controllers.signinup;

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

    @RequestMapping(path = "/signin" , method = RequestMethod.POST)
    @Transactional // メソッド開始時にトランザクションを開始、終了時にコミットする
    public ModelAndView userCreate(@ModelAttribute UserForm userForm , ModelAndView mv) {

        System.out.println("サインインアップcontroller通過");
        System.out.println(userForm.getToken());

        if(userForm.getToken() != null && userForm.getToken().equals(session.getId())) {
            System.out.println("tokenをキャッチ");
            System.out.println(userForm.getToken());


            User u = new User();

            u.setName(userForm.getName());
            u.setMail(userForm.getMail());
            u.setAdmin_flag(userForm.getAdmin_flag());
            u.setPassword(
                    EncryptUtil.getPasswordEncrypt(
                    userForm.getPassword(),
                    securityData.getPepper()));

            userForm.setToken(session.getId());
            mv.addObject("user",userForm);
            mv.setViewName("/");

            session.setAttribute("flush", "登録が完了しました");
            session.setAttribute("login_user", userForm);

        }else {
            System.out.println("tokenをキャッチ出来ず……");
            System.out.println(userForm.getToken());
            System.out.println(userForm.getName());
            mv.setViewName("/");
            session.setAttribute("flush", "登録に失敗しました");
        }
        return mv;

    }


}
