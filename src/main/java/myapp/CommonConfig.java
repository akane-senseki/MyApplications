package myapp;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class CommonConfig extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //SpringSecurityのデフォログイン・ログアウト画面の無効化
      http.authorizeRequests().antMatchers("/*").permitAll().and().formLogin().and().httpBasic();
      http.logout().logoutSuccessUrl("/").permitAll();
      //403エラーの対処
      http.csrf().disable();

    }

}