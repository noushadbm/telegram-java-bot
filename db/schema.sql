
create table task_lock(
  task_id varchar(64) NOT NULL,
  last_execution NUMBER default 0 NOT NULL,
  PRIMARY KEY (task_id)
);

INSERT INTO task_lock (task_id) VALUES ('produce');
INSERT INTO task_lock (task_id) VALUES ('consume');

create table scheduled_tasks(
   scheduled_task_id varchar(64) NOT NULL,
   created_by varchar(64) NOT NULL,
   status varchar(64) default 'PENDING' NOT NULL,
   PRIMARY KEY (scheduled_task_id)
);