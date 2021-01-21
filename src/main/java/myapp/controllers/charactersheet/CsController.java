package myapp.controllers.charactersheet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
    public ModelAndView upload(@RequestParam("sample") MultipartFile sample  , ModelAndView mv) throws IOException {
        System.out.println("csuploadController通過");
        System.out.println(sample);

        //MultipartFile型をInputStream型にキャストしてる(入出力出来るように)
        byte [] byteArr = sample.getBytes();
        InputStream sample1 = new ByteArrayInputStream(byteArr);

        BufferedInputStream reader = new BufferedInputStream(sample1);

        try (
            FileOutputStream img = new FileOutputStream("C:/pleiades/workspace/MyApplications/src/main/resources/images/img.png");
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

            return mv;

    }
}


//以下息抜きに調べたディレクトリ作成方法メモ。
/*
 File images = new File("C:/pleiades/workspace/MyApplications/src/main/resources/images");
 if(images.mkdir()) { //フォルダ作成に成功するとtrue、失敗するとfalseを返す。もう存在している場合はfalse
    System.out.println("イメージフォルダの作成に成功しました");
}else {
    System.out.println("イメージフォルダの作成に失敗しました");
}*/