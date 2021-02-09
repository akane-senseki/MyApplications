package myapp.controllers.toppage;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import myapp.models.User;
import myapp.repositories.UserRepository;

//@Controller+@ResponseBody=@RestController?
@Controller //下記参照
public class TopPageController {

    @Autowired //インスタンス化せずにメソッドを使用できる
    HttpSession session; // セッション

    @Autowired
    UserRepository userrepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView mv) {
        List<User> users = userrepository.findAll();

            mv.addObject("token", session.getId());
            mv.addObject("users" , users);

            if(session.getAttribute("flush") != null) {
            mv.addObject("flush",session.getAttribute("flush"));
            session.removeAttribute("flush");
            }

            mv.setViewName("views/toppage/index");

            return mv;

    }

    @RequestMapping(path = "/top/about" , method = RequestMethod.GET)
    public ModelAndView about(ModelAndView mv) {
        mv.setViewName("views/toppage/about");
        return mv;
    }
}