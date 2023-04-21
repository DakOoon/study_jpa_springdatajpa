package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.datajpa.entity.Member;

@Data
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String username;
    private String teamname;

    public MemberDto(Member member) {
        id = member.getId();
        username = member.getName();
        if(member.getTeam() != null) {
            teamname = member.getTeam().getName();
        } else {
            teamname = "소속 팀 없음";
        }
    }
}
