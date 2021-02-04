package myapp.controllers.PcDice;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import myapp.config.SecurityData;
import myapp.forms.Pic_DataForm;
import myapp.models.Pic_Data;
import myapp.models.User;
import myapp.repositories.PicDateRepository;

@Controller
public class PcDiceController {
    @Autowired
    HttpSession session;

    @Autowired
    PicDateRepository pdrepository;

    @Autowired
    SecurityData securitydate;

    @RequestMapping(value = "/pd/index", method = RequestMethod.GET)
    public ModelAndView pdIndex(ModelAndView mv) {

        List<Pic_Data> pic = pdrepository.findByDeleteFlagAndReleaseFlag(0, 0);

        mv.addObject("pic", pic);
        if (session.getAttribute("flush") != null) {
            mv.addObject("flush", session.getAttribute("flush"));
            session.removeAttribute("flush");
        }

        mv.setViewName("views/pcdice/index");
        return mv;
    }

    @RequestMapping(value = "/pd/new", method = RequestMethod.GET)
    public ModelAndView pdNew(Pic_DataForm pdform, ModelAndView mv) {

        pdform.setToken(session.getId());
        mv.addObject("pd", pdform);
        mv.setViewName("views/pcdice/new");
        return mv;
    }

    @RequestMapping(value = "/pd/create", method = RequestMethod.POST)
    @Transactional
    public ModelAndView pdCreate(@ModelAttribute Pic_DataForm pdForm, ModelAndView mv) throws IOException {
        if (pdForm.getToken() != null && pdForm.getToken().equals(session.getId())) {
            Pic_Data p = new Pic_Data();
            String img_path = null;
            MultipartFile imgFile = null;

            p.setUser((User) session.getAttribute("login_user"));
            p.setName(pdForm.getName());
            p.setReleaseFlag(pdForm.getReleaseFlag());
            p.setDeleteFlag(0);
            p.setyAxis(0);
            p.setxAxis(0);
            p.setColor("#666666");

            User u = (User) session.getAttribute("login_user");

            //フォルダ作成
            Long count = pdrepository.count() + 1;
            File images = new File(securitydate.getPicPath() + u.getId() + "/" + count);
            images.mkdirs();


            //画像パス　defaImg---------------------------
            String defaultPath = null;
            if (!pdForm.getDefa().get(0).getOriginalFilename().equals("")
                    && pdForm.getDefa().get(0).getOriginalFilename() != null) {
                imgFile = pdForm.getDefa().get(pdForm.getDefa().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setDefaImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate);
                defaultPath = img_path;

            } else {
                //デフォルトはnullable=falseなのでエラーを起こす。
            }

            //画像パス　LoadImg---------------------------
            if (!pdForm.getLoad().get(0).getOriginalFilename().equals("")
                    && pdForm.getLoad().get(0).getOriginalFilename() != null) {
                imgFile = pdForm.getLoad().get(pdForm.getLoad().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setLoadImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate);

            } else {
                p.setLoadImg(defaultPath);
            }

            //画像パス　CriticalImg---------------------------
            if (!pdForm.getCritical().get(0).getOriginalFilename().equals("")
                    && pdForm.getCritical().get(0).getOriginalFilename() != null) {
                imgFile = pdForm.getCritical().get(pdForm.getCritical().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setCriticalImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate);

            } else {
                p.setCriticalImg(defaultPath);
            }

            //画像パス　FumbleImg---------------------------
            if (!pdForm.getFumble().get(0).getOriginalFilename().equals("")
                    && pdForm.getFumble().get(0).getOriginalFilename() != null) {
                imgFile = pdForm.getFumble().get(pdForm.getFumble().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setFumbleImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate);

            } else {
                p.setFumbleImg(defaultPath);
            }

            //画像パス　HoverImg---------------------------
            if (!pdForm.getHover().get(0).getOriginalFilename().equals("")
                    && pdForm.getHover().get(0).getOriginalFilename() != null) {
                imgFile = pdForm.getHover().get(pdForm.getHover().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setHoverImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate);

            } else {
                p.setHoverImg(defaultPath);
            }

            //画像パス　ActiveImg---------------------------
            if (!pdForm.getActive().get(0).getOriginalFilename().equals("")
                    && pdForm.getActive().get(0).getOriginalFilename() != null) {
                imgFile = pdForm.getActive().get(pdForm.getActive().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setActiveImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate);

            } else {
                p.setActiveImg(defaultPath);
            }

            pdForm.setToken(session.getId());
            mv.addObject("pic", pdForm);
            pdrepository.save(p);
            session.setAttribute("flush", "画像を登録しました");
            mv = new ModelAndView("redirect:/pd/index"); // リダイレクト
            return mv;
        }

        mv = new ModelAndView("redirect:/"); // リダイレクト
        session.setAttribute("flush", "画像の登録に失敗しました");
        return mv;
    }

    @RequestMapping(path = "/pd/play", method = RequestMethod.GET)
    public ModelAndView pdPlay(@ModelAttribute Pic_DataForm pdForm, ModelAndView mv) {

        Optional<Pic_Data> pic = pdrepository.findById(pdForm.getId());
        ModelMapper modelMapper = new ModelMapper();
        pdForm = modelMapper.map(pic.orElse(new Pic_Data()), Pic_DataForm.class);
        pdForm.setToken(session.getId());

        mv.addObject("pic", pdForm);
        session.setAttribute("pic_id", pdForm.getId());
        mv.setViewName("views/pcdice/play");
        return mv;
    }

    @RequestMapping(value = "/pd/adjustment", method = RequestMethod.POST)
    @Transactional
    public ModelAndView pdAdjustment(@ModelAttribute Pic_DataForm pdForm, ModelAndView mv) {
        if (pdForm.getToken() != null && pdForm.getToken().equals(session.getId())) {
            Optional<Pic_Data> oppic = pdrepository.findById((Integer) session.getAttribute("pic_id"));
            Pic_Data p = oppic.orElse(null);
            p.setyAxis(pdForm.getyAxis());
            p.setxAxis(pdForm.getxAxis());
            p.setColor(pdForm.getColor());

            pdrepository.save(p);
            session.setAttribute("flush", "保存しました");
            mv = new ModelAndView("redirect:/pd/index"); // リダイレクト
            return mv;
        }

        session.setAttribute("flush", "保存に失敗しました");
        mv = new ModelAndView("redirect:/pd/index"); // リダイレクト
        return mv;

    }

}
