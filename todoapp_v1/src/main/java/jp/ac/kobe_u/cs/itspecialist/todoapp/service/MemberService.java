package jp.ac.kobe_u.cs.itspecialist.todoapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.kobe_u.cs.itspecialist.todoapp.dto.MemberForm;
import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.Member;
import jp.ac.kobe_u.cs.itspecialist.todoapp.exception.ToDoAppException;
import jp.ac.kobe_u.cs.itspecialist.todoapp.repository.MemberRepository;

/**
 * メンバーのCRUDを行うサービス
 */
@Service
public class MemberService {
    @Autowired
    MemberRepository mRepo;

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

    /**
     * メンバーを更新する (U)
     */
    public Member updateMember(String mid, MemberForm form) {
        //認可チェック．他人の情報は書き換えちゃダメ！
        if (!mid.equals(form.getMid())) {
            throw new ToDoAppException(ToDoAppException.INVALID_MEMBER_OPERATION, mid +": Cannot update other's member info");
        }

        Member m = getMember(mid); //情報とってきて
        m.setName(form.getName()); //名前を書き換えて
        return mRepo.save(m);       //セーブ
    }

    /**
     * メンバーを削除する (D)
     */
    public void deleteMember(String mid) {
        Member m = getMember(mid);
        mRepo.delete(m);
    }

}
