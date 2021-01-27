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

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "/cs/index", method = RequestMethod.GET)
    public ModelAndView index(@RequestParam(name = "page" , required = false) Integer page ,  ModelAndView mv) {
        System.out.println("csindexController通過");
        if(page == null) {
            page = 1;
        }

        Page<Pc_Entity> pc = pcrepository.findAll(PageRequest.of(20 * (page - 1), 20, Sort.by("id").descending()));
        System.out.println("全件検索");
        mv.addObject("pc" , pc);
        mv.addObject("page" , page);
        System.out.println("pcデータを詰めた");
        mv.setViewName("views/charactersheet/index");

        return mv;

    }

    @RequestMapping(value = "/cs/new", method = RequestMethod.GET)
    public ModelAndView edit(@ModelAttribute Pc_EntityForm peForm, ModelAndView mv) {

        System.out.println("cseditController通過");
        peForm.setToken(session.getId());
        mv.addObject("pc", peForm);
        mv.setViewName("views/charactersheet/new");

        return mv;

    }

    @RequestMapping(value = "/cs/upload", method = RequestMethod.POST)
    public ModelAndView upload(@ModelAttribute Pc_EntityForm peForm, ModelAndView mv) throws IOException {
        System.out.println("csuploadController通過");

        if (peForm.getToken() != null && peForm.getToken().equals(session.getId())) {
            Pc_Entity p = new Pc_Entity();

            p.setUser((User) session.getAttribute("login_user"));
            p.setName(peForm.getName());
            p.setName_ruby(peForm.getName_ruby());
            p.setRelease_flag(peForm.getRelease_flag());

            Date update_date = new Date(System.currentTimeMillis());
            p.setUpdate_at(update_date);

            p.setStr(peForm.getStr());
            p.setCon(peForm.getCon());
            p.setDex(peForm.getDex());
            p.setSiz(peForm.getSiz());

            p.setAvoidance_add(peForm.getAvoidance_add());
            p.setKick_add(peForm.getKick_add());
            p.setFist_add(peForm.getFist_add());
            p.setHeadbutt_add(peForm.getHeadbutt_add());
            System.out.println("登録出来た！");

            User u = (User)session.getAttribute("login_user");



          //ここから画像のパス作成------------------------------------

            //画像保存場所+ログインユーザーIDでフォルダの作成
            File images = new File(securitydate.getImg_path() + u.getId());
            images.mkdirs();

            String img_name = peForm.getCs_img().getOriginalFilename();
            String extension = img_name.substring(img_name.lastIndexOf("."));//最後の.より右側の文字(拡張子)を取得
            String img_path = (int)(Math.floor(Math.random()*1000000000)) + "." + extension;
            List<Pc_Entity> pc = pcrepository.findByUser_id(u);
            //画像名が既存のものと被っていた場合変更する
            for(int i = 0 ; i < pc.size() ; i ++) {
                if(img_path.equals(pc.get(i).getImg_path())) {
                    img_path = "9966" + img_path;
                }
            }

            p.setImg_path(img_path);

            //MultipartFile型をInputStream型にキャストしてる(入出力出来るように)
            byte[] byteArr = peForm.getCs_img().getBytes();
            InputStream image = new ByteArrayInputStream(byteArr);
            BufferedInputStream reader = new BufferedInputStream(image);

            try (
                    FileOutputStream img = new FileOutputStream(securitydate.getImg_path() + u.getId() + "/" + img_path);
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


            peForm.setToken(session.getId());

            mv.addObject("pc", peForm);
            pcrepository.save(p);
            mv = new ModelAndView("redirect:/"); // リダイレクト
            return mv;

        } else {

            mv.setViewName("views/charactersheet/index");
            System.out.println("登録できず");

            return mv;

        }
    }
}
