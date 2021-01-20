package myapp.controllers.charactersheet;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView upload(ModelAndView mv) {
        System.out.println("csuploadController通過");
        File images = new File("C:/pleiades/workspace/MyApplications/src/main/resources/images");
        if(images.mkdir()) { //フォルダ作成に成功するとtrue、失敗するとfalseを返す。もう存在している場合はfalse
            System.out.println("イメージフォルダの作成に成功しました");
        }else {
            System.out.println("イメージフォルダの作成に失敗しました");
        }

            mv.setViewName("views/charactersheet/index");

            return mv;

    }
}
