package myapp.controllers.likes;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import myapp.forms.Pc_EntityForm;
import myapp.forms.Pc_Entity_LikeForm;
import myapp.models.PcEntity;
import myapp.models.PcEntityLike;
import myapp.models.User;
import myapp.repositories.PcLikeRepository;
import myapp.repositories.PcRepository;
import myapp.repositories.PicDataLikeRepository;

@Controller
public class LikesControllers {
    @Autowired
    HttpSession session; // セッション

    @Autowired
    PicDataLikeRepository picrepository;

    @Autowired
    PcLikeRepository pcLikerepository;

    @Autowired
    PcRepository pcrepository;

    @RequestMapping(value = "/pcLike", method = RequestMethod.POST)
    @Transactional
    public ModelAndView pcLike(@ModelAttribute Pc_Entity_LikeForm peForm, ModelAndView mv){

    //元の画面への推移
    Optional<PcEntity>  oppc = pcrepository.findById(peForm.getId());
    ModelMapper modelMapper = new ModelMapper();
    Pc_EntityForm pcEntityForm = modelMapper.map(oppc.orElse(new PcEntity()), Pc_EntityForm.class);

    mv.addObject("pc", pcEntityForm);
    mv.setViewName("views/charactersheet/show");

    //お気に入りに追加or削除
    PcEntity pc = oppc.get();
    PcEntityLike p = pcLikerepository.findByUserAndPcEntity((User)session.getAttribute("login_user"), pc);
    if(p == null) {
        PcEntityLike like = new PcEntityLike();
        like.setPc_entity(pc);
        like.setUser((User)session.getAttribute("login_user"));
        pcLikerepository.save(like);
        mv.addObject("like",like);
    }else {
        pcLikerepository.delete(p);
    }
        return mv;
    }
}
