create database if not exists safsdata;
use safsdata;

/* the tables must be dropped in order */
drop table if exists teststep;
drop table if exists testcase;
drop table if exists testsuite;
drop table if exists status;
drop table if exists engine;
drop table if exists framework;
drop table if exists orderable;
drop table if exists machine;
drop table if exists user;

create table if not exists user (
	/*id int not null auto_increment,*/
	id varchar(20) not null,/*SAS user ID*/
	first_name varchar(30) not null,
	last_name varchar(30) not null,
	
	primary key(id)
);

/**
 * Machine information will be captured when running SAFS Test and will be sent to SAFS Data Service
 */
create table if not exists machine (
	id int not null auto_increment,
	name varchar(100),
	ip varchar(20),
	platform varchar(200),/* operating system? */
	
	primary key(id)
);

/**
 * This might be something that user create/modify/delete from the page. We can provide a page for that purpose.
 */
create table if not exists orderable (
	id int not null auto_increment,
	product_name varchar(200) not null,
	platform varchar(200),
	track varchar(200),
	branch varchar(200),
	
	primary key(id)
);

/**
 * the pair (name, version) should be unique
 */
create table if not exists framework (
	id int not null auto_increment,
	name varchar(100) not null, /*SeleniumPlus, SAFS*/
	version varchar(100) not null, /* 1.0*/
	description varchar(200) not null,
	
	primary key(id)
);

/**
 * the pair (name, version) should be unique
 */
create table if not exists engine (
	id int not null auto_increment,
	name varchar(100) not null, /*Selenium;  RFT*/
	version varchar(100) not null, /*2.41, 3.0; 7.0, 8.0; */
	description varchar(200) not null,
	
	primary key(id)
);

/**
 * 'status' will be refered by table 'teststep'
 * 
 * How we define the type code?
 * 0 - Success
 * 1 - Failure (do we need a failure-type for different kinds of failures?)
 * 2 - Skipped
 * 3 ...
 * according to the 'type', we write the "junit xml" as below:
 * 
 * <failure type="type" diffFile="description"/>
 * <passed type="type" desc="description"/>
 * <skipped type="type" desc="description"/>
 */
create table if not exists status (
	id int not null auto_increment,
	type varchar(50) not null,
	description varchar(200) not null,
	
	primary key(id)
);

/**
 * 'testcycle' contains 'testsuite'
 * 'testsuite' contains 'testcase'
 * 'testcase' contains 'teststep'
 * 
 * For now, I will just ignore those foreign keys ('orderable_id', 'framework_id' etc.) and start the work, we can add them later
 */
create table if not exists testcycle (
	id int not null auto_increment,
/*	orderable_id int,
	framework_id int,
	engine_id int,
	user_id varchar,
	machine_id int,
	*/
	name varchar(100) not null,
	tests int not null,
	failures int not null,
	skipped int,
	time double not null, /* it is how long it gets all testsuites done, in millisecond? */
	timestamp datetime, /* it is when this testsuites starts? */
	
	command_line varchar(500),
	
	primary key(id)
/*	primary key(id),
	foreign key (orderable_id) references orderable(id),
	foreign key (framework_id) references framework(id),
	foreign key (engine_id) references engine(id),
	foreign key (user_id) references user(id),
	foreign key (machine_id) references machine(id)
	*/
);

/**
 * 'testsuite' contains 'testcase' which contains 'teststep'
 * 
 */
create table if not exists testsuite (
	id int not null auto_increment,
    testcycle_id int,
	name varchar(100) not null,
	tests int not null,
	failures int not null,
	skipped int,
	time double not null, /* it is how long it gets all testcases done, in millisecond? */
	timestamp datetime, /* it is when this testsuite starts? */
	
	primary key(id),
	foreign key (testcycle_id) references testcycle(id)
);

create table if not exists testcase (
	id int not null auto_increment,
	testsuite_id int not null,
	name varchar(100) not null,
	time double not null, /* it is how long it gets all teststeps done, in millisecond? */
	
	primary key(id),
	foreign key (testsuite_id) references testsuite(id)
);

/**
 * This 'teststep' table might be needed.
 * If we don't want to keep every teststep, we can just write the failed ones (from the SAFS/SeleniumPlus side).
 * Later if we want to track down all the steps, it will be easier.
 */
create table if not exists teststep (
	id int not null auto_increment,
	testcase_id int not null,
	status_id int not null,
	log_message varchar(200) not null,
	
	primary key(id),
	foreign key (testcase_id) references testcase(id),
	foreign key (status_id) references status(id)
);
