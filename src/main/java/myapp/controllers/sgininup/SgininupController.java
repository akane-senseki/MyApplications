package myapp.controllers.sgininup;

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
import myapp.repositories.UserRepository;


@Controller
public class SgininupController {

    @Autowired
    HttpSession session; //セッション

    @Autowired
    SecurityData securityDate;

    @Autowired
    UserRepository userrepository;

    @RequestMapping(path = "/signin" , method = RequestMethod.POST)
    @Transactional // メソッド開始時にトランザクションを開始、終了時にコミットする
    public ModelAndView userCreate(@ModelAttribute UserForm userForm , ModelAndView mv) {

        if(userForm.getToken() != null && )

    }


}
