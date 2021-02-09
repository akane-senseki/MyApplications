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

import myapp.forms.PcEntityForm;
import myapp.forms.PcEntityLikeForm;
import myapp.forms.PicDataForm;
import myapp.forms.PicDataLikeForm;
import myapp.models.PcEntity;
import myapp.models.PcEntityLike;
import myapp.models.PicData;
import myapp.models.PicDataLike;
import myapp.models.User;
import myapp.repositories.PcLikeRepository;
import myapp.repositories.PcRepository;
import myapp.repositories.PicDataLikeRepository;
import myapp.repositories.PicDateRepository;

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

    @Autowired
    PicDateRepository pdrepository;

    @RequestMapping(value = "/pcLike", method = RequestMethod.POST)
    @Transactional
    public ModelAndView pcLike(@ModelAttribute PcEntityLikeForm peForm, ModelAndView mv){

    //元の画面への推移
    Optional<PcEntity>  oppc = pcrepository.findById(peForm.getId());
    ModelMapper modelMapper = new ModelMapper();
    PcEntityForm pcEntityForm = modelMapper.map(oppc.orElse(new PcEntity()), PcEntityForm.class);
    pcEntityForm.setToken(session.getId());
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

    @RequestMapping(value = "/picLike", method = RequestMethod.POST)
    @Transactional
    public ModelAndView picLike(@ModelAttribute PicDataLikeForm picForm, ModelAndView mv){
      //元の画面への推移
        Optional<PicData> oppic = pdrepository.findById(picForm.getId());
        ModelMapper modelMapper = new ModelMapper();
        PicDataForm pdForm = modelMapper.map(oppic.orElse(new PicData()), PicDataForm.class);
        pdForm.setToken(session.getId());
        mv.addObject("pic", pdForm);
        session.setAttribute("picId", pdForm.getId());
        mv.setViewName("views/pcdice/play");

      //お気に入りに追加or削除
        PicData pic = oppic.get();
        PicDataLike p = picrepository.findByUserAndPicData((User)session.getAttribute("login_user"), pic);
        if(p == null) {
            PicDataLike like = new PicDataLike();
            like.setpicData(pic);
            like.setUser((User)session.getAttribute("login_user"));
            picrepository.save(like);
            mv.addObject("like",like);
        }else {
            picrepository.delete(p);
        }

        return mv;


    }



}
