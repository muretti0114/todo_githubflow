package jp.ac.kobe_u.cs.itspecialist.todoapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import jp.ac.kobe_u.cs.itspecialist.todoapp.entity.Member;
import lombok.Data;

/**
 * メンバーの登録フォーム
 */
@Data
public class MemberForm {
    @Pattern(regexp ="[a-z0-9_\\-]{4,16}")
    String mid; //メンバーID．英小文字，数字，ハイフン，アンダーバー．4文字以上16文字以下．

    @NotBlank
    @Size(min = 1, max = 32)
    String name; //名前．最大32文字

    public Member toEntity() {
        Member m = new Member(mid, name);
        return m;
    }

}
