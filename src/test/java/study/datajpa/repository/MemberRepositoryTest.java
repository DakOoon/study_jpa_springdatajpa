package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void testFindByNameAndAgeGreaterThan() {
        Member member1 = new Member("memberA", 20, null);
        Member member2 = new Member("memberB", 10, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findByNameAndAgeGreaterThan("memberA", 15);

        Assertions.assertThat(findMembers.size()).isEqualTo(1);
        Assertions.assertThat(findMembers.get(0).getAge()).isEqualTo(member1.getAge());
    }

    @Test
    public void testPaging() {
        // given
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 10, null));
        memberRepository.save(new Member("member3", 10, null));
        memberRepository.save(new Member("member4", 10, null));
        memberRepository.save(new Member("member5", 10, null));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));

        // when
        Page<Member> page = memberRepository.findPageByAge(age, pageRequest);

        // for controller
        Page<MemberDto> toDto = page.map(m -> new MemberDto(m.getId(), m.getName(), null));

        // then
        List<Member> content = page.getContent();
        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void testSlicing() {
        // given
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 10, null));
        memberRepository.save(new Member("member3", 10, null));
        memberRepository.save(new Member("member4", 10, null));
        memberRepository.save(new Member("member5", 10, null));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));

        // when
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        // then
        List<Member> content = page.getContent();
        Assertions.assertThat(content.size()).isEqualTo(3);
//        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
//        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 19, null));
        memberRepository.save(new Member("member3", 20, null));
        memberRepository.save(new Member("member4", 21, null));
        memberRepository.save(new Member("member5", 40, null));

        // then
        int resultCount = memberRepository.bulkAgePlus(20);

        Assertions.assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member memberA = new Member("memberA", 10, null);
        Member memberB = new Member("memberB", 10, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        // when
        List<Member> members = memberRepository.findMemberEntityGraph();
    }

    @Test
    public void queryHint() {
        // given
        Member member = new Member("member1", 10, null);
        memberRepository.save(member);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByName(member.getName());
        findMember.changeName("member2");

        em.flush();
    }
}