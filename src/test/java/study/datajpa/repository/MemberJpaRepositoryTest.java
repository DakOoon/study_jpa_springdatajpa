package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void testFindByNameAndAgeGreaterThan() {
        Member member1 = new Member("memberA", 20, null);
        Member member2 = new Member("memberB", 10, null);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> findMembers = memberJpaRepository.findByNameAndAgeGreaterThan("memberA", 15);

        Assertions.assertThat(findMembers.size()).isEqualTo(1);
        Assertions.assertThat(findMembers.get(0).getAge()).isEqualTo(member1.getAge());
    }

    @Test
    public void testPaging() {
        // given
        memberJpaRepository.save(new Member("member1", 10, null));
        memberJpaRepository.save(new Member("member2", 10, null));
        memberJpaRepository.save(new Member("member3", 10, null));
        memberJpaRepository.save(new Member("member4", 10, null));
        memberJpaRepository.save(new Member("member5", 10, null));

        int age = 10;
        int offset = 1;
        int limit = 3;

        // when
        List<Member> members = memberJpaRepository.findWithPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // 페이지 계산 공식 적용 ...
        // totalPage = totalCount / size ...
        // isLastpage ...
        // isFirstPage ...

        // then
        Assertions.assertThat(members.size()).isEqualTo(3);
        Assertions.assertThat(totalCount).isEqualTo(5);
    }

    @Test
    public void bulkUpdate() {
        // given
        memberJpaRepository.save(new Member("member1", 10, null));
        memberJpaRepository.save(new Member("member2", 19, null));
        memberJpaRepository.save(new Member("member3", 20, null));
        memberJpaRepository.save(new Member("member4", 21, null));
        memberJpaRepository.save(new Member("member5", 40, null));

        // then
        int resultCount = memberJpaRepository.bulkAgePlus(20);

        Assertions.assertThat(resultCount).isEqualTo(3);
    }
}