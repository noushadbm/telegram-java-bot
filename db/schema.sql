
create table task_lock(
  task_id varchar(64) NOT NULL,
  last_execution NUMBER default 0 NOT NULL,
  PRIMARY KEY (task_id)
);

INSERT INTO task_lock (task_id) VALUES ('produce');
INSERT INTO task_lock (task_id) VALUES ('consume');
INSERT INTO task_lock (task_id) VALUES ('re-run');

create table scheduled_tasks(
   scheduled_task_id varchar(64) NOT NULL,
   created_by varchar(64) NOT NULL,
   status varchar(64) default 'PENDING' NOT NULL,
   submitted_time date default sysdate,
   retry_count number default 0,
   PRIMARY KEY (scheduled_task_id)
);