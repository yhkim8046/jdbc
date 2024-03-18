package hello.jdbc.service;

import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final hello.jdbc.repository.MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection con = dataSource.getConnection();
        try {
            bozLogic(fromId, toId, money, con);
            con.commit();
        }catch (Exception e){
            con.rollback();
            throw new IllegalStateException();
        }finally {
            if(con != null){
                try{
                    con.setAutoCommit(true);
                    con.close();
                }catch(Exception e){
                    log.info("error", e);
                }
            }
        }
    }

    private void bozLogic(String fromId, String toId, int money, Connection con) throws SQLException {
        con.setAutoCommit(false);
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney()- money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney()+ money);
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }
}
