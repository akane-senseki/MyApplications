package myapp.controllers.charactersheet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import myapp.forms.Pc_EntityForm;
import myapp.models.Pc_Entity;
import myapp.models.User;

@Controller
public class CsController {

    @Autowired
    HttpSession session; // セッション

    @RequestMapping(value = "/cs/index", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView mv) {

        System.out.println("csindexController通過");

            mv.setViewName("views/charactersheet/index");

            return mv;

    }

    @RequestMapping(value = "/cs/upload", method = RequestMethod.POST)
    public ModelAndView upload(@ModelAttribute Pc_EntityForm peForm , ModelAndView mv) throws IOException {
        System.out.println("csuploadController通過");

        if(peForm.getToken() != null && peForm.getToken().equals(session.getId())) {
            Pc_Entity p = new Pc_Entity();

            p.setUser((User)session.getAttribute("login_user"));
            p.setName(peForm.getName());
            p.setName_ruby(peForm.getName_ruby());
            p.setRelease_flag(peForm.getRelease_flag());

            p.setStr(peForm.getStr());
            p.setCon(peForm.getCon());
            p.setDex(peForm.getDex());
            p.setSiz(peForm.getSiz());

            p.setAvoidance_add(peForm.getAvoidance_add());
            p.setKick_add(peForm.getKick_add());
            p.setFist_add(peForm.getFist_add());
            p.setHeadbutt_add(peForm.getHeadbutt_add());

            p.setImg_path("");

            peForm.setToken(session.getId());
            mv.addObject("pc" , peForm);
            mv = new ModelAndView("redirect:/"); // リダイレクト
            System.out.println("登録出来た！");


        }

      //以下息抜きに調べたディレクトリ作成方法メモ。
        String user_id = peForm.getUser() + "";
        String img_id =  peForm.getId() + "" ;


         File images = new File("C:/pleiades/workspace/MyApplications/src/main/resources/images/" + user_id + "/" + img_id);
         if(images.mkdirs()) { //フォルダ作成に成功するとtrue、失敗するとfalseを返す。もう存在している場合はfalse
            System.out.println("イメージフォルダの作成に成功しました");
        }else {
            System.out.println("イメージフォルダの作成に失敗しました");
        }


        //MultipartFile型をInputStream型にキャストしてる(入出力出来るように)
        InputStream image = peForm.getCs_img().getInputStream();
        BufferedInputStream reader = new BufferedInputStream(image);

        try (
            FileOutputStream img = new FileOutputStream("C:/Users/81806/git/MyApplications/src/main/resources/images/img.jpg");
            BufferedOutputStream writer = new BufferedOutputStream(img);
        ){
            int data;
            while((data = reader.read()) != -1) {
                writer.write(data);
            }

        }catch (FileNotFoundException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IOException e1) {
            // TODO 自動生成された catch ブロック
            e1.printStackTrace();
        }


            mv.setViewName("views/charactersheet/index");
            System.out.println("登録できず");

            return mv;

    }
}


