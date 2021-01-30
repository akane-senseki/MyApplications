package myapp.controllers.demobattle;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value="db/log" , method = RequestMethod.POST)
    public ModelAndView dbLog(@RequestParam(name = "pc1") Integer p1 , @RequestParam(name = "pc2") Integer p2 , ModelAndView mv) {
        Optional<Pc_Entity> oppc1 = pcrepository.findById(p1);
        Optional<Pc_Entity> oppc2 = pcrepository.findById(p2);
        Pc_Entity pc1 = oppc1.orElse(null);
        Pc_Entity pc2 = oppc2.orElse(null);

        mv.addObject("pc1" , pc1);
        mv.addObject("pc2" , pc2);

        mv.setViewName("/views/demobattle/log");

        return mv;
    }

}
