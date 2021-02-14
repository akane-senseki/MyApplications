package myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:security.properties")
@SpringBootApplication
public class MyApplicationsApplication extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(MyApplicationsApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MyApplicationsApplication.class);
      }



}

//ここは一番最初に呼び出される起動クラス
//SpringBootプロジェクトで使用する全てのクラスファイルはこの起動クラスと同一パッケージもしくはその配下に配置しないと読み込まれない
//参考
//【Spring Boot】Controllerが正しく動かない原因
//https://pointsandlines.jp/server-side/java/spring-boot-controller-load-error

//パッケージの名称変更でtoppage.controllerが起動した。ので、原因は上記。