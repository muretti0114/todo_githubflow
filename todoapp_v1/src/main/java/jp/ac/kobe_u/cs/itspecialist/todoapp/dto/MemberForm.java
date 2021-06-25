package jp.ac.kobe_u.cs.itspecialist.todoapp.dto;

import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.Member;
import lombok.Data;

/**
 * メンバーの登録フォーム
 */
@Data
public class MemberForm {
    String mid; //メンバーID．
    String name; //名前

    public Member toEntity() {
        Member m = new Member(mid, name);
        return m;
    }

}
