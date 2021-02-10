package myapp;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import myapp.filters.EncodingFilter;

//Configuration=構成
@Configuration //構成クラスであることを表している
public class MyApplicationsFilter {

    @SuppressWarnings({"rawtypes","unchecked"}) //警告を抑制
    @Bean //部品
    public FilterRegistrationBean encodingFilter() {
        //FilterRegistrationBeanに格納される
        FilterRegistrationBean bean = new FilterRegistrationBean(new EncodingFilter());
        //urlパターン。可変長引数の為複数指定することも可能
        bean.addUrlPatterns("/*");
        //Filterの実行順序。昇順に実行される
        bean.setOrder(1);

        return bean;
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean //部品
    public FilterRegistrationBean loginFilter() {

        FilterRegistrationBean bean = new FilterRegistrationBean(new EncodingFilter());

        bean.addUrlPatterns("/*");
        //encodingFillter()のあとに実行される
        bean.setOrder(2);

        return bean;
    }


}


//FilterRegistrationBean はSpringBootで用意されているクラス
//Bean定義すると、Spring BootのAuto Configurationクラス内でFilterが取り出され、Embedded Serverに登録される

//参考
//Spring Bootでサーブレットフィルターを使う [Spring Boot 1.x, 2.x両対応]
//https://qiita.com/suke_masa/items/7bdfab8e974931afdac5