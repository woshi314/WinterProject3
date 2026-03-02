use winterproject3;

create table student
(
    id INT auto_increment primary key,
    name varchar(20) not null comment '姓名',
    gender char(1) check ( gender='男' or gender='女' ) not null comment '性别',
    age tinyint unsigned not null comment '学生年龄',
    student_no varchar(10) unique not null comment '学号',
    create_time datetime not null default now() comment '创建时间'
);

create table score(
    id int auto_increment primary key,
    student_id int not null ,
    subject varchar(50) not null comment '科目名称',
    score decimal(5,2) check (score>=0 and score<=100) not null comment '成绩',
    exam_time date not null comment '考试日期',
    constraint fk_student_id foreign key (student_id) references student(id)
                  on update cascade
                  on delete cascade
);