package jp.ac.kobe_u.cs.itspecialist.todoapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.MemberForm;
import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.UserDetailsImpl;
import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.Member;
import jp.ac.kobe_u.cs.itspecialist.todoapp.exception.ToDoAppException;
import jp.ac.kobe_u.cs.itspecialist.todoapp.repository.MemberRepository;

/**
 * メンバーのCRUDを行うサービス
 */
@Service
public class MemberService implements UserDetailsService{
    @Autowired
    MemberRepository mRepo;
    @Autowired
    BCryptPasswordEncoder encoder;

    /**
     * メンバーを作成する (C)
     * @param form
     * @return
     */
    public Member createMember(MemberForm form) {
        //IDの重複チェック
        String mid = form.getMid();
        if (mRepo.existsById(mid)) {
            throw new ToDoAppException(ToDoAppException.MEMBER_ALREADY_EXISTS, mid + ": Member already exists");
        }

        
        Member m = form.toEntity();
        m.setPassword(encoder.encode(m.getPassword())); //エンコードしてセーブする
        return mRepo.save(m);
    }

    /**
     * メンバーを取得する (R)
     * @param mid
     * @return
     */
    public Member getMember(String mid) {
        Member m = mRepo.findById(mid).orElseThrow(
                () -> new ToDoAppException(ToDoAppException.NO_SUCH_MEMBER_EXISTS, mid + ": No such member exists"));
        return m;
    }

    /**
     * 全メンバーを取得する (R)
     * @return
     */
    public List<Member> getAllMembers() {
        return mRepo.findAll();
    }

    public boolean existById(String mid) {
        return mRepo.existsById(mid);
    }

    /**
     * メンバーを削除する (D)
     */
    public void deleteMember(String mid) {
        Member m = getMember(mid);
        mRepo.delete(m);
    }

    /* ------------------ ここから追加分  ---------------------------*/
    /**
     * Spring Securityのメソッド．ユーザIDを与えて，ユーザ詳細を生成する
     */
    @Override
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
        Member m = mRepo.findById(mid).orElseThrow(
            () -> new UsernameNotFoundException(mid + ": no such user exists")
        );
        return new UserDetailsImpl(m);
    }

    /**
     * 管理者を登録するサービス．
     */
    public Member registerAdmin(String adminPassword) {
        Member m = new Member();
        m.setMid("admin");
        m.setName("System Administrator");
        m.setPassword(encoder.encode(adminPassword));
        m.setRole("ADMIN");
        return mRepo.save(m);
    }


}
