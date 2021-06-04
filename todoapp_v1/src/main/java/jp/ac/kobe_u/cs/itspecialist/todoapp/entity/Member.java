package jp.ac.kobe_u.cs.itspecialist.todoapp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * メンバーエンティティ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {
    @Id
    String mid;   //メンバーID
    String name;  //名前
}
