drop database workflowdb;
create database workflowdb;
use workflowdb;

create table Workflow(
  wf_id int AUTO_INCREMENT,
  name varchar(30) NOT NULL UNIQUE,
  constraint pk_wf PRIMARY KEY(wf_id)
);
create table Task(
  task_id int AUTO_INCREMENT,
  -- actions_permitted ENUM('Approve','Reject'),
  duration int,
  name varchar(30) NOT NULL,
  sequence_id int,
  wf_id int,
  user_id int,
  role_id int,
  constraint uk_task1 UNIQUE(name,wf_id),
  constraint pk_task PRIMARY KEY(task_id)
);
create table User(
  user_id int AUTO_INCREMENT,
  login_id varchar(20) NOT NULL UNIQUE,
  name varchar(30) NOT NULL,
  password varchar(10) NOT NULL,
  constraint pk_user PRIMARY KEY(user_id)
);
create table Role(
  role_id int AUTO_INCREMENT,
  type varchar(30) UNIQUE,
  constraint pk_role PRIMARY KEY(role_id)
);
create table UserRole(
  user_role_id int AUTO_INCREMENT,
  -- user_role varchar(30),
  user_id int,
  role_id int,
  constraint uk_user_role1 UNIQUE(user_id,role_id),
  constraint pk_user_role PRIMARY KEY(user_role_id)
);
create table WF_Inst(
  wf_inst_id int AUTO_INCREMENT,
  wf_id int,
  wf_inst_name varchar(50) UNIQUE, -- wf_inst_name = (wf_name + login_id)
  status ENUM ('Completed','Not Completed'),
  start_date datetime,
  end_date datetime,
  constraint pk_wf_inst PRIMARY KEY(wf_inst_id)
);
create table Task_Inst(
  task_inst_id int AUTO_INCREMENT,
  action ENUM('Approved','Rejected'),
  data varchar(100),
  status ENUM ('Completed','Not Started','Pending','Cancelled'),
  user_id int,
  task_id int,
  wf_inst_id int,
  start_date datetime,
  end_date datetime,
  constraint uk_task_inst1 UNIQUE(user_id,task_id,wf_inst_id),
  constraint pk_task_inst PRIMARY KEY(task_inst_id)
);

alter table Task
  add constraint fk_task_wf_id FOREIGN KEY (wf_id) REFERENCES Workflow(wf_id) ON DELETE CASCADE,
  add constraint fk_task_user_id FOREIGN KEY (user_id) REFERENCES User(user_id),
  add constraint fk_task_role_id FOREIGN KEY (role_id) REFERENCES Role(role_id);
alter table UserRole
  add constraint fk_user_role_user_id FOREIGN KEY (user_id) REFERENCES User(user_id),
  add constraint fk_user_role_role_id FOREIGN KEY (role_id) REFERENCES Role(role_id);
alter table WF_Inst
  add constraint fk_wf_inst_wf_id FOREIGN KEY (wf_id) REFERENCES Workflow(wf_id) ON DELETE CASCADE;
alter table Task_Inst
  add constraint fk_task_inst_user_id FOREIGN KEY (user_id) REFERENCES User(user_id),
  add constraint fk_task_inst_task_id FOREIGN KEY (task_id) REFERENCES Task(task_id) ON DELETE CASCADE,
  add constraint fk_task_inst_wf_inst_id FOREIGN KEY (wf_inst_id) REFERENCES WF_Inst(wf_inst_id) ON DELETE CASCADE;

insert into Role(type) values("Guest");
insert into Role(type) values("Interviwer");
insert into Role(type) values("HR");
insert into Role(type) values("Manager");

insert into User values(0,"a","Akshu","1234");
insert into User values(0,"m","Manu","1234");
insert into User values(0,"s","Soum","1234");
insert into User values(0,"v","Vaishu","1234");
insert into User values(0,"k","Kavya","1234");
insert into User values(0,"rc","RC","1234");

insert into UserRole values(0,1,1);
insert into UserRole values(0,2,1);
insert into UserRole values(0,3,4);
insert into UserRole values(0,4,1);
insert into UserRole values(0,5,2);
insert into UserRole values(0,6,2);
insert into UserRole values(0,6,3);

insert into Workflow values(0,"Employee Recruitment");

insert into Task values(0,7,"Application Review",1,1,NULL,2);
insert into Task values(0,1,"Inform candidate1",2,1,NULL,2);
insert into Task values(0,7,"Conduct Interview1",3,1,NULL,2);
insert into Task values(0,1,"Inform candidate2",4,1,NULL,2);
insert into Task values(0,7,"Conduct Interview2",5,1,6,NULL);
insert into Task values(0,1,"Inform candidate(HR)",6,1,NULL,2);
insert into Task values(0,7,"Conduct Interview(HR)",7,1,NULL,3);
insert into Task values(0,1,"Selected or Rejected",8,1,NULL,3);

insert into WF_Inst values(0,1,"Employee Recruitment_a","Not Completed","2019-01-10 17:34:11",NULL);

insert into Task_Inst values(0,"Approved","Good","Completed",5,1,1,"2019-04-10 17:34:11","2019-04-16 17:34:11");
insert into Task_Inst values(0,NULL,NULL,"Cancelled",6,1,1,"2019-04-10 17:34:11",NULL);
insert into Task_Inst values(0,NULL,NULL,"Pending",5,2,1,"2019-04-16 17:34:11",NULL);
insert into Task_Inst values(0,NULL,NULL,"Pending",6,2,1,"2019-04-16 17:34:11",NULL);
insert into Task_Inst values(0,NULL,NULL,"Not Started",5,3,1,NULL,NULL);
insert into Task_Inst values(0,NULL,NULL,"Not Started",6,3,1,NULL,NULL);
insert into Task_Inst values(0,NULL,NULL,"Not Started",5,4,1,NULL,NULL);
insert into Task_Inst values(0,NULL,NULL,"Not Started",6,4,1,NULL,NULL);
insert into Task_Inst values(0,NULL,NULL,"Not Started",6,5,1,NULL,NULL);
insert into Task_Inst values(0,NULL,NULL,"Not Started",5,6,1,NULL,NULL);
insert into Task_Inst values(0,NULL,NULL,"Not Started",6,6,1,NULL,NULL);
insert into Task_Inst values(0,NULL,NULL,"Not Started",6,7,1,NULL,NULL);
insert into Task_Inst values(0,NULL,NULL,"Not Started",3,8,1,NULL,NULL);
