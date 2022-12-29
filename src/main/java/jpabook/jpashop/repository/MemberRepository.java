package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    //저장
    public void save(Member member) {
        em.persist(member);
    }

    //
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    // jsql을 보냄
    public List<Member> findAll() {
        //Member class 엔티티에 매핑된 리스트를 다 가져오겠다
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        //                                                                  set parameter
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }


}
