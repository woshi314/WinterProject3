package com.java.demo.dao;

import com.java.demo.entity.PageResult;
import com.java.demo.entity.Student;
import com.java.demo.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public int addStudent(Student student) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int generatedId = -1; // Default to an invalid ID

        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into student(name,gender,age,student_no) values(?,?,?,?)";
            pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getGender());
            pstmt.setInt(3, student.getAge());
            pstmt.setString(4, student.getStudentNo());

            int count = pstmt.executeUpdate();

            if (count > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, pstmt, rs);
        }
        return generatedId;
    }

    public int addStudentsBatch(List<Student> students){
        int count=0;

        Connection conn=null;
        PreparedStatement pstmt=null;
        
        try{
            conn= JDBCUtils.getConnection();
            String sql="insert into student(name,gender,age,student_no) values(?,?,?,?)";
            pstmt = conn.prepareStatement(sql);

            for(Student stu:students){
                pstmt.setString(1,stu.getName());
                pstmt.setString(2,stu.getGender());
                pstmt.setInt(3,stu.getAge());
                pstmt.setString(4,stu.getStudentNo());

                pstmt.addBatch();
            }
            count=pstmt.executeBatch().length;

        }catch (SQLException e){
            e.printStackTrace();
            return count;
        }finally{
            JDBCUtils.close(conn,pstmt,null);
        }

        return count;
    }

    public int updateStudent(int id,String name,int age) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int count=0;

        try{
            conn= JDBCUtils.getConnection();
            String sql="update student set name=?,age=? where id=?";
            pstmt=conn.prepareStatement(sql);

            pstmt.setString(1,name);
            pstmt.setInt(2,age);
            pstmt.setInt(3,id);

            count=pstmt.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt,null);
        }
        return count;
    }

    public boolean deleteStudentAndScores(int studentId) {
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        boolean success = false;
        try {
            conn = JDBCUtils.getConnection();

            conn.setAutoCommit(false);

            String sql1 = "delete from score where student_id=?";
            pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setInt(1, studentId);
            pstmt1.executeUpdate();

            String sql2 = "delete from student where id=?";
            pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, studentId);
            int studentDeleted = pstmt2.executeUpdate();

            conn.commit();
            success = studentDeleted > 0;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            JDBCUtils.close(null, pstmt1, null);
            JDBCUtils.close(conn, pstmt2, null);
        }
        return success;
    }

    public List<Student> queryStudentByName(String name){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Student> list=new ArrayList<Student>();

        try{
            conn= JDBCUtils.getConnection();

            String sql="select * from student where name like ?";
            pstmt=conn.prepareStatement(sql);

            pstmt.setString(1,"%"+name+"%");
            rs=pstmt.executeQuery();

            while(rs.next()){
                Student student=new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setGender(rs.getString("gender"));
                student.setAge(rs.getInt("age"));
                student.setStudentNo(rs.getString("student_no"));
                student.setCreateTime(rs.getTimestamp("create_time"));
                list.add(student);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,pstmt,rs);
        }
        return list;
    }

    public Student queryStudentById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Student student = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "select * from student where id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setGender(rs.getString("gender"));
                student.setAge(rs.getInt("age"));
                student.setStudentNo(rs.getString("student_no"));
                student.setCreateTime(rs.getTimestamp("create_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(conn, pstmt, rs);
        }
        return student;
    }

    public PageResult<Student> queryStudentByPage(int page, int pageSize, String name, String gender) {
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs2 = null;
        int count = 0;
        int lowerbound = (page - 1) * pageSize;

        List<Student> list = new ArrayList<>();
        PageResult pageResult = new PageResult();

        try {
            conn = JDBCUtils.getConnection();

            String sql1 = "select count(*) from student where name like ? and gender=?";
            pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setString(1, "%" + name + "%");
            pstmt1.setString(2, gender);
            rs1 = pstmt1.executeQuery();
            if (rs1.next()) {
                count = rs1.getInt(1);
            }

            String sql2 = "select * from student where name like ? and gender=? limit ?,?";
            pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setString(1, "%" + name + "%");
            pstmt2.setString(2, gender);
            pstmt2.setInt(3, lowerbound);
            pstmt2.setInt(4, pageSize);

            rs2 = pstmt2.executeQuery();
            while (rs2.next()) {
                Student student = new Student();
                student.setId(rs2.getInt("id"));
                student.setName(rs2.getString("name"));
                student.setGender(rs2.getString("gender"));
                student.setAge(rs2.getInt("age"));
                student.setStudentNo(rs2.getString("student_no"));
                student.setCreateTime(rs2.getTimestamp("create_time"));
                list.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, pstmt1, rs1);
            JDBCUtils.close(conn, pstmt2, rs2);
        }

        pageResult.setRows(list);
        pageResult.setTotal(count);
        return pageResult;
    }
}
