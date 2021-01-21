package myapp.controllers.toppage;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import myapp.models.User;
import myapp.repositories.UserRepository;

//@Controller+@ResponseBody=@RestController?
@Controller //下記参照
public class TopPageController {

    @Autowired //インスタンス化せずにメソッドを使用できる
    HttpSession session; // セッション

    @Autowired
    UserRepository userrepository;

    //@RequestMapping……"/"にアクセスするとindexメソッドを実行する
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView mv) {

        System.out.println("toppageController通過");

        List<User> users = userrepository.findAll();

            mv.addObject("token", session.getId());
            mv.addObject("users" , users);

            if(session.getAttribute("flush") != null) {
            mv.addObject("flush",session.getAttribute("flush"));
            session.removeAttribute("flush");
            }

            mv.setViewName("views/toppage/index");

            return mv;

    }
}

//参考
//【Spring】@Controllerと@RestControllerの違い
//https://memento-mori-blog.com/spring-controller-restcontroller/

//message_boardのcontrollerは@RestController
//dayly_report_systemのcontrolelrは@Controller
//本プロジェクトで@ResetControllerにするとindexの文字のみを返される。
//@Controllerは、戻り値としてView(HTML)を返す際に使うアノテーション
//@RestControllerはJSONやXMLを返すAPIサーバー用のアノテーション
//mesage_boardはDB接続にJPAを使用しているから？確認する