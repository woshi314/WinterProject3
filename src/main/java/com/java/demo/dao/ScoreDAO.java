package com.java.demo.dao;

import com.java.demo.entity.Score;
import com.java.demo.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {
    public int addScore(Score score){
        Connection conn = null;
        PreparedStatement pstmt = null;
        int count=0;
        try {
            conn= JDBCUtils.getConnection();
            String sql="insert into score(student_id,subject,score,exam_time) values(?,?,?,?)";
            pstmt=conn.prepareStatement(sql);

            pstmt.setInt(1,score.getStudentId());
            pstmt.setString(2,score.getSubject());
            pstmt.setBigDecimal(3,score.getScore());
            pstmt.setDate(4, new java.sql.Date(score.getExamTime().getTime()));

            count=pstmt.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt,null);
        }
        return count;
    }

    public int deleteScoresByStudentId(int studentId){
        Connection conn = null;
        PreparedStatement pstmt = null;

        int count=0;

        try{
            conn= JDBCUtils.getConnection();
            String sql="delete from score where student_id=?";

            pstmt=conn.prepareStatement(sql);
            pstmt.setInt(1,studentId);
            count=pstmt.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt,null);
        }
        return count;
    }

    public List<Score> queryScoresByStudentNo(String studentNo){
        Connection conn=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        List<Score> list=new ArrayList<>();
        try{
            conn= JDBCUtils.getConnection();
            String sql="select sc.* from score sc join student st on sc.student_id = st.id where st.student_no = ?";
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,studentNo);
            rs=pstmt.executeQuery();

            while(rs.next()){
                Score sc=new Score();
                sc.setId(rs.getInt("id"));
                sc.setStudentId(rs.getInt("student_id"));
                sc.setSubject(rs.getString("subject"));
                sc.setScore(rs.getBigDecimal("score"));
                sc.setExamTime(rs.getDate("exam_time"));
                list.add(sc);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt,rs);
        }
        return list;
    }
}
