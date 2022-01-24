package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)  // JPA 데이터 변경은 무조건 Transactional 안에 있어야 함. 안전성을 위해 전역적으로 readOnly 설정
@RequiredArgsConstructor    // final 필드 대상으로 Constructor 생성
public class MemberService {

    private final MemberRepository memberRepository;

    /*
     @Autowired
     // 생성자가 단 하나인 경우 Autowired 생략 가능
     public MemberService(MemberRepository memberRepository) {
         this.memberRepository = memberRepository;
     }
     */

    @Transactional(readOnly = false)    // 데이터 변경이 필요하면 readOnly false 설정
    public Long join(Member member) {
        validateDuplicateMember(member);    // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> foundMembers = memberRepository.findByName(member.getName());
        if (!foundMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member> findMembers() {     // 자동으로 transactional, readOnly 설정됨
        return memberRepository.findAll();
    }

    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }

}
