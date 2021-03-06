package myapp.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import myapp.config.SecurityData;
import myapp.forms.PicDataForm;
import myapp.models.PicData;
import myapp.models.PicDataLike;
import myapp.models.User;
import myapp.repositories.PicDataLikeRepository;
import myapp.repositories.PicDateRepository;

@Controller
public class PcDiceController {
    @Autowired
    HttpSession session;

    @Autowired
    PicDateRepository pdrepository;

    @Autowired
    SecurityData securitydate;

    @Autowired
    PicDataLikeRepository picrepository;

    @RequestMapping(value = "/pd/user", method = RequestMethod.GET)
    public ModelAndView pdUserIndex(@RequestParam(name = "page", required = false) Integer page, ModelAndView mv) {
        if (page == null) {
            page = 1;
        }

        if (session.getAttribute("login_user") != null) {
            Page<PicData> pic = pdrepository.findByUserAndDeleteFlag((User) session.getAttribute("login_user"), 0,
                    PageRequest.of(page - 1, 10, Sort.by("id").descending()));
            long count = pdrepository.countByUserAndDeleteFlag((User) session.getAttribute("login_user"), 0);

            mv.addObject("pic", pic);
            mv.addObject("page", page);
            mv.addObject("count", count);
            mv.setViewName("views/pcdice/user");
        }
        return mv;
    }

    @RequestMapping(value = "/pd/like", method = RequestMethod.GET)
    public ModelAndView pdUserLikeIndex(ModelAndView mv) {
        if (session.getAttribute("login_user") != null) {
            List<PicDataLike> likes = picrepository.findByUser((User) session.getAttribute("login_user"));
            List<PicData> pic = new ArrayList<PicData>();
            for (int i = 0; i < likes.size(); i++) {
                if (likes.get(i).getpicData().getDeleteFlag() == 0) {
                    pic.add(likes.get(i).getpicData());
                }
            }
            mv.addObject("pic", pic);
            mv.setViewName("views/pcdice/like");
        }
        return mv;
    }

    @RequestMapping(value = "/pd/index", method = RequestMethod.GET)
    public ModelAndView pdIndex(@RequestParam(name = "page", required = false) Integer page, ModelAndView mv) {
        if (page == null) {
            page = 1;
        }

        Page<PicData> pic = pdrepository.findByDeleteFlagAndReleaseFlag(0, 0,
                PageRequest.of(page - 1, 10, Sort.by("id").descending()));
        long count = pdrepository.countByDeleteFlagAndReleaseFlag(0, 0);

        mv.addObject("pic", pic);
        mv.addObject("page", page);
        mv.addObject("count", count);
        if (session.getAttribute("flush") != null) {
            mv.addObject("flush", session.getAttribute("flush"));
            session.removeAttribute("flush");
        }
        if (session.getAttribute("error") != null) {
            mv.addObject("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }

        mv.setViewName("views/pcdice/index");
        return mv;
    }

    @RequestMapping(value = "/pd/new", method = RequestMethod.GET)
    public ModelAndView pdNew(PicDataForm pdform, ModelAndView mv) {

        pdform.setToken(session.getId());
        mv.addObject("pd", pdform);
        mv.setViewName("views/pcdice/new");
        return mv;
    }

    @RequestMapping(value = "/pd/create", method = RequestMethod.POST)
    @Transactional
    public ModelAndView pdCreate(@ModelAttribute PicDataForm pdForm, ModelAndView mv) throws IOException {
        if (pdForm.getToken() != null && pdForm.getToken().equals(session.getId())) {
            PicData p = new PicData();
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

            //画像パス　defaImg---------------------------
            String defaultPath = null;
            if (!pdForm.getDefa().get(0).getOriginalFilename().equals("")
                    && pdForm.getDefa().get(0).getOriginalFilename() != null) {

                imgFile = pdForm.getDefa().get(pdForm.getDefa().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setDefaImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate, "");
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
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate, "");

            } else {
                p.setLoadImg(defaultPath);
            }

            //画像パス　CriticalImg---------------------------
            if (!pdForm.getCritical().get(0).getOriginalFilename().equals("")
                    && pdForm.getCritical().get(0).getOriginalFilename() != null) {
                imgFile = pdForm.getCritical().get(pdForm.getCritical().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setCriticalImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate, "");

            } else {
                p.setCriticalImg(defaultPath);
            }

            //画像パス　FumbleImg---------------------------
            if (!pdForm.getFumble().get(0).getOriginalFilename().equals("")
                    && pdForm.getFumble().get(0).getOriginalFilename() != null) {
                imgFile = pdForm.getFumble().get(pdForm.getFumble().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setFumbleImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate, "");

            } else {
                p.setFumbleImg(defaultPath);
            }

            //画像パス　HoverImg---------------------------
            if (!pdForm.getHover().get(0).getOriginalFilename().equals("")
                    && pdForm.getHover().get(0).getOriginalFilename() != null) {
                imgFile = pdForm.getHover().get(pdForm.getHover().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setHoverImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate, "");

            } else {
                p.setHoverImg(defaultPath);
            }

            //画像パス　ActiveImg---------------------------
            if (!pdForm.getActive().get(0).getOriginalFilename().equals("")
                    && pdForm.getActive().get(0).getOriginalFilename() != null) {
                imgFile = pdForm.getActive().get(pdForm.getActive().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setActiveImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate, "");

            } else {
                p.setActiveImg(defaultPath);
            }

            pdForm.setToken(session.getId());
            mv.addObject("pic", pdForm);
            pdrepository.save(p);
            session.setAttribute("flush", "画像を登録しました");
            mv = new ModelAndView("redirect:/pd/index"); // リダイレクト
            return mv;
        } else {

            mv = new ModelAndView("redirect:/"); // リダイレクト
            session.setAttribute("error", "画像の登録に失敗しました");
            return mv;
        }
    }

    @RequestMapping(path = "/pd/play", method = RequestMethod.GET)
    public ModelAndView pdPlay(@ModelAttribute PicDataForm pdForm, ModelAndView mv) {

        Optional<PicData> oppic = pdrepository.findById(pdForm.getId());
        ModelMapper modelMapper = new ModelMapper();
        pdForm = modelMapper.map(oppic.orElse(new PicData()), PicDataForm.class);
        pdForm.setToken(session.getId());

        mv.addObject("pic", pdForm);
        session.setAttribute("picId", pdForm.getId());
        mv.setViewName("views/pcdice/play");

        //お気に入りの確認
        PicData pic = oppic.get();
        if (session.getAttribute("login_user") != null) {
            PicDataLike p = picrepository.findByUserAndPicData((User) session.getAttribute("login_user"), pic);
            if (p != null) {
                mv.addObject("like", p);
            }
        }

        return mv;
    }

    @RequestMapping(value = "/pd/adjustment", method = RequestMethod.POST)
    @Transactional
    public ModelAndView pdAdjustment(@ModelAttribute PicDataForm pdForm, ModelAndView mv) {
        if (pdForm.getToken() != null && pdForm.getToken().equals(session.getId())) {
            Optional<PicData> oppic = pdrepository.findById((Integer) session.getAttribute("picId"));
            PicData p = oppic.orElse(null);
            p.setyAxis(pdForm.getyAxis());
            p.setxAxis(pdForm.getxAxis());
            p.setColor(pdForm.getColor());

            pdrepository.save(p);
            session.setAttribute("flush", "保存しました");
            mv = new ModelAndView("redirect:/pd/index"); // リダイレクト
            return mv;
        }

        session.setAttribute("error", "保存に失敗しました");
        mv = new ModelAndView("redirect:/pd/index"); // リダイレクト
        return mv;

    }

    @RequestMapping(value = "/pd/edit", method = RequestMethod.GET)
    public ModelAndView pdEdit(PicDataForm pdForm, ModelAndView mv) {

        Optional<PicData> pic = pdrepository.findById(pdForm.getId());
        ModelMapper modelMapper = new ModelMapper();
        pdForm = modelMapper.map(pic.orElse(new PicData()), PicDataForm.class);
        pdForm.setToken(session.getId());

        mv.addObject("pd", pdForm);
        mv.setViewName("views/pcdice/edit");
        session.setAttribute("picId", pdForm.getId());
        return mv;
    }

    @RequestMapping(path = "/pd/update", method = RequestMethod.POST)
    @Transactional
    public ModelAndView pdUpdate(@ModelAttribute PicDataForm pdForm, ModelAndView mv) throws IOException {
        if (pdForm.getToken() != null && pdForm.getToken().equals(session.getId())) {
            Optional<PicData> oppic = pdrepository.findById((Integer) session.getAttribute("picId"));
            PicData p = oppic.orElse(null);
            String img_path = null;
            MultipartFile imgFile = null;

            //デフォルト画像変更後、デフォルト画像を参考している画像も変更
            String beforeDefaPath = "";
            String afterDefaPath = "";

            if (pdForm.getName() != null && !pdForm.getName().equals("")) {
                p.setName(pdForm.getName());
            }
            p.setReleaseFlag(pdForm.getReleaseFlag());

            User u = (User) session.getAttribute("login_user");

            //フォルダをidで選択(IntegerをLongに変換)
            Long count = p.getId().longValue();

            //画像パス　defaImg---------------------------
            if (!pdForm.getDefa().get(0).getOriginalFilename().equals("")
                    && pdForm.getDefa().get(0).getOriginalFilename() != null) {

                String deletePath = "pd/" + u.getId() + "/" + p.getId() + "/" + p.getDefaImg();
                beforeDefaPath = p.getDefaImg();

                imgFile = pdForm.getDefa().get(pdForm.getDefa().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setDefaImg(img_path);
                afterDefaPath = img_path;
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate, deletePath);

            }

            //画像パス　LoadImg---------------------------
            if (!pdForm.getLoad().get(0).getOriginalFilename().equals("")
                    && pdForm.getLoad().get(0).getOriginalFilename() != null) {

                String deletePath = "pd/" + u.getId() + "/" + p.getId() + "/" + p.getLoadImg();

                imgFile = pdForm.getLoad().get(pdForm.getLoad().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setLoadImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate, deletePath);

            } else if (!beforeDefaPath.equals("") && p.getLoadImg().equals(beforeDefaPath)) {
                p.setLoadImg(afterDefaPath);
            }

            //画像パス　CriticalImg---------------------------
            if (!pdForm.getCritical().get(0).getOriginalFilename().equals("")
                    && pdForm.getCritical().get(0).getOriginalFilename() != null) {

                String deletePath = "pd/" + u.getId() + "/" + p.getId() + "/" + p.getCriticalImg();

                imgFile = pdForm.getCritical().get(pdForm.getCritical().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setCriticalImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate, deletePath);

            } else if (!beforeDefaPath.equals("") && p.getCriticalImg().equals(beforeDefaPath)) {
                p.setCriticalImg(afterDefaPath);
            }

            //画像パス　FumbleImg---------------------------
            if (!pdForm.getFumble().get(0).getOriginalFilename().equals("")
                    && pdForm.getFumble().get(0).getOriginalFilename() != null) {

                String deletePath = "pd/" + u.getId() + "/" + p.getId() + "/" + p.getFumbleImg();

                imgFile = pdForm.getFumble().get(pdForm.getFumble().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setFumbleImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate, deletePath);

            } else if (!beforeDefaPath.equals("") && p.getFumbleImg().equals(beforeDefaPath)) {
                p.setFumbleImg(afterDefaPath);
            }

            //画像パス　HoverImg---------------------------
            if (!pdForm.getHover().get(0).getOriginalFilename().equals("")
                    && pdForm.getHover().get(0).getOriginalFilename() != null) {

                String deletePath = "pd/" + u.getId() + "/" + p.getId() + "/" + p.getHoverImg();

                imgFile = pdForm.getHover().get(pdForm.getHover().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setHoverImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate, deletePath);

            } else if (!beforeDefaPath.equals("") && p.getHoverImg().equals(beforeDefaPath)) {
                p.setHoverImg(afterDefaPath);
            }

            //画像パス　ActiveImg---------------------------
            if (!pdForm.getActive().get(0).getOriginalFilename().equals("")
                    && pdForm.getActive().get(0).getOriginalFilename() != null) {

                String deletePath = "pd/" + u.getId() + "/" + p.getId() + "/" + p.getActiveImg();

                imgFile = pdForm.getActive().get(pdForm.getActive().size() - 1);
                img_path = PicSaveClass.PicSavePath(u, img_path, imgFile, pdrepository);
                p.setActiveImg(img_path);
                PicSaveClass.PicSaveFile(u, imgFile, img_path, count, securitydate, deletePath);

            } else if (!beforeDefaPath.equals("") && p.getActiveImg().equals(beforeDefaPath)) {
                p.setActiveImg(afterDefaPath);
            }

            pdForm.setToken(session.getId());
            mv.addObject("pic", pdForm);
            pdrepository.save(p);
            session.setAttribute("flush", "画像を変更しました");
            mv = new ModelAndView("redirect:/pd/index"); // リダイレクト
            return mv;
        } else {

            mv = new ModelAndView("redirect:/pd/index"); // リダイレクト
            session.setAttribute("error", "画像の登録に失敗しました");
            return mv;

        }
    }

    @RequestMapping(path = "/pd/destroy", method = RequestMethod.POST)
    @Transactional
    public ModelAndView pdDestroy(@ModelAttribute PicDataForm pdForm, ModelAndView mv) {
        if (pdForm.getToken() != null && pdForm.getToken().equals(session.getId())) {
            Optional<PicData> oppic = pdrepository.findById((Integer) session.getAttribute("picId"));
            PicData p = oppic.orElse(null);

            p.setDeleteFlag(1);

            //保存している画像の削除
            //User u = (User) session.getAttribute("login_user");
            /*File deleteFIle = new File(securitydate.getPicPath() + u.getId() + "/" + p.getId());
            File[] deleteImages = deleteFIle.listFiles();
            if (deleteImages != null) {
                for (int i = 0; i < deleteImages.length; i++) {
                    deleteImages[i].delete();
                }
            }*/
            // 設定情報
            BasicAWSCredentials credentials;
            credentials = new BasicAWSCredentials(securitydate.getAcKey(), securitydate.getScKey());

            // AWSのクライアント取得
            AmazonS3 s3 = new AmazonS3Client(credentials);
            User u = (User) session.getAttribute("login_user");

            String deletePathDefa = "pd/" + u.getId() + "/" + p.getId() + "/" + p.getDefaImg();
            String deletePathCli = "pd/" + u.getId() + "/" + p.getId() + "/" + p.getCriticalImg();
            String deletePathFun = "pd/" + u.getId() + "/" + p.getId() + "/" + p.getFumbleImg();
            String deletePathLoad = "pd/" + u.getId() + "/" + p.getId() + "/" + p.getLoadImg();
            String deletePathAct = "pd/" + u.getId() + "/" + p.getId() + "/" + p.getActiveImg();
            String deletePathHov = "pd/" + u.getId() + "/" + p.getId() + "/" + p.getHoverImg();
            s3.deleteObject("picdemo", deletePathDefa);
            s3.deleteObject("picdemo", deletePathCli);
            s3.deleteObject("picdemo", deletePathFun);
            s3.deleteObject("picdemo", deletePathLoad);
            s3.deleteObject("picdemo", deletePathAct);
            s3.deleteObject("picdemo", deletePathHov);

            pdrepository.save(p);
            session.removeAttribute("picId");
            session.setAttribute("flush", "削除しました");
            mv = new ModelAndView("redirect:/pd/index"); // リダイレクト
        } else {
            session.setAttribute("error", "削除に失敗しました");
            mv = new ModelAndView("redirect:/pd/index"); // リダイレクト
        }

        return mv;

    }
}
