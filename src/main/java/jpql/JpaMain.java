package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // WerServer가 올라오는 시점에 딱 한개만 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        // begin transaction
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(0);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(0);
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(0);
            member3.setTeam(teamB);
            em.persist(member3);

//            em.flush();
//            em.clear();

            //flush, clear를 지워도 쿼리가 나가기때문에 자동으로 flush가 되긴 함
            // 벌크 연산 - FLUSH 자동 호출 (조건: commit, query, 강제 flush)
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            System.out.println("resultCount = " + resultCount);

            //영속성 컨텍스트를 초기화, 기존에 있던 age=0을 clear 후 초기화
            em.clear();
            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("초기화 한 findMember.getAge() : " + findMember.getAge());

            //em.createQuery로 인해 영속성 컨텍스트에 있는 값들이 flush됨
            //clear가 되는 것은 아님 => 여전히 age 값이 0인채로 영속성 컨텍스트에 담겨져 있음
            //따라서 벌크 연산 후 영속성 컨텍스틀 초기화 해줘야함
            System.out.println("member1.getAge() = " + member1.getAge()); //출력결과 getAge(): 0
            System.out.println("member2.getAge() = " + member2.getAge()); //출력결과 getAge(): 0
            System.out.println("member3.getAge() = " + member3.getAge()); //출력결과 getAge(): 0

            // commit transaction
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
