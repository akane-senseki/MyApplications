package myapp.controllers.likes;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import myapp.forms.Pc_Entity_LikeForm;
import myapp.repositories.PcLikeRepository;
import myapp.repositories.PicDataLikeRepository;

@Controller
public class LikesControllers {
    @Autowired
    HttpSession session; // セッション

    @Autowired
    PicDataLikeRepository picrepository;

    @Autowired
    PcLikeRepository pcrepository;

    @RequestMapping(value = "/pcLike", method = RequestMethod.POST)
    @Transactional // メソッド開始時にトランザクションを開始、終了時にコミットする
    public ModelAndView pcLike(@ModelAttribute Pc_Entity_LikeForm peForm, ModelAndView mv){
    //Optional<Pc_Entity_Like> p = pcrepository.findById(peForm.getId());
    System.out.println(peForm.getUser().getName());
    System.out.println(peForm.getPc_entity().getId());

        mv.setViewName("views/charactersheet/edit(id=" + peForm.getPc_entity().getId() + ")");
        return mv;
    }
}
