package myapp.controllers.PcDice;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import myapp.config.SecurityData;
import myapp.forms.Pic_DataForm;
import myapp.repositories.PicDateRepository;

@Controller
public class PcDiceController {
    @Autowired
    HttpSession session;

    @Autowired
    PicDateRepository picdaterepository;

    @Autowired
    SecurityData securitydate;

    @RequestMapping(value="/pd/index" , method = RequestMethod.GET)
    public ModelAndView pdIndex(ModelAndView mv) {
        mv.setViewName("views/pcdice/index");
        return mv;
    }

    @RequestMapping(value="/pd/new" , method = RequestMethod.GET)
    public ModelAndView pdNew(Pic_DataForm pdform , ModelAndView mv) {

        pdform.setToken(session.getId());
        mv.addObject("pd" , pdform);
        mv.setViewName("views/pcdice/new");
        return mv;
    }


}
