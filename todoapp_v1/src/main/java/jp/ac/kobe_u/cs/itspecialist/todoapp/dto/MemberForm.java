package jp.ac.kobe_u.cs.itspecialist.todoapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.Member;
import lombok.Data;

/**
 * メンバーの登録フォーム
 */
@Data
public class MemberForm {

    @NotBlank
    @Pattern(regexp = "[0-9a-z_\\-]+")
    String mid; //メンバーID．英数小文字，アンダーバー，ハイフン

    @NotBlank
    String name; //名前

    public Member toEntity() {
        Member m = new Member(mid, name);
        return m;
    }

}
