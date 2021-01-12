package myapp.controllers.toppage;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@Controller+@ResponseBody=@RestController?
@Controller  //@Controllerでも起動はした。
public class TopPageController {

    @Autowired  //インスタンス化せずにメソッドを使用できる
    HttpSession session; // セッション


  //@RequestMapping……"/"にアクセスするとindexメソッドを実行する
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model m) {

        String test = "てすと";

        m.addAttribute("test" , test);

        return "index";
    }

}
