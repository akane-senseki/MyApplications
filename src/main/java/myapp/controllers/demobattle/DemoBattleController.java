package myapp.controllers.demobattle;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import myapp.models.Pc_Entity;
import myapp.repositories.PcRepository;

@Controller
public class DemoBattleController {

    @Autowired
    HttpSession session;

    @Autowired
    PcRepository pcrepository;

    @RequestMapping(value="/db/index" , method = RequestMethod.GET)
    public ModelAndView dbIndex(ModelAndView mv) {

        mv.setViewName("views/demobattle/index");
        return mv;
    }

    @RequestMapping(value="/db/1vs1" , method = RequestMethod.GET)
    public ModelAndView db1vs1Select(ModelAndView mv) {

        List<Pc_Entity> p =  pcrepository.findByDeleteFlagAndReleaseFlag(0, 0);
        mv.addObject( "pc", p );
        mv.setViewName("views/demobattle/1vs1");
        return mv;
    }

}
