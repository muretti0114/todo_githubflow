package jp.ac.kobe_u.cs.itspecialist.todoapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jp.ac.kobe_u.cs.itspecialist.todoapp.service.MemberService;
@Configuration
@EnableWebSecurity //(1) Spring Securityを使うための設定
public class ToDoAppSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MemberService mService;
    
    @Value("${admin.pass}")
    String adminPass;

    /**
     * 静的リソースの認可設定
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/img/**", "/css/**", "/js/**");

    }

    /**
     * HTTPリクエストに対する認可，および，ログイン・ログアウト設定
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //認可の設定
        http.authorizeRequests()
       .antMatchers("/sign_in").permitAll()           //サインインページは誰でも許可
       .antMatchers("/sign_up/**").permitAll()
       .antMatchers("/admin/**").hasRole("ADMIN")     //ユーザ管理は管理者のみ許可
       .anyRequest().authenticated();                 //それ以外は全て認証必要

       //ログインの設定
       http.formLogin()                               // 1.
       .loginPage("/sign_in")                       // 2. ログインページ
       .loginProcessingUrl("/authenticate")       // 3. フォームのPOST先URL．認証処理を実行する
       .usernameParameter("mid")                  // 4. ユーザ名に該当するリクエストパラメタ
       .passwordParameter("password")             // 5. パスワードに該当するリクエストパラメタ
       .defaultSuccessUrl("/sign_in_success", true)        // 6. 成功時のページ (trueは以前どこにアクセスしてもここに遷移する設定)
       .failureUrl("/sign_in?error");               // 7. 失敗時のページ

        //ログアウトの設定
        http.logout()                               //1.
        .logoutUrl("/sign_out")                      //2. ログアウトのURL
        .logoutSuccessUrl("/sign_in?sign_out")         //3. ログアウト完了したらこのページへ
        .deleteCookies("JSESSIONID")               //4. クッキー削除
        .invalidateHttpSession(true)               //5. セッション情報消去
        .permitAll();                              //6. ログアウトはいつでもアクセスできる
    }

    /**
     * 認証の方法を設定
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(mService).passwordEncoder(passwordEncoder());
        //ついでに管理者を登録しておく
        mService.registerAdmin(adminPass);
    }

    /**
     * アプリで共通のパスワード生成器を作る．
     * @Beanをつけているので任意の箇所でAutowired可能になる
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        return bcpe;
    }
}
