package myapp.controllers.charactersheet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
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

import myapp.config.SecurityData;
import myapp.forms.Pc_EntityForm;
import myapp.models.Pc_Entity;
import myapp.models.User;
import myapp.repositories.PcRepository;

@Controller
public class CsController {

    @Autowired
    HttpSession session; // セッション

    @Autowired
    PcRepository pcrepository;

    @Autowired
    SecurityData securitydate;

    @RequestMapping(value = "/cs/user", method = RequestMethod.GET)
    public ModelAndView csUsserIndex(@RequestParam(name = "page", required = false) Integer page, ModelAndView mv) {
        if(session.getAttribute("login_user") != null) {

        if (page == null) {
            page = 1;
        }

        Page<Pc_Entity> pc = pcrepository.findByUserAndDeleteFlag((User)session.getAttribute("login_user") , 0 , (PageRequest.of(20 * (page - 1), 20, Sort.by("id").descending())));

        mv.addObject("pc", pc);
        mv.addObject("page", page);
        mv.setViewName("views/charactersheet/user");

        if(session.getAttribute("flush") != null){
            mv.addObject("flush" , session.getAttribute("flush"));
            session.removeAttribute("flush");
        }

        }else {
            //ログインしていないなら通常のindexへ
            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト
        }
        return mv;

    }


    @RequestMapping(value = "/cs/index", method = RequestMethod.GET)
    public ModelAndView csIndex(@RequestParam(name = "page", required = false) Integer page, ModelAndView mv) {
        if (page == null) {
            page = 1;
        }

        Page<Pc_Entity> pc = pcrepository.findByDeleteFlagAndReleaseFlag( 0 , 0 ,(PageRequest.of(20 * (page - 1), 20, Sort.by("id").descending())));

        mv.addObject("pc", pc);
        mv.addObject("page", page);
        mv.setViewName("views/charactersheet/index");

        if(session.getAttribute("flush") != null){
            mv.addObject("flush" , session.getAttribute("flush"));
            session.removeAttribute("flush");
        }

        return mv;

    }

    @RequestMapping(value = "/cs/new", method = RequestMethod.GET)
    public ModelAndView csNew(@ModelAttribute Pc_EntityForm peForm, ModelAndView mv) {

        peForm.setToken(session.getId());
        mv.addObject("pc", peForm);
        mv.setViewName("views/charactersheet/new");

        return mv;

    }

    @RequestMapping(value = "/cs/create", method = RequestMethod.POST)
    @Transactional // メソッド開始時にトランザクションを開始、終了時にコミットする
    public ModelAndView csCerate(@ModelAttribute Pc_EntityForm peForm, ModelAndView mv) throws IOException {

        if (peForm.getToken() != null && peForm.getToken().equals(session.getId())) {
            Pc_Entity p = new Pc_Entity();

            p.setUser((User) session.getAttribute("login_user"));
            p.setName(peForm.getName());
            p.setNameRuby(peForm.getNameRuby());
            p.setReleaseFlag(peForm.getReleaseFlag());
            p.setDeleteFlag(0);

            Date update_date = new Date(System.currentTimeMillis());
            p.setUpdateDate(update_date);

            p.setStr(peForm.getStr());
            p.setCon(peForm.getCon());
            p.setDex(peForm.getDex());
            p.setSiz(peForm.getSiz());

            p.setAvoidanceAdd(peForm.getAvoidanceAdd());
            p.setKickAdd(peForm.getKickAdd());
            p.setFistAdd(peForm.getFistAdd());
            p.setHeadbuttAdd(peForm.getHeadbuttAdd());

            User u = (User) session.getAttribute("login_user");

            //ここから画像のパス作成------------------------------------

            //画像保存場所+ログインユーザーIDでフォルダの作成
            File images = new File(securitydate.getImg_path() + u.getId());
            images.mkdirs();

            //画像がアップロードされていたら
            if (!peForm.getCsImg().get(0).getOriginalFilename().equals("") && peForm.getCsImg().get(0).getOriginalFilename() != null) {

                MultipartFile imgFile = peForm.getCsImg().get(peForm.getCsImg().size() - 1);

                String imgName = imgFile.getOriginalFilename();
                String extension = imgName.substring(imgName.lastIndexOf("."));//最後の.より右側の文字(拡張子)を取得
                String img_path = (int) (Math.floor(Math.random() * 1000000000)) + extension;
                List<Pc_Entity> pc = pcrepository.findByUserAndDeleteFlag(u ,0);
                //画像名が既存のものと被っていた場合変更する(変更後はもう一度確認する)
                for (int i = 0; i < pc.size(); i++) {
                    if (img_path.equals(pc.get(i).getImgPath())) {
                        img_path = "96" + img_path;
                        for (int n = 0; n < pc.size(); n++) {
                            if (img_path.equals(pc.get(n).getImgPath())) {
                                img_path = "96" + img_path;
                            }
                        }
                    }
                }
                p.setImgPath(img_path);

                //MultipartFile型をInputStream型にキャストしてる(入出力出来るように)
                byte[] byteArr = imgFile.getBytes();
                InputStream image = new ByteArrayInputStream(byteArr);
                BufferedInputStream reader = new BufferedInputStream(image);

                try (
                        FileOutputStream img = new FileOutputStream(
                                securitydate.getImg_path() + u.getId() + "/" + img_path);
                        BufferedOutputStream writer = new BufferedOutputStream(img);) {
                    int data;
                    while ((data = reader.read()) != -1) {
                        writer.write(data);
                    }

                } catch (FileNotFoundException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                } catch (IOException e1) {
                    // TODO 自動生成された catch ブロック
                    e1.printStackTrace();
                }
            } else {
                p.setImgPath(null);
            }

            peForm.setToken(session.getId());

            mv.addObject("pc", peForm);
            pcrepository.save(p);
            session.setAttribute("flush", "登録しました");
            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト
            return mv;

        } else {

            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト
            session.setAttribute("flush", "登録に失敗しました");

            return mv;

        }
    }

    @RequestMapping(path = "/cs/show", method = RequestMethod.GET)
    public ModelAndView csShow(@ModelAttribute Pc_EntityForm peForm, ModelAndView mv) {

        //Optional<> はその値がnullかも知れないことをあらわしている(nullチェックが必要ない)
        Optional<Pc_Entity> p = pcrepository.findById(peForm.getId());

        //ModelMapperでEntity→Formオブジェクトへマッピングする。
        ModelMapper modelMapper = new ModelMapper();
        peForm = modelMapper.map(p.orElse(new Pc_Entity()), Pc_EntityForm.class);

        mv.addObject("pc", peForm);
        mv.setViewName("views/charactersheet/show");

        return mv;

    }

    @RequestMapping(path = "/cs/edit", method = RequestMethod.GET)
    public ModelAndView csEdit(@ModelAttribute Pc_EntityForm peForm, ModelAndView mv) {

        Optional<Pc_Entity> p = pcrepository.findById(peForm.getId());

        //ModelMapperでEntity→Formオブジェクトへマッピングする。(変換する)
        ModelMapper modelMapper = new ModelMapper();
        peForm = modelMapper.map(p.orElse(new Pc_Entity()), Pc_EntityForm.class);
        peForm.setToken(session.getId());

        mv.addObject("pc", peForm);
        session.setAttribute("pc_id", peForm.getId());
        mv.setViewName("views/charactersheet/edit");
        return mv;

    }

    @RequestMapping(path = "/cs/update", method = RequestMethod.POST)
    @Transactional
    public ModelAndView csUpdate(@ModelAttribute Pc_EntityForm peForm, ModelAndView mv) throws IOException {
        if (peForm.getToken() != null && peForm.getToken().equals(session.getId())) {
            Optional<Pc_Entity> opp = pcrepository.findById((Integer) session.getAttribute("pc_id"));
            Pc_Entity p = opp.orElse(null);
            //orElse……存在しない場合はother(この場合はnull)を返却する。

            p.setName(peForm.getName());
            p.setNameRuby(peForm.getNameRuby());
            p.setReleaseFlag(peForm.getReleaseFlag());

            Date update_date = new Date(System.currentTimeMillis());
            p.setUpdateDate(update_date);

            p.setStr(peForm.getStr());
            p.setCon(peForm.getCon());
            p.setDex(peForm.getDex());
            p.setSiz(peForm.getSiz());

            p.setAvoidanceAdd(peForm.getAvoidanceAdd());
            p.setKickAdd(peForm.getKickAdd());
            p.setFistAdd(peForm.getFistAdd());
            p.setHeadbuttAdd(peForm.getHeadbuttAdd());

            User u = (User) session.getAttribute("login_user");

            //ここから画像のパス作成------------------------------------

            //画像がアップロードされていたら更新。無かったら何もしない。
            if (!peForm.getCsImg().get(0).getOriginalFilename().equals("") && peForm.getCsImg().get(0).getOriginalFilename() != null) {

                //登録されていた画像の削除
                File delete_images = new File(securitydate.getImg_path() + u.getId() + "/" + p.getImgPath());
                delete_images.delete();

                //ここからcreate時と同様の処理
                MultipartFile img_file = peForm.getCsImg().get(peForm.getCsImg().size() - 1);

                String img_name = img_file.getOriginalFilename();
                String extension = img_name.substring(img_name.lastIndexOf("."));//最後の.より右側の文字(拡張子)を取得
                String img_path = (int) (Math.floor(Math.random() * 1000000000)) + extension;
                List<Pc_Entity> pc = pcrepository.findByUserAndDeleteFlag(u , 0);
                //画像名が既存のものと被っていた場合変更する(変更後はもう一度確認する)
                for (int i = 0; i < pc.size(); i++) {
                    if (img_path.equals(pc.get(i).getImgPath())) {
                        img_path = "96" + img_path;
                        for (int n = 0; n < pc.size(); n++) {
                            if (img_path.equals(pc.get(n).getImgPath())) {
                                img_path = "96" + img_path;
                            }
                        }
                    }
                }
                p.setImgPath(img_path);

                //MultipartFile型をInputStream型にキャストしてる(入出力出来るように)
                byte[] byteArr = img_file.getBytes();
                InputStream image = new ByteArrayInputStream(byteArr);
                BufferedInputStream reader = new BufferedInputStream(image);

                try (
                        FileOutputStream img = new FileOutputStream(
                                securitydate.getImg_path() + u.getId() + "/" + img_path);
                        BufferedOutputStream writer = new BufferedOutputStream(img);) {
                    int data;
                    while ((data = reader.read()) != -1) {
                        writer.write(data);
                    }

                } catch (FileNotFoundException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                } catch (IOException e1) {
                    // TODO 自動生成された catch ブロック
                    e1.printStackTrace();
                }
            }

            peForm.setToken(session.getId());

            mv.addObject("pc", peForm);
            pcrepository.save(p);
            session.setAttribute("flush", "更新しました");
            session.removeAttribute("pc_id");
            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト
            return mv;

        } else {
            session.setAttribute("flush", "更新に失敗しました");
            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト
            return mv;
        }
    }

    @RequestMapping(path = "/cs/destroy" , method = RequestMethod.POST)
    @Transactional
    public ModelAndView csDestroy(@ModelAttribute Pc_EntityForm peForm , ModelAndView mv) {

        if(peForm.getToken() != null && peForm.getToken().equals(session.getId())) {

            Optional<Pc_Entity> opp = pcrepository.findById((Integer) session.getAttribute("pc_id"));
            Pc_Entity p = opp.orElse(null);

            p.setDeleteFlag(1);

            //保存している画像の削除
            User u = (User) session.getAttribute("login_user");
            File delete_images = new File(securitydate.getImg_path() + u.getId() + "/" + p.getImgPath());
            delete_images.delete();

            pcrepository.save(p);
            session.removeAttribute("pc_id");
            session.setAttribute("flush", "削除しました");
            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト

            return mv;
        }else {
            session.setAttribute("flush", "削除に失敗しました");
            mv = new ModelAndView("redirect:/cs/index"); // リダイレクト
            return mv;
        }
    }
}
