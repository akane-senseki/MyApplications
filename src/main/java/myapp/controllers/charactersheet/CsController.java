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
    public ModelAndView csIndex(@RequestParam(name = "page" , required = false) Integer page ,  ModelAndView mv) {
        if(page == null) {
            page = 1;
        }

        Page<Pc_Entity> pc = pcrepository.findAll(PageRequest.of(20 * (page - 1), 20, Sort.by("id").descending()));
        mv.addObject("pc" , pc);
        mv.addObject("page" , page);
        mv.setViewName("views/charactersheet/index");

        return mv;

    }

    @RequestMapping(value = "/cs/new", method = RequestMethod.GET)
    public ModelAndView csNew(@ModelAttribute Pc_EntityForm peForm, ModelAndView mv) {

        System.out.println("cseditController通過");
        peForm.setToken(session.getId());
        mv.addObject("pc", peForm);
        mv.setViewName("views/charactersheet/new");

        return mv;

    }

    @RequestMapping(value = "/cs/create", method = RequestMethod.POST)
    public ModelAndView csCerate(@ModelAttribute Pc_EntityForm peForm, ModelAndView mv) throws IOException {

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

            User u = (User)session.getAttribute("login_user");




          //ここから画像のパス作成------------------------------------

            //画像保存場所+ログインユーザーIDでフォルダの作成
            File images = new File(securitydate.getImg_path() + u.getId());
            images.mkdirs();
            System.out.println("フォルダは作った");

            String img_name = peForm.getCs_img().getOriginalFilename();
            String extension = img_name.substring(img_name.lastIndexOf("."));//最後の.より右側の文字(拡張子)を取得
            String img_path = (int)(Math.floor(Math.random()*1000000000)) + extension;
            List<Pc_Entity> pc = pcrepository.findByUser(u);
            //画像名が既存のものと被っていた場合変更する(変更後はもう一度確認する)
            for(int i = 0 ; i < pc.size() ; i ++) {
                if(img_path.equals(pc.get(i).getImg_path())) {
                    img_path = "96" + img_path;
                    for(int n = 0 ; n < pc.size() ; n ++) {
                        if(img_path.equals(pc.get(n).getImg_path())) {
                            img_path = "96" + img_path;
                        }
                    }
                }
            }
            p.setImg_path(img_path);
            System.out.println("img_pathの登録完了");

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
            System.out.println("登録出来た！");
            mv = new ModelAndView("redirect:/"); // リダイレクト
            return mv;

        } else {

            mv.setViewName("views/charactersheet/index");
            System.out.println("登録できず");

            return mv;

        }
    }

    @RequestMapping(path = "/cs/show" , method = RequestMethod.GET)
    public ModelAndView csShow(@ModelAttribute Pc_EntityForm peForm , ModelAndView mv) {

        //Optional<> はその値がnullかも知れないことをあらわしている(nullチェックが必要ない)
        Optional<Pc_Entity> p = pcrepository.findById(peForm.getId());

        //ModelMapperでEntity→Formオブジェクトへマッピングする。
        ModelMapper modelMapper = new ModelMapper();
        peForm = modelMapper.map(p.orElse(new Pc_Entity()), Pc_EntityForm.class);

        mv.addObject("pc" , peForm);
        mv.setViewName("views/charactersheet/show");

        return mv;

    }

    @RequestMapping(path = "/cs/edit" , method = RequestMethod.GET)
    public ModelAndView csEdit(@ModelAttribute Pc_EntityForm peForm , ModelAndView mv) {

        Optional<Pc_Entity> p = pcrepository.findById(peForm.getId());
        //ModelMapperでEntity→Formオブジェクトへマッピングする。(変換する)
        ModelMapper modelMapper = new ModelMapper();
        peForm = modelMapper.map(p.orElse(new Pc_Entity()), Pc_EntityForm.class);
        peForm.setToken(session.getId());

        mv.addObject("pc" , peForm );
        mv.setViewName("views/charactersheet/edit");

        return mv;

    }
}
