package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByNameAndAgeGreaterThan(String name, int age);

    @Query("select m from Member m where m.name = :name and m.age = :age")
    List<Member> findByNameAndAge(@Param("name") String name, @Param("age") int age);

    @Query("select m.name from Member m")
    List<String> findNameList();

    //
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.name, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //
    @Query("select m from Member m where m.name in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    //
    Member findMemberByName(String name);
    List<Member> findListByName(String name);
    Optional<Member> findOptionalByName(String name);

    //
    Page<Member> findPageByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);
    List<Member> findListByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    //
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    //
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
//    @EntityGraph("Member.all")
    List<Member> findEntityGraphByName(@Param("name") String name);

    //
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByName(String name);

    // select for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByName(String name);
}
