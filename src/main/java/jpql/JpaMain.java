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
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            //collection fetch join
            String query = "select t From Team t join fetch t.members";

            List<Team> result = em.createQuery(query, Team.class).getResultList();

            for (Team team : result) {
                System.out.println("team = " + team.getName() + "||" + team.getMembers().size());
                for( Member member : team.getMembers() ) {
                    System.out.println("-> member = " + member);
                 }
              }

//            for (Member member : result) {
//                System.out.println("team = " + member.getUsername() + "||" + member.getTeam().getName());
//                System.out.println("member.getClass() : " + member.getClass() + " " + "Team.getClass() : " + member.getTeam().getClass());
//                //회원1, 팀A : SQL
//                // 로그에 회원1, 팀A와 회원2, 팀1 사이에 쿼리문이 없음 => 1차 캐시에서 갖고왔음
//                //회원2, 팀A : 1차 캐시
//                //회원3, 팀B : SQL
//            }

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
