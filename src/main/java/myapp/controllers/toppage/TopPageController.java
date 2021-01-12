package myapp.controllers.toppage;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

//@Controller+@ResponseBody=@RestController?
@Controller  //下記参照
public class TopPageController {

    @Autowired  //インスタンス化せずにメソッドを使用できる
    HttpSession session; // セッション


  //@RequestMapping……"/"にアクセスするとindexメソッドを実行する
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView mv) {

        String test = "てすと";

        mv.addObject("test" , test);
        mv.setViewName("views/toppage/index");
        System.out.println("topcontroller起動");

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