package myapp.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import myapp.repositories.UserRepository;


@Controller
public class TopPageController {

    @Autowired
    HttpSession session;

    @Autowired
    UserRepository userrepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView mv) {
            mv.addObject("token", session.getId());
            if(session.getAttribute("flush") != null) {
            mv.addObject("flush",session.getAttribute("flush"));
            session.removeAttribute("flush");
            }
            if(session.getAttribute("error") != null) {
            mv.addObject("error",session.getAttribute("error"));
            session.removeAttribute("error");
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