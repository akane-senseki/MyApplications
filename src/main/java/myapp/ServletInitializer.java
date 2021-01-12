package myapp;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MyApplicationsApplication.class);
    }

}

//参考
//SpringBootで作ったアプリをwarにしてデプロイしたい
//http://arcanum.hatenablog.com/entry/2019/01/24/153244

//プロジェクトのパッケージをjarからwarにするためのもの？(このプロジェクトはjar)
//デイリープロジェクトのpom.xmlには <packaging>war</packaging>の表記がある
//し、spring-boot-starter-tomcatでtoncatの設定もしてある。

//ここを設定してもencodingFilter was not registeredのエラーは無くならず