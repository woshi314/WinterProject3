package com.java.demo;

import com.java.demo.dao.ScoreDAO;
import com.java.demo.dao.StudentDAO;
import com.java.demo.entity.PageResult;
import com.java.demo.entity.Score;
import com.java.demo.entity.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(com.java.demo.Main.class);

    public static void main(String[] args) throws SQLException, IOException {

        StudentDAO studentDAO = new StudentDAO();
        ScoreDAO scoreDAO = new ScoreDAO();

        log.info("=================新增操作==================");
        log.info("  （1）新增单条学生数据；");
        Student student1 = new Student();
        student1.setName("张一");
        student1.setGender("男");
        student1.setAge(17);
        student1.setStudentNo("1234567890");
        int stuId1 = studentDAO.addStudent(student1);
        log.info("      新增学生id为{}", stuId1);

        log.info("  （2）新增单条学生成绩；");
        Score score1 = new Score();
        score1.setStudentId(stuId1);
        score1.setSubject("语文");
        score1.setScore(new BigDecimal("90.00"));
        score1.setExamTime(Date.valueOf("2026-01-01"));
        int scCount1 = scoreDAO.addScore(score1);
        log.info("      新增成绩{}条", scCount1);


        log.info("=================修改操作=================");
        log.info("  （1）根据id修改学生的姓名和年龄；");
        int updateStuCount1 = studentDAO.updateStudent(1, "张四", 18);
        log.info("      修改学生{}条", updateStuCount1);


        log.info("===============查询操作=====================");
        log.info("  （1）根据id查询单个学生数据；");
        Student queryStu1 = studentDAO.queryStudentById(1);
        log.info("      学生信息:{}", queryStu1);

        log.info("  （2）根据姓名模糊查询学生数据；");
        List<Student> queryStus2 = studentDAO.queryStudentByName("张");
        for (Student queryStu2 : queryStus2) {
            log.info("      学生信息:{}", queryStu2);
        }

        log.info("  （3）根据学号查询指定学生的所有科目成绩。");
        List<Score> queryScores = scoreDAO.queryScoresByStudentNo("1234567890");
        for (Score queryScore : queryScores) {
            log.info("      学生成绩{}", queryScore);
        }

        log.info("===============删除操作==================");
        log.info("  （1）根据student_id删除学生的所有成绩；");
        int deleteStuCount1 = scoreDAO.deleteScoresByStudentId(1);
        log.info("      删除成绩{}条", deleteStuCount1);

        log.info("  （2）根据id删除学生数据及其所有成绩；");
        log.info("      (拓展要求:（1）事务控制：)");
        boolean success = studentDAO.deleteStudentAndScores(1);
        log.info("      删除{}", success ? "成功" : "失败");

        log.info("================拓展要求 ==================");
        log.info("  （2）批量新增学生：");
        List<Student> students = new ArrayList<Student>();
        Student student2 = new Student();
        student2.setName("张二");
        student2.setGender("男");
        student2.setAge(17);
        student2.setStudentNo("1234567892");
        students.add(student2);

        Student student3 = new Student();
        student3.setName("张三");
        student3.setGender("女");
        student3.setAge(18);
        student3.setStudentNo("1234567893");
        students.add(student3);

        Student student4 = new Student();
        student4.setName("张四");
        student4.setGender("男");
        student4.setAge(19);
        student4.setStudentNo("1234567894");
        students.add(student4);

        Student student5 = new Student();
        student5.setName("张五");
        student5.setGender("女");
        student5.setAge(20);
        student5.setStudentNo("1234567895");
        students.add(student5);

        Student student6 = new Student();
        student6.setName("张六");
        student6.setGender("男");
        student6.setAge(21);
        student6.setStudentNo("1234567896");
        students.add(student6);

        Student student7 = new Student();
        student7.setName("张七");
        student7.setGender("男");
        student7.setAge(22);
        student7.setStudentNo("1234567897");
        students.add(student7);

        Student student8 = new Student();
        student8.setName("张八");
        student8.setGender("男");
        student8.setAge(23);
        student8.setStudentNo("1234567898");
        students.add(student8);

        Student student9 = new Student();
        student9.setName("张九");
        student9.setGender("男");
        student9.setAge(24);
        student9.setStudentNo("1234567899");
        students.add(student9);

        Student student10 = new Student();
        student10.setName("张十");
        student10.setGender("男");
        student10.setAge(25);
        student10.setStudentNo("1234567880");
        students.add(student10);

        Student student11 = new Student();
        student11.setName("陈十一");
        student11.setGender("男");
        student11.setAge(26);
        student11.setStudentNo("1234567881");
        students.add(student11);

        int batchCount = studentDAO.addStudentsBatch(students);
        log.info("      批量新增了{}条数据", batchCount);

        log.info("  （3）条件分页查询学生：");
        int page = 2;
        int pageSize = 3;
        PageResult pageResult = studentDAO.queryStudentByPage(page, pageSize, "张", "男");
        log.info("      查询条件：第{}页, 页面大小为{}, 姓名模糊='张', 性别='男'", page, pageSize);
        log.info("      符合条件的总记录数：{}", pageResult.getTotal());
        log.info("      当前页数据：");
        for (Object student : pageResult.getRows()) {
            log.info("        {}", student);
        }
    }
}